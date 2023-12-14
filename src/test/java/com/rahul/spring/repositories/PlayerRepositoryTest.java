package com.rahul.spring.repositories;

import com.rahul.spring.entities.Player;
import jakarta.validation.ConstraintViolationException;
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
    void testNameTooLong(){
        assertThrows(ConstraintViolationException.class, () ->{
            Player player = playerRepository.save(Player.builder()
                    .name("sed elementum tempus egestas sed sed risus pretium quam vulputate dignissim suspendisse in est ante in nibh mauris cursus mattis molestie a iaculis at erat pellentesque adipiscing commodo elit at imperdiet dui accumsan sit amet nulla facilisi morbi tempus iaculis urna id volutpat lacus laoreet non curabitur gravida arcu ac tortor dignissim convallis aenean et")
                    .foot("Right")
                    .jerseyNo(17)
                    .playStyle("Hole player")
                    .position("AMF")
                    .build());
            playerRepository.flush();
        });
    }
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