package com.roima.hrms.gamescheduling.service;

import com.roima.hrms.auth.model.UserPrincipal;
import com.roima.hrms.gamescheduling.dto.GameConfigRequestDto;
import com.roima.hrms.gamescheduling.dto.GameConfigResponseDto;
import com.roima.hrms.gamescheduling.dto.GameInterestUserDto;
import com.roima.hrms.gamescheduling.dto.GameResponseDto;
import com.roima.hrms.gamescheduling.entity.Game;
import com.roima.hrms.gamescheduling.entity.GameConfig;
import com.roima.hrms.gamescheduling.entity.GameInterest;
import com.roima.hrms.gamescheduling.entity.PlayerStats;
import com.roima.hrms.gamescheduling.exception.ConfigExistException;
import com.roima.hrms.gamescheduling.exception.NotFoundException;
import com.roima.hrms.gamescheduling.mapper.GameConfigMapper;
import com.roima.hrms.gamescheduling.mapper.GameMapper;
import com.roima.hrms.gamescheduling.mapper.InterestUserMapper;
import com.roima.hrms.gamescheduling.repository.GameConfigRepository;
import com.roima.hrms.gamescheduling.repository.GameInterestRepository;
import com.roima.hrms.gamescheduling.repository.GameRepository;
import com.roima.hrms.gamescheduling.repository.PlayerStatsRepository;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GameServiceImpl implements GameService {


    private final GameRepository gameRepository;
    private final GameConfigRepository gameConfigRepository;
    private final UserRepository userRepository;
    private final GameInterestRepository gameInterestRepository;
    private final PlayerStatsRepository  playerStatsRepository;

    public GameServiceImpl(GameRepository gameRepository,
                           GameConfigRepository gameConfigRepository,
                           UserRepository userRepository,
                           GameInterestRepository gameInterestRepository,
                           PlayerStatsRepository playerStatsRepository) {
        this.gameRepository = gameRepository;
        this.gameConfigRepository = gameConfigRepository;
        this.userRepository = userRepository;
        this.gameInterestRepository = gameInterestRepository;
        this.playerStatsRepository = playerStatsRepository;
    }

    //create game
    @Override
    public ResponseEntity<String> createGame(String gameName)
    {
        Game game = new Game();
        game.setGameName(gameName);
        gameRepository.save(game);

        return ResponseEntity.ok("Game created successfully");
    }

    //get All active and inActive games
    @Override
    public List<GameResponseDto> getAllGames()
    {
        return gameRepository.findAll()
                .stream()
                .map(GameMapper::toDto)
                .toList();
    }

    //create first configuration
    @Override
    public ResponseEntity<String> configGame(Long gameId, GameConfigRequestDto dto)
    {
        Optional<GameConfig> gameConfig = gameConfigRepository.findByGameId(gameId);
        if(gameConfig.isPresent())
        {
            throw new ConfigExistException("This Game have already configuration");
        }
        Game game = gameRepository.findById(gameId).orElseThrow(()->  new NotFoundException("Game not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getUserId()).orElseThrow(()->new UsernameNotFoundException("User not found"));

        GameConfig config = new GameConfig();
        config.setGame(game);
        config.setCreatedBy(user.getName());
        config.setCreatedAt(LocalDateTime.now());
        config.setStartTime(dto.getStartTime());
        config.setEndTime(dto.getEndTime());
        config.setMaxPlayers(dto.getCapacity());
        config.setSlotDuration(dto.getSlotDuration());

        gameConfigRepository.save(config);
        return ResponseEntity.ok().body("Game created successfully");
    }


    //get game specific config
    @Override
    public GameConfigResponseDto getConfigGame(Long gameId)
    {
        GameConfig config = gameConfigRepository.findByGameId(gameId).orElseThrow(()->new NotFoundException("Game Config not found"));
        return GameConfigMapper.toDto(config);
    }

    @Override
    public ResponseEntity<String> updateConfigGame(Long gameId,GameConfigRequestDto dto){

        GameConfig config = gameConfigRepository.findByGameId(gameId).orElseThrow(()->new NotFoundException("Game Config not found"));

        if(dto.getStartTime() != null){
            config.setStartTime(dto.getStartTime());
        }
        if(dto.getEndTime() != null){
            config.setEndTime(dto.getEndTime());
        }
        if(dto.getSlotDuration() != null){
            config.setSlotDuration(dto.getSlotDuration());
        }
        if(dto.getCapacity()!= null){
            config.setMaxPlayers(dto.getCapacity());
        }

        gameConfigRepository.save(config);

        return ResponseEntity.ok().body("Game Configuration  updated successfully");
    }


    //interested game
    @Override
    public ResponseEntity<String> interestedGame(Long gameId){

        Game game = gameRepository.findById(gameId).orElseThrow(()->new NotFoundException("Game not found"));

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =  userRepository.findById(userPrincipal.getUserId()).orElseThrow(()->new UsernameNotFoundException("User not found"));

        if(gameInterestRepository.existsByUserIdAndGameId(user.getId(),gameId))
        {
            return ResponseEntity.ok().body("You have already submitted Game interested successfully");
        }

        GameInterest gameInterest = new GameInterest();
        gameInterest.setGame(game);
        gameInterest.setUser(user);
        gameInterestRepository.save(gameInterest);

        PlayerStats stats = new PlayerStats();
        stats.setGame(game);
        stats.setUser(user);
        playerStatsRepository.save(stats);

        return ResponseEntity.ok("Your Interest for Game is save successfully");
    }

    //get all interested user by gameID
    @Override
    public List<GameInterestUserDto> getGameInterested(Long gameId){

        gameRepository.findById(gameId).orElseThrow(()->new NotFoundException("Game not found"));

        return gameInterestRepository.findUserByGameId(gameId)
                .stream()
                .map(InterestUserMapper::toDto)
                .toList();

    }

    //get user specific game interest
    @Override
    public List<GameInterestUserDto> getMyGameInterested(){

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =  userRepository.findById(userPrincipal.getUserId()).orElseThrow(()->new UsernameNotFoundException("User not found"));

        return gameInterestRepository.findGameByUserId(user.getId())
                .stream()
                .map(InterestUserMapper::toDto)
                .toList();

    }


}
