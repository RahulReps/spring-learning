package com.rahul.spring.controllers;

import com.rahul.spring.entities.Player;
import com.rahul.spring.model.PlayerDTO;
import com.rahul.spring.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PlayerControllerIT {
    @Autowired
    PlayerController playerController;
    @Autowired
    PlayerRepository playerRepository;

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