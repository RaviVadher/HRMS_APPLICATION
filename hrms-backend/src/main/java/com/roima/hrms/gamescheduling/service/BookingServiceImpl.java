package com.roima.hrms.gamescheduling.service;

import com.roima.hrms.auth.model.UserPrincipal;
import com.roima.hrms.gamescheduling.dto.BookingRequestDto;
import com.roima.hrms.gamescheduling.dto.BookingResponseDto;
import com.roima.hrms.gamescheduling.entity.Booking;
import com.roima.hrms.gamescheduling.entity.BookingPlayer;
import com.roima.hrms.gamescheduling.entity.GameSlot;
import com.roima.hrms.gamescheduling.entity.PlayerStats;
import com.roima.hrms.gamescheduling.enums.BookingStatus;
import com.roima.hrms.gamescheduling.enums.SlotStatus;
import com.roima.hrms.gamescheduling.exception.NotAllowedException;
import com.roima.hrms.gamescheduling.exception.NotFoundException;
import com.roima.hrms.gamescheduling.exception.TimeOutException;
import com.roima.hrms.gamescheduling.repository.BookingPlayerRepository;
import com.roima.hrms.gamescheduling.repository.BookingRepository;
import com.roima.hrms.gamescheduling.repository.GameSlotRepository;
import com.roima.hrms.gamescheduling.repository.PlayerStatsRepository;
import com.roima.hrms.mail.EmailService;
import com.roima.hrms.mail.EmailTemplate;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final GameSlotRepository gameSlotRepository;
    private final BookingPlayerRepository bookingPlayerRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final EmailService emailService;

    public BookingServiceImpl(UserRepository userRepository,
                              BookingRepository bookingRepository,
                              GameSlotRepository gameSlotRepository,
                              BookingPlayerRepository bookingPlayerRepository,
                              PlayerStatsRepository playerStatsRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.gameSlotRepository = gameSlotRepository;
        this.bookingPlayerRepository = bookingPlayerRepository;
        this.playerStatsRepository = playerStatsRepository;
        this.emailService = emailService;
    }


    //initial booking
    @Override
    public BookingResponseDto bookSlot(BookingRequestDto dto) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GameSlot slot = gameSlotRepository.findById(dto.getSlotId()).orElseThrow(()-> new NotFoundException("slot not found"));
        ValidateBooking(slot);
        slot.setBookedCount(slot.getBookedCount() + 1);

        Long gameId = slot.getGame().getId();
        Long userId = userPrincipal.getUserId();

        int completed = getComplated(userId, gameId);
        //slot is free
        if (completed == 0 && slot.getStatus() == SlotStatus.Available) {
            slot.setStatus(SlotStatus.UnAvailable);
            return createBooking(dto,userId, slot, BookingStatus.Conformed, "Slot Confirmed");
        }
        return createBooking(dto, userId,slot, BookingStatus.Waiting, "Slot not Confirmed");
    }


         //actual booking method
        private BookingResponseDto createBooking(BookingRequestDto dto,Long userId, GameSlot slot,BookingStatus status,String msg)
        {
            User user = userRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("user not found"));
            Booking  booking = new Booking();
            booking.setSlot(slot);
            booking.setBookedBy(user);
            booking.setBookedAt(LocalDateTime.now());
            booking.setStatus(status);
            bookingRepository.save(booking);

            for(Long id : dto.getTeamUserId())
            {
                User u = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("user not found"));
                BookingPlayer player = new  BookingPlayer();

                player.setBooking(booking);
                player.setUser(u);
                player.setJoinedAt(LocalDateTime.now());
                player.setSlot(slot);
                bookingPlayerRepository.save(player);
                emailService.sendEmail(u.getEmail(),"Game Slot Booked", EmailTemplate.gameScheduling(u.getName(),user.getName(),slot.getStartTime(),slot.getEndTime(),slot.getGame().getGameName(),status));
            }
            BookingResponseDto bookingDto = new BookingResponseDto();
            bookingDto.setBookingId(booking.getId());
            bookingDto.setStatus(status);
            bookingDto.setMessage(msg);
            return bookingDto;
        }

        //validate booking
        private void ValidateBooking(GameSlot slot) {
          LocalTime lastAllowed = slot.getStartTime().minusHours(1);
          if(LocalTime.now().isAfter(lastAllowed)) {
              throw new TimeOutException("Booking time excited");
          }
        }


        //get complated slots
        private int getComplated(Long userId, Long gameId) {
            return playerStatsRepository.findByUserIdAndGameId(userId,gameId)
                    .map(PlayerStats::getCompletedSlots)
                    .orElse(0);
        }


    //waiting to booked or cancel
    @Override
    public void processWaitingBookings()
    {
         LocalTime limit = LocalTime.now().plusHours(1);
        List<GameSlot> slots =gameSlotRepository.findSlotStartingIsNow(limit);

        for(GameSlot slot : slots)
        {
            List<Booking> confirmed = bookingRepository.findBySlotIdAndStatus(slot.getId(),BookingStatus.Conformed);
            List<Booking> waiting = bookingRepository.findBySlotIdAndStatus(slot.getId(),BookingStatus.Waiting);

            if(waiting.isEmpty())
                continue;

            if (!confirmed.isEmpty())
            {
                cancelAll(waiting);
                continue;
            }

            Booking win = getHighestPriority(waiting,slot.getGame().getId());
            win.setStatus(BookingStatus.Conformed);
            emailService.sendEmail(win.getBookedBy().getEmail(),"Game Slot Conformed", "You waiting slot now converted into Conformed.");

            waiting.remove(win);
            slot.setStatus(SlotStatus.UnAvailable);
            cancelAll(waiting);
        }
    }


    //priority checking
    private Booking getHighestPriority(List<Booking> waitingList, Long gameId) {
        Booking highest = null;
        for(Booking b : waitingList)
        {
            if(highest==null || getComplated(b.getBookedBy().getId(),gameId) > getComplated(highest.getBookedBy().getId(),gameId))
            {
                highest=b;
            }
        }
        return highest;
    }


    //before starting slot update  all waiting to canceled
    private void cancelAll(List<Booking> waitingList)
    {
        for(Booking b : waitingList)
        {
            b.setStatus(BookingStatus.Cancelled);
            bookingRepository.save(b);
        }
    }



    //update the slot status which are already finished
    @Override
    public void  updateStatsAfterSlotEnd()
    {
        LocalTime time = LocalTime.now();
        List<GameSlot> slots =gameSlotRepository.findFinishedSlots(time);

        for (GameSlot slot : slots)
        {
            List<Booking> confirmed = bookingRepository.findBySlotIdAndStatus(slot.getId(),BookingStatus.Conformed);
            if(confirmed.isEmpty())
            {
                continue;
            }

            Booking booking = confirmed.get(0);
            List<BookingPlayer> players = bookingPlayerRepository.findByBookingId(booking.getId());

            for (BookingPlayer player : players)
            {
                playerStatsRepository.incComplatedSlot(slot.getGame().getId(),player.getUser().getId());
            }
            checkResetCycle(slot.getGame().getId());
        }
    }


      //checking for reset cycle
       private  void checkResetCycle(Long gameId)
       {
            List<PlayerStats> list = playerStatsRepository.countNotPlayed(gameId);
            if(list.isEmpty())
            {
               playerStatsRepository.resetCycle(gameId);
            }
       }


    //slot cancel
    @Override
    public void cancelSlot(Long bookedId)
    {
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Booking booking = bookingRepository.findById(bookedId).orElseThrow(()-> new NotFoundException("Booking not found"));

        if(!user.getUserId().equals(booking.getBookedBy().getId()))
        {
            throw new NotAllowedException("you have not allowed to cancel booked slot");
        }

        LocalTime time = booking.getSlot().getStartTime().minusHours(1);
        if(LocalTime.now().isAfter(time))
        {
            throw new TimeOutException("Booking cancel time excited");
        }
        if(BookingStatus.Conformed.equals(booking.getStatus()))
        {
            booking.getSlot().setStatus(SlotStatus.Available);
        }
        booking.setStatus(BookingStatus.Cancelled);

    }
}
