package com.rahul.spring.bootstrap;

import com.rahul.spring.repositories.PlayerRepository;
import com.rahul.spring.services.PlayerCsvService;
import com.rahul.spring.services.PlayerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Import(PlayerCsvServiceImpl.class)
class BootstrapDataTest {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerCsvService playerCsvService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp(){
        bootstrapData = new BootstrapData(playerRepository, playerCsvService);
    }
    @Test
    void testRun() throws Exception {
        bootstrapData.run(null);

        assertThat(playerRepository.count()).isEqualTo(573);
    }
}