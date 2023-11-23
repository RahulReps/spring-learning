package com.rahul.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.spring.controllers.PlayerController;
import com.rahul.spring.model.Players;
import com.rahul.spring.services.PlayerService;
import com.rahul.spring.services.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;
@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    PlayerService playerService;
    PlayerServiceImpl playerServiceImpl;
    @Autowired
    ObjectMapper objectMapper;
    @BeforeEach
    void setUp(){
        playerServiceImpl = new PlayerServiceImpl();
    }

    @Test
    void testCreatePlayer() throws Exception {
        Players players = playerServiceImpl.getAllPlayers().get(0);
        players.setId(null);

        given(playerService.addPlayer(any(Players.class))).willReturn(playerServiceImpl.getAllPlayers().get(1));

        mockMvc.perform(post("/players/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(players)))
                .andExpect(status().isCreated());
    }

    @Test
    void getPlayers() throws Exception{
        given(playerService.getAllPlayers()).willReturn(playerServiceImpl.getAllPlayers());

        mockMvc.perform(get("/players/players")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }
    @Test
    void getPlayerById() throws Exception{

        Players players = playerServiceImpl.getAllPlayers().get(0);

        given(playerService.getPlayerById(players.getId())).willReturn(players);

        mockMvc.perform(get("/players/players/" + players.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(players.getId().toString())));
    }
}
