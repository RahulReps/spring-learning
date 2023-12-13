package com.rahul.spring.controllers;

import com.rahul.spring.model.PlayerDTO;
import com.rahul.spring.services.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/players")
public class PlayerController {
    public static final String APP_URI = "/players";
    public static final String APP_URI_GET_ID = "/players/players/{id}";
    PlayerService playerService;
    @GetMapping("/players")
    public List<PlayerDTO> getPlayers(){
        return playerService.getAllPlayers();
    }

    @GetMapping("/players/{id}")
    public PlayerDTO getPlayerById(@PathVariable UUID id){
        return playerService.getPlayerById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping("/add")
    public ResponseEntity addPlayers(@Validated @RequestBody PlayerDTO player){
        PlayerDTO playerDTO = playerService.addPlayer(player);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", APP_URI+"/"+playerDTO.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<PlayerDTO> edit(@PathVariable UUID id, @RequestBody PlayerDTO player){
        if(playerService.editPlayer(id,player).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity<PlayerDTO>(HttpStatus.CREATED);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<PlayerDTO> deletePlayer(@PathVariable UUID id){
        if(!playerService.removePlayer(id)){
            throw new NotFoundException();
        }
        return new ResponseEntity<PlayerDTO>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<PlayerDTO> patchPlayer(@PathVariable UUID id, @RequestBody PlayerDTO playerDTO){
        if(!playerService.patchPlayer(id, playerDTO)){
            throw new NotFoundException();
        }
        return new ResponseEntity<PlayerDTO>(HttpStatus.NO_CONTENT);
    }
}
