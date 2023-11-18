package com.rahul.spring.services;

import com.rahul.spring.model.Players;

import java.util.List;
import java.util.UUID;

public interface PlayerService {
    List<Players> getAllPlayers();
    Players getPlayerById(UUID id);
    Players addPlayer(Players player);
    Boolean editPlayer(Players players);
    Boolean removePlayer(UUID id);

}
