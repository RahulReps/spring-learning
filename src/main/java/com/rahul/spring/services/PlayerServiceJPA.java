package com.rahul.spring.services;

import com.rahul.spring.mappers.PlayerMapper;
import com.rahul.spring.model.PlayerDTO;
import com.rahul.spring.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class PlayerServiceJPA implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    @Override
    public List<PlayerDTO> getAllPlayers() {
        return playerRepository.findAll().stream().map(playerMapper::playerToPlayerDto).collect(Collectors.toList());
    }

    @Override
    public Optional<PlayerDTO> getPlayerById(UUID id) {
        return Optional.ofNullable(playerMapper.playerToPlayerDto(playerRepository.findById(id).orElse(null)));
    }

    @Override
    public PlayerDTO addPlayer(PlayerDTO player) {
        return null;
    }

    @Override
    public Boolean editPlayer(PlayerDTO playerDTO) {
        return null;
    }

    @Override
    public Boolean removePlayer(UUID id) {
        return null;
    }

    @Override
    public void patchPlayer(UUID id, PlayerDTO playerDTO) {

    }
}
