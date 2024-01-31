package com.rahul.spring.bootstrap;

import com.rahul.spring.repositories.PlayerRepository;
import com.rahul.spring.repositories.AccountRepository;
import com.rahul.spring.services.PlayerCsvService;
import com.rahul.spring.services.PlayerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PlayerCsvServiceImpl.class)
class BootstrapDataTest {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PlayerCsvService playerCsvService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp(){
        bootstrapData = new BootstrapData(playerRepository, playerCsvService, accountRepository);
    }
    @Test
    void testRun() throws Exception {
        bootstrapData.run(null);

        assertThat(playerRepository.count()).isEqualTo(573);
    }
}