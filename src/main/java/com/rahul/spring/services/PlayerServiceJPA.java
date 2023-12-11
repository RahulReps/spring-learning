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
import java.util.concurrent.atomic.AtomicReference;
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
        return playerMapper.playerToPlayerDto(playerRepository.save(playerMapper.playerDtoToPlayer(player)));
    }

    @Override
    public Optional<PlayerDTO> editPlayer(UUID id, PlayerDTO playerDTO) {
        AtomicReference<Optional<PlayerDTO>> atomicReference = new AtomicReference<>();

        playerRepository.findById(id).ifPresentOrElse(player -> {
            player.setName(playerDTO.getName());
            player.setFoot(playerDTO.getFoot());
            player.setJerseyNo(playerDTO.getJerseyNo());
            player.setPlayStyle(playerDTO.getPlayStyle());
            player.setPosition(playerDTO.getPosition());
            atomicReference.set(Optional.of(playerMapper.playerToPlayerDto(playerRepository.save(player))));
        }, () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }

    @Override
    public Boolean removePlayer(UUID id) {
        if(playerRepository.existsById(id)){
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean patchPlayer(UUID id, PlayerDTO playerDTO) {
        AtomicReference<Boolean> result= new AtomicReference<>(true);
        playerRepository.findById(id).ifPresentOrElse(player -> {
            if(playerDTO.getName() != null){
                player.setName(playerDTO.getName());
            }
            if(playerDTO.getFoot() != null){
                player.setFoot(playerDTO.getFoot());
            }
            if(playerDTO.getJerseyNo() != null){
                player.setJerseyNo(playerDTO.getJerseyNo());
            }
            if(playerDTO.getPlayStyle() != null){
                player.setPlayStyle(playerDTO.getPlayStyle());
            }
            if(playerDTO.getPosition() != null){
                player.setPosition(playerDTO.getPosition());
            }
            playerRepository.save(player);
            result.set(true);
        }, ()->{
            result.set(false);
        });
        return result.get();
    }
}
