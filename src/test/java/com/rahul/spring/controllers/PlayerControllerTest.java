package com.rahul.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.spring.model.PlayerDTO;
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
import java.util.Optional;
import java.util.UUID;

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
    ArgumentCaptor<PlayerDTO> playersArgumentCaptor;

    @Test
    void testCreatePlayerNullName() throws Exception {

        PlayerDTO playerDTO = PlayerDTO.builder().build();

        given(playerService.addPlayer(any(PlayerDTO.class))).willReturn(playerServiceImpl.getAllPlayers(null, null, 1, 10).getContent().get(1));

        mockMvc.perform(post("/players/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(7)));
    }
    @Test
    void testNotFoundException() throws Exception{
        given(playerService.getPlayerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(PlayerController.APP_URI_GET_ID,UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPatchPlayer() throws Exception{
        PlayerDTO playerDTO = playerServiceImpl.getAllPlayers(null, null, 1, 10).getContent().get(0);
        given(playerService.patchPlayer(any(),any())).willReturn(true);
        Map<String, Object> playerMap = new HashMap<>();
        playerMap.put("name","Penaldo");

        mockMvc.perform(patch("/players/patch/" + playerDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerMap)))
                .andExpect(status().isNoContent());

        verify(playerService).patchPlayer(uuidArgumentCaptor.capture(),playersArgumentCaptor.capture());

        assertThat(playerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(playerMap.get("name")).isEqualTo(playersArgumentCaptor.getValue().getName());
    }

    @Test
    void testDeletePlayer() throws Exception {
        PlayerDTO player = playerServiceImpl.getAllPlayers(null, null, 1, 10).getContent().get(0);
        given(playerService.removePlayer(any())).willReturn(true);
        mockMvc.perform(delete("/players/delete/" + player.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        verify(playerService).removePlayer(uuidArgumentCaptor.capture());

        assertThat(player.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdatePlayer() throws Exception{
        PlayerDTO playerDTO = playerServiceImpl.getAllPlayers(null, null, 1, 10).getContent().get(0);
        given(playerService.editPlayer(any(), any())).willReturn(Optional.of(playerDTO));
        mockMvc.perform(put("/players/edit/"+playerDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerDTO)))
                .andExpect(status().isCreated());

        verify(playerService).editPlayer(any(UUID.class),any(PlayerDTO.class));
    }
    @Test
    void testCreatePlayer() throws Exception {
        PlayerDTO playerDTO = playerServiceImpl.getAllPlayers(null, null, 1, 10).getContent().get(0);
        playerDTO.setId(null);

        given(playerService.addPlayer(any(PlayerDTO.class))).willReturn(playerServiceImpl.getAllPlayers(null, null, 1, 10).getContent().get(1));

        mockMvc.perform(post("/players/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetPlayers() throws Exception{
        given(playerService.getAllPlayers(any(), any(), any(), any())).willReturn(playerServiceImpl.getAllPlayers(null, null, 1, 10));

        mockMvc.perform(get("/players/players")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(11)));
    }
    @Test
    void testGetPlayerById() throws Exception{

        PlayerDTO playerDTO = playerServiceImpl.getAllPlayers(null, null, 1, 10).getContent().get(0);

        given(playerService.getPlayerById(playerDTO.getId())).willReturn(Optional.of(playerDTO));

        mockMvc.perform(get(PlayerController.APP_URI_GET_ID, playerDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(playerDTO.getId().toString())));
    }
}
