package com.roima.hrms.gamescheduling.service;

import com.roima.hrms.gamescheduling.dto.SlotResponseDto;
import com.roima.hrms.gamescheduling.entity.Game;
import com.roima.hrms.gamescheduling.entity.GameConfig;
import com.roima.hrms.gamescheduling.entity.GameSlot;
import com.roima.hrms.gamescheduling.enums.SlotStatus;
import com.roima.hrms.gamescheduling.exception.NotFoundException;
import com.roima.hrms.gamescheduling.mapper.SlotMapper;
import com.roima.hrms.gamescheduling.repository.GameConfigRepository;
import com.roima.hrms.gamescheduling.repository.GameRepository;
import com.roima.hrms.gamescheduling.repository.GameSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class SlotServiceImpl implements SlotService {

    private final GameRepository gameRepository;
    private final GameConfigRepository gameConfigRepository;
    private final GameSlotRepository gameSlotRepository;

    public SlotServiceImpl(GameRepository gameRepository, GameConfigRepository gameConfigRepository, GameSlotRepository gameSlotRepository) {
        this.gameRepository = gameRepository;
        this.gameConfigRepository = gameConfigRepository;
        this.gameSlotRepository = gameSlotRepository;
    }


    //slot generation for all games at ones
    @Override
    public void generateSlotForGames(){

        List<Game> games = gameRepository.findAll();

        for(Game game : games){
            generateSlotForGame(game.getId(), LocalDate.now());
        }
    }

    //method used for slot generation
    @Override
    public void generateSlotForGame(Long gameId, LocalDate date){

        Game game =  gameRepository.findById(gameId).orElseThrow(()->new NotFoundException("Game not found"));
        GameConfig config= gameConfigRepository.findByGameId(gameId).orElseThrow(()->new NotFoundException("Game config not found"));

        if(gameSlotRepository.existsByGameIdAndSlotDate(gameId,date))
        {
            return;
        }

        LocalTime start = config.getStartTime();
        LocalTime end = config.getEndTime();

        while (start.isBefore(end)){

            GameSlot gameSlot = new GameSlot();
            gameSlot.setGame(game);
            gameSlot.setStartTime(start);
            gameSlot.setEndTime(start.plusMinutes(config.getSlotDuration()));
            gameSlot.setSlotDate(date);
            gameSlot.setBookedCount(0);
            gameSlot.setStatus(SlotStatus.Available);

            gameSlotRepository.save(gameSlot);
            start = start.plusMinutes(config.getSlotDuration());
        }
    }

    //get slot by gameId
    @Override
    public List<SlotResponseDto> getSlotForGame(Long gameId)
    {
        gameRepository.findById(gameId).orElseThrow(()->new NotFoundException("Game not found"));

        return gameSlotRepository.findByGameIdAndSlotDate(gameId,LocalDate.now())
                .stream()
                .map(SlotMapper::toDto)
                .toList();

    }


}
