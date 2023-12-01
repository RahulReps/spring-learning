package com.rahul.spring.bootstrap;

import com.rahul.spring.repositories.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class BootstrapDataTest {
    @Autowired
    PlayerRepository playerRepository;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp(){
        bootstrapData = new BootstrapData(playerRepository);
    }
    @Test
    void testRun() throws Exception {
        bootstrapData.run(null);

        assertThat(playerRepository.count()).isEqualTo(3);
    }
}