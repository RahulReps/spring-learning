package com.rahul.spring.controller;

import com.rahul.spring.controllers.PlayerController;
import com.rahul.spring.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
public class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    PlayerService playerService;

    @Test
    void getPlayerById() throws Exception{
        mockMvc.perform(get("/players/players/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
