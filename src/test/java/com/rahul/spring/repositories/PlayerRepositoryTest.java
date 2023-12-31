package com.rahul.spring.repositories;

import com.rahul.spring.bootstrap.BootstrapData;
import com.rahul.spring.entities.Player;
import com.rahul.spring.services.PlayerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Import({BootstrapData.class, PlayerCsvServiceImpl.class})
class PlayerRepositoryTest {
    @Autowired
    PlayerRepository playerRepository;

    @Test
    void testPlayersByNameAndPlayStyle(){
        Page<Player> list = playerRepository.findAllByNameIsLikeIgnoreCaseAndPlayStyleIsLikeIgnoreCase("%KEV%", "%Anc%", null);

        assertThat(list.getContent().size()).isEqualTo(1);
    }
    @Test
    void testPlayersByName(){
        Page<Player> list = playerRepository.findAllByNameIsLikeIgnoreCase("%KEV%", null);

        assertThat(list.getContent().size()).isEqualTo(4);
    }

    @Test
    void testPlayersByPlayStyle(){
        Page<Player> list = playerRepository.findAllByPlayStyleIsLikeIgnoreCase("%Anc%", PageRequest.of(0,10));

        assertThat(list.getContent().size()).isEqualTo(10);
    }
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