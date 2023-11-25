package com.rahul.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.spring.controllers.PlayerController;
import com.rahul.spring.model.Players;
import com.rahul.spring.services.PlayerService;
import com.rahul.spring.services.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<Players> playersArgumentCaptor;

    @Test
    void testPatchPlayer() throws Exception{
        Players players = playerServiceImpl.getAllPlayers().get(0);

        Map<String, Object> playerMap = new HashMap<>();
        playerMap.put("name","Penaldo");

        mockMvc.perform(patch("/players/patch/" + players.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerMap)))
                .andExpect(status().isNoContent());

        verify(playerService).patchPlayer(uuidArgumentCaptor.capture(),playersArgumentCaptor.capture());

        assertThat(players.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(playerMap.get("name")).isEqualTo(playersArgumentCaptor.getValue().getName());
    }

    @Test
    void testDeletePlayer() throws Exception {
        Players player = playerServiceImpl.getAllPlayers().get(0);

        mockMvc.perform(delete("/players/delete/" + player.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        verify(playerService).removePlayer(uuidArgumentCaptor.capture());

        assertThat(player.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdatePlayer() throws Exception{
        Players players = playerServiceImpl.getAllPlayers().get(0);

        mockMvc.perform(put("/players/edit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(players)))
                .andExpect(status().isCreated());

        verify(playerService).editPlayer(any(Players.class));
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
    void testGetPlayers() throws Exception{
        given(playerService.getAllPlayers()).willReturn(playerServiceImpl.getAllPlayers());

        mockMvc.perform(get("/players/players")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }
    @Test
    void testGetPlayerById() throws Exception{

        Players players = playerServiceImpl.getAllPlayers().get(0);

        given(playerService.getPlayerById(players.getId())).willReturn(players);

        mockMvc.perform(get(PlayerController.APP_URI_GET_ID, players.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(players.getId().toString())));
    }
}
