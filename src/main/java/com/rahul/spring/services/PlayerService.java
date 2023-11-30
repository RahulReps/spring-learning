package com.rahul.spring.services;

import com.rahul.spring.model.PlayerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerService {
    List<PlayerDTO> getAllPlayers();
    Optional<PlayerDTO> getPlayerById(UUID id);
    PlayerDTO addPlayer(PlayerDTO player);
    Boolean editPlayer(PlayerDTO playerDTO);
    Boolean removePlayer(UUID id);
    void patchPlayer(UUID id, PlayerDTO playerDTO);

}
