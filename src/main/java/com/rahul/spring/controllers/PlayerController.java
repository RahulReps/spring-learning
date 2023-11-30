package com.rahul.spring.controllers;

import com.rahul.spring.model.PlayerDTO;
import com.rahul.spring.services.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity addPlayers(@RequestBody PlayerDTO player){
        if(playerService.addPlayer(player)!=null){
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/edit")
    public ResponseEntity<PlayerDTO> edit(@RequestBody PlayerDTO player){
        playerService.editPlayer(player);
        return new ResponseEntity<PlayerDTO>(HttpStatus.CREATED);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<PlayerDTO> deletePlayer(@PathVariable UUID id){
        playerService.removePlayer(id);
        return new ResponseEntity<PlayerDTO>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<PlayerDTO> patchPlayer(@PathVariable UUID id, @RequestBody PlayerDTO playerDTO){
        playerService.patchPlayer(id, playerDTO);
        return new ResponseEntity<PlayerDTO>(HttpStatus.NO_CONTENT);
    }
}
