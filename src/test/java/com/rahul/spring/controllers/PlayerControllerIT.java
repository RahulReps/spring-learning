package com.rahul.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.spring.entities.Player;
import com.rahul.spring.mappers.PlayerMapper;
import com.rahul.spring.model.PlayerDTO;
import com.rahul.spring.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PlayerControllerIT {
    @Autowired
    PlayerController playerController;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PlayerMapper playerMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetPlayersByPlayerNameAndPlayStyle2() throws Exception {
        mockMvc.perform(get(PlayerController.GET_URI)
                        .queryParam("playStyle", "Anc")
                        .queryParam("pageNumber", "1")
                        .queryParam("pageSize","5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(5)));
    }

    @Test
    void testGetPlayersByPlayerNameAndPlayStyle() throws Exception {
        mockMvc.perform(get(PlayerController.GET_URI)
                        .queryParam("playStyle", "Anc")
                        .queryParam("playerName", "KEV"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(1)));
    }
    @Test
    void testGetPlayersByPlayStyle() throws Exception {
        mockMvc.perform(get(PlayerController.GET_URI)
                            .queryParam("playStyle", "Anc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(10)));
    }

    @Test
    void testGetPlayersByName() throws Exception {
        mockMvc.perform(get(PlayerController.GET_URI)
                .queryParam("playerName", "Kev"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(4)));
    }
    @Test
    void testInvalidPatchPlayerName() throws Exception{
        Player player = playerRepository.findAll().get(0);

        Map<String, Object> playerMap = new HashMap<>();
        playerMap.put("name","1234567890123456789012345678901234567890123456789012345678901234567890");

        MvcResult result = mockMvc.perform(patch("/players/patch/" + player.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void testInvalidPatchPlayer(){
        assertThrows(NotFoundException.class, () ->{
            playerController.patchPlayer(UUID.randomUUID(), PlayerDTO.builder().build());
        });
    }
    @Rollback
    @Transactional
    @Test
    void testPatchPlayer(){
        Player player = playerRepository.findAll().get(0);
        PlayerDTO playerDTO = playerMapper.playerToPlayerDto(player);
        playerDTO.setName("Updated Name");
        playerDTO.setId(null);

        ResponseEntity responseEntity = playerController.patchPlayer(player.getId(), playerDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(204);

        Player updated = playerRepository.findById(player.getId()).get();
        assertThat(updated.getName()).isEqualTo("Updated Name");
    }
    @Test
    void testInvalidDeletePlayer(){
        assertThrows(NotFoundException.class, () ->{
            playerController.deletePlayer(UUID.randomUUID());
        });
    }
    @Rollback
    @Transactional
    @Test
    void testDeletePlayer(){
        Player player = playerRepository.findAll().get(0);
        playerController.deletePlayer(player.getId());
        assertThat(playerRepository.findById(player.getId())).isEmpty();
    }
    @Test
    void testInvalidUpdatePlayer(){
        assertThrows(NotFoundException.class, () ->{
            playerController.edit(UUID.randomUUID(), PlayerDTO.builder().build());
        });
    }
    @Transactional
    @Rollback
    @Test
    void testUpdatePlayer() {
        Player player = playerRepository.findAll().get(0);
        PlayerDTO playerDTO = playerMapper.playerToPlayerDto(player);
        playerDTO.setName("Updated Name");
        playerDTO.setId(null);

        ResponseEntity responseEntity = playerController.edit(player.getId(), playerDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);

        Player updated = playerRepository.findById(player.getId()).get();
        assertThat(updated.getName()).isEqualTo("Updated Name");
    }

    @Rollback
    @Transactional
    @Test
    void testCreatePlayer() {
        PlayerDTO playerDTO = PlayerDTO.builder()
                .foot("right")
                .name("Gavi")
                .position("CMF")
                .jerseyNo(17)
                .playStyle("Playmaker")
                .build();
        ResponseEntity responseEntity = playerController.addPlayers(playerDTO);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] location = responseEntity.getHeaders().getLocation().toString().split("/");
        UUID storedUUID = UUID.fromString(location[2]);

        Player player = playerRepository.findById(storedUUID).get();
        assertThat(player).isNotNull();
    }

    @Test
    void testPlayerIdNotFound(){
        assertThrows(NotFoundException.class, () -> {
            playerController.getPlayerById(UUID.randomUUID());
        });
    }
    @Test
    void testGetPlayerById() {
        Player player = playerRepository.findAll().get(0);
        PlayerDTO playerDTO = playerController.getPlayerById(player.getId());
        assertThat(playerDTO.getId()).isEqualTo(player.getId());
    }

    @Test
    void testGetAllPlayers() {
        Page<PlayerDTO> playerDTOS = playerController.getPlayers(null, null, 1, 1000);
        assertThat(playerDTOS.getContent().size()).isEqualTo(100);
    }
    @Rollback
    @Transactional
    @Test
    void testEmptyPlayerList() {
        playerRepository.deleteAll();
        Page<PlayerDTO> playerDTOS = playerController.getPlayers(null, null, 1, 10);
        assertThat(playerDTOS.getContent().size()).isEqualTo(0);
    }
}