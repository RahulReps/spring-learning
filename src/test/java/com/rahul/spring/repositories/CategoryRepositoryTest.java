package com.rahul.spring.repositories;

import com.rahul.spring.entities.Category;
import com.rahul.spring.entities.Player;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PlayerRepository playerRepository;

    Player testPlayer;

    @BeforeEach
    void setUp(){
        testPlayer = playerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void addCategory() {
        Category sampleCategory = categoryRepository.save(Category.builder().description("Test Cat").build());

        testPlayer.addCategory(sampleCategory);
        Player savePlayer = playerRepository.save(testPlayer);

        System.out.println(savePlayer.getName());
    }
}