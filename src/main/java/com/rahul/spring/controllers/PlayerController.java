package com.rahul.spring.controllers;

import com.rahul.spring.model.Players;
import com.rahul.spring.services.PlayerService;
import com.rahul.spring.services.PlayerServiceImpl;
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

    PlayerService playerService;
    @GetMapping("/players")
    public List<Players> getPlayers(){
        return playerService.getAllPlayers();
    }

    @GetMapping("/players/{id}")
    public Players getPlayerById(@PathVariable UUID id){
        return playerService.getPlayerById(id);
    }

    @PostMapping("/add")
    public ResponseEntity addPlayers(@RequestBody Players player){
        if(playerService.addPlayer(player)!=null){
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/edit")
    public ResponseEntity<Players> edit(@RequestBody Players player){
        playerService.editPlayer(player);
        return new ResponseEntity<Players>(HttpStatus.CREATED);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Players> deletePlayer(@PathVariable UUID id){
        if(playerService.removePlayer(id)){
            return new ResponseEntity<Players>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<Players>(HttpStatus.NOT_FOUND);
    }
}
