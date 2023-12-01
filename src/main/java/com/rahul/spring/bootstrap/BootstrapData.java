package com.rahul.spring.bootstrap;

import com.rahul.spring.entities.Player;
import com.rahul.spring.model.PlayerDTO;
import com.rahul.spring.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final PlayerRepository playerRepository;
    @Override
    public void run(String... args) throws Exception {
        if(playerRepository.count()==0){
            Player player1 = Player.builder()
                    .foot("left")
                    .name("Leonal Messi")
                    .position("LW")
                    .jerseyNo(10)
                    .playStyle("Playmaker")
                    .build();

            Player player2 = Player.builder()
                    .foot("right")
                    .name("Cristiano Ronaldo")
                    .position("CF")
                    .jerseyNo(7)
                    .playStyle("Striker")
                    .build();

            Player player3 = Player.builder()
                    .foot("left")
                    .name("Haaland")
                    .position("CF")
                    .jerseyNo(9)
                    .playStyle("Poacher")
                    .build();
            playerRepository.saveAll(Arrays.asList(player1,player2,player3));
        }
    }
}
