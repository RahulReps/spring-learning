package com.rahul.spring.services;

import com.rahul.spring.model.PlayerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
@Service
public class PlayerServiceImpl implements PlayerService {
    private Map<UUID, PlayerDTO> playerList;
    public PlayerServiceImpl(){
        playerList = new HashMap<>();

        PlayerDTO player1 = PlayerDTO.builder()
                .id(UUID.randomUUID())
                .foot("left")
                .name("Leonal Messi")
                .position("LW")
                .jerseyNo(10)
                .playStyle("Playmaker")
                .build();

        PlayerDTO player2 = PlayerDTO.builder()
                .id(UUID.randomUUID())
                .foot("right")
                .name("Cristiano Ronaldo")
                .position("CF")
                .jerseyNo(7)
                .playStyle("Striker")
                .build();

        PlayerDTO player3 = PlayerDTO.builder()
                .id(UUID.randomUUID())
                .foot("left")
                .name("Haaland")
                .position("CF")
                .jerseyNo(9)
                .playStyle("Poacher")
                .build();

        playerList.put(player1.getId(),player1);
        playerList.put(player2.getId(),player2);
        playerList.put(player3.getId(),player3);
    }


    @Override
    public List<PlayerDTO> getAllPlayers(String playerName, String playStyle) {
        return new ArrayList<>(playerList.values());
    }

    @Override
    public Optional<PlayerDTO> getPlayerById(UUID id) {
        return Optional.of(playerList.get(id));
    }

    @Override
    public PlayerDTO addPlayer(PlayerDTO player) {
        PlayerDTO savePlayer = PlayerDTO.builder()
                .id(UUID.randomUUID())
                .foot(player.getFoot())
                .name(player.getName())
                .position(player.getPosition())
                .jerseyNo(player.getJerseyNo())
                .playStyle(player.getPlayStyle())
                .build();
        playerList.put(savePlayer.getId(),savePlayer);
        return savePlayer;
    }

    @Override
    public Optional<PlayerDTO> editPlayer(UUID id, PlayerDTO playerDTO) {
        PlayerDTO existing = playerList.get(id);
        existing.setName(playerDTO.getName());
        existing.setFoot(playerDTO.getFoot());
        existing.setPosition(playerDTO.getPosition());
        existing.setJerseyNo(playerDTO.getJerseyNo());
        existing.setPlayStyle(playerDTO.getPlayStyle());
        return Optional.of(existing);
    }

    @Override
    public Boolean removePlayer(UUID id) {
        if(playerList.containsKey(id)){
            playerList.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean patchPlayer(UUID id, PlayerDTO playerDTO) {
        PlayerDTO existing = playerList.get(id);
        if(StringUtils.hasText(playerDTO.getName())){
            existing.setName(playerDTO.getName());
        }
        if(StringUtils.hasText(playerDTO.getPosition())){
            existing.setPosition(playerDTO.getPosition());
        }
        if(StringUtils.hasText(playerDTO.getFoot())){
            existing.setFoot(playerDTO.getFoot());
        }
        if(StringUtils.hasText(playerDTO.getPlayStyle())){
            existing.setPlayStyle(playerDTO.getPlayStyle());
        }
        if(playerDTO.getJerseyNo() != null){
            existing.setJerseyNo(playerDTO.getJerseyNo());
        }
        return true;
    }
}
