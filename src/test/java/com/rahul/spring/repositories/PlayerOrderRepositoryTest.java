package com.rahul.spring.repositories;

import com.rahul.spring.entities.Account;
import com.rahul.spring.entities.Player;
import com.rahul.spring.entities.PlayerOrder;
import com.rahul.spring.entities.PlayerOrderShipment;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlayerOrderRepositoryTest {

    @Autowired
    PlayerOrderRepository playerOrderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PlayerRepository playerRepository;

    Account testAccount;
    Player testPlayer;

    @BeforeEach
    void setUp() {
        testAccount = accountRepository.findAll().get(0);
        testPlayer = playerRepository.findAll().get(0);
    }
    @Transactional
    @Test
    void testOrders() {
        PlayerOrder playerOrder = PlayerOrder.builder()
                .accountRef("Test order")
                .account(testAccount)
                .playerOrderShipment(PlayerOrderShipment.builder()
                        .trackingNumber("1234")
                        .build())
                .build();

        PlayerOrder saved = playerOrderRepository.save(playerOrder);

        System.out.println(saved.getAccountRef());
    }
}