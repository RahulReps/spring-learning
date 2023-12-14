package com.rahul.spring.repositories;

import com.rahul.spring.entities.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class PlayerRepositoryTest {
    @Autowired
    PlayerRepository playerRepository;
    @Test
    void testSavePlayer(){
        Player player = playerRepository.save(Player.builder()
                    .name("Rahul")
                        .foot("Right")
                        .jerseyNo(17)
                        .playStyle("Hole player")
                        .position("AMF")
                    .build());

        playerRepository.flush();
        assertThat(player.getId()).isNotNull();
    }
}