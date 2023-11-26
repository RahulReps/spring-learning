package com.rahul.spring.services;

import com.rahul.spring.model.Players;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
@Service
public class PlayerServiceImpl implements PlayerService {
    private Map<UUID, Players> playerList;
    public PlayerServiceImpl(){
        playerList = new HashMap<>();

        Players player1 = Players.builder()
                .id(UUID.randomUUID())
                .foot("left")
                .name("Leonal Messi")
                .position("LW")
                .jerseyNo(10)
                .playStyle("Playmaker")
                .build();

        Players player2 = Players.builder()
                .id(UUID.randomUUID())
                .foot("right")
                .name("Cristiano Ronaldo")
                .position("CF")
                .jerseyNo(7)
                .playStyle("Striker")
                .build();

        Players player3 = Players.builder()
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
    public List<Players> getAllPlayers() {
        return new ArrayList<>(playerList.values());
    }

    @Override
    public Optional<Players> getPlayerById(UUID id) {
        return Optional.of(playerList.get(id));
    }

    @Override
    public Players addPlayer(Players player) {
        Players savePlayer = Players.builder()
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
    public Boolean editPlayer(Players players) {
        if(playerList.containsKey(players.getId())){
            Players existing = playerList.get(players.getId());
            existing.setName(players.getName());
            existing.setFoot(players.getFoot());
            existing.setPosition(players.getPosition());
            existing.setJerseyNo(players.getJerseyNo());
            existing.setPlayStyle(players.getPlayStyle());
            return true;
        }
        return false;
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
    public void patchPlayer(UUID id, Players players) {
        Players existing = playerList.get(id);
        if(StringUtils.hasText(players.getName())){
            existing.setName(players.getName());
        }
        if(StringUtils.hasText(players.getPosition())){
            existing.setPosition(players.getPosition());
        }
        if(StringUtils.hasText(players.getFoot())){
            existing.setFoot(players.getFoot());
        }
        if(StringUtils.hasText(players.getPlayStyle())){
            existing.setPlayStyle(players.getPlayStyle());
        }
        if(players.getJerseyNo()!=null){
            existing.setJerseyNo(players.getJerseyNo());
        }

    }
}
