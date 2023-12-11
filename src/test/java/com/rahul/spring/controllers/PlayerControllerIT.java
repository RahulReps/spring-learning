package com.rahul.spring.controllers;

import com.rahul.spring.entities.Player;
import com.rahul.spring.mappers.PlayerMapper;
import com.rahul.spring.model.PlayerDTO;
import com.rahul.spring.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PlayerControllerIT {
    @Autowired
    PlayerController playerController;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PlayerMapper playerMapper;

    @Test
    void testInvalidPatchPlayer(){
        assertThrows(NotFoundException.class, () ->{
            playerController.patchPlayer(UUID.randomUUID(), PlayerDTO.builder().build());
        });
    }
    @Rollback
    @Transactional
    @Test
    void testPatchPlayer(){
        Player player = playerRepository.findAll().get(0);
        PlayerDTO playerDTO = playerMapper.playerToPlayerDto(player);
        playerDTO.setName("Updated Name");
        playerDTO.setId(null);

        ResponseEntity responseEntity = playerController.patchPlayer(player.getId(), playerDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(204);

        Player updated = playerRepository.findById(player.getId()).get();
        assertThat(updated.getName()).isEqualTo("Updated Name");
    }
    @Test
    void testInvalidDeletePlayer(){
        assertThrows(NotFoundException.class, () ->{
            playerController.deletePlayer(UUID.randomUUID());
        });
    }
    @Rollback
    @Transactional
    @Test
    void testDeletePlayer(){
        Player player = playerRepository.findAll().get(0);
        playerController.deletePlayer(player.getId());
        assertThat(playerRepository.findById(player.getId())).isEmpty();
    }
    @Test
    void testInvalidUpdatePlayer(){
        assertThrows(NotFoundException.class, () ->{
            playerController.edit(UUID.randomUUID(), PlayerDTO.builder().build());
        });
    }
    @Transactional
    @Rollback
    @Test
    void testUpdatePlayer() {
        Player player = playerRepository.findAll().get(0);
        PlayerDTO playerDTO = playerMapper.playerToPlayerDto(player);
        playerDTO.setName("Updated Name");
        playerDTO.setId(null);

        ResponseEntity responseEntity = playerController.edit(player.getId(), playerDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);

        Player updated = playerRepository.findById(player.getId()).get();
        assertThat(updated.getName()).isEqualTo("Updated Name");
    }

    @Rollback
    @Transactional
    @Test
    void testCreatePlayer() {
        PlayerDTO playerDTO = PlayerDTO.builder()
                .foot("right")
                .name("Gavi")
                .position("CMF")
                .jerseyNo(17)
                .playStyle("Playmaker")
                .build();
        ResponseEntity responseEntity = playerController.addPlayers(playerDTO);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] location = responseEntity.getHeaders().getLocation().toString().split("/");
        UUID storedUUID = UUID.fromString(location[2]);

        Player player = playerRepository.findById(storedUUID).get();
        assertThat(player).isNotNull();
    }

    @Test
    void testPlayerIdNotFound(){
        assertThrows(NotFoundException.class, () -> {
            playerController.getPlayerById(UUID.randomUUID());
        });
    }
    @Test
    void testGetPlayerById() {
        Player player = playerRepository.findAll().get(0);
        PlayerDTO playerDTO = playerController.getPlayerById(player.getId());
        assertThat(playerDTO.getId()).isEqualTo(player.getId());
    }

    @Test
    void testGetAllPlayers() {
        List<PlayerDTO> playerDTOS = playerController.getPlayers();
        assertThat(playerDTOS.size()).isEqualTo(3);
    }
    @Rollback
    @Transactional
    @Test
    void testEmptyPlayerList() {
        playerRepository.deleteAll();
        List<PlayerDTO> playerDTOS = playerController.getPlayers();
        assertThat(playerDTOS.size()).isEqualTo(0);
    }
}