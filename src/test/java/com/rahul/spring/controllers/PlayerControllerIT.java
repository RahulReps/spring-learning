package com.rahul.spring.controllers;

import com.rahul.spring.entities.Player;
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