package com.rahul.spring.bootstrap;

import com.rahul.spring.entities.Player;
import com.rahul.spring.model.PlayerCSVRecord;
import com.rahul.spring.model.PlayerDTO;
import com.rahul.spring.repositories.PlayerRepository;
import com.rahul.spring.services.PlayerCsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final PlayerRepository playerRepository;
    private final PlayerCsvService playerCsvService;
    private final List<String> PLAYSTYLES = Arrays.asList("Goal Poacher", "Prolific Winger", "Roaming Flank", "Hole Player", "Playmaker", "Anchor Man", "Build Up", "Destroyer");
    @Override
    public void run(String... args) throws Exception {
        loadData();
        loadCsvData();
    }

    private void loadCsvData() throws FileNotFoundException {
        if(playerRepository.count()<10){
            File file = ResourceUtils.getFile("classpath:csvdata/players.csv");
            List<PlayerCSVRecord> playerCsvServiceList = playerCsvService.convertCSV(file);
            playerCsvServiceList.forEach(playerCSVRecord -> {
                playerRepository.save(Player.builder()
                                .name(playerCSVRecord.getName())
                                .foot(new java.util.Random().nextBoolean() ? "left" : "right")
                                .club(playerCSVRecord.getCurrent_club())
                                .position(playerCSVRecord.getPosition())
                                .jerseyNo(playerCSVRecord.getJersey())
                                .playStyle(playerCSVRecord.getPlaystyle())
                        .build());
            });
        }
    }

    private void loadData() {
        if (playerRepository.count()==0) {
            Player player1 = Player.builder()
                    .foot("left")
                    .name("Leonal Messi")
                    .club("Barcelona")
                    .position("LW")
                    .jerseyNo(10)
                    .playStyle("Playmaker")
                    .build();

            Player player2 = Player.builder()
                    .foot("right")
                    .name("Cristiano Ronaldo")
                    .club("Real Madrid")
                    .position("CF")
                    .jerseyNo(7)
                    .playStyle("Striker")
                    .build();

            Player player3 = Player.builder()
                    .foot("left")
                    .name("Haaland")
                    .club("Manchester City")
                    .position("CF")
                    .jerseyNo(9)
                    .playStyle("Poacher")
                    .build();
            playerRepository.saveAll(Arrays.asList(player1,player2,player3));
        }
    }
}
