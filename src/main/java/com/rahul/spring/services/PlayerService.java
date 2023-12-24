package com.rahul.spring.services;

import com.rahul.spring.model.PlayerDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface PlayerService {
    Page<PlayerDTO> getAllPlayers(String playerName, String playStyle, Integer pageNumber, Integer pageSize);
    Optional<PlayerDTO> getPlayerById(UUID id);
    PlayerDTO addPlayer(PlayerDTO player);
    Optional<PlayerDTO> editPlayer(UUID id, PlayerDTO playerDTO);
    Boolean removePlayer(UUID id);
    Boolean patchPlayer(UUID id, PlayerDTO playerDTO);

}
