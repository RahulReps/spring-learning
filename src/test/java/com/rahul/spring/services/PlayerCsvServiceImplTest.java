package com.rahul.spring.services;

import com.rahul.spring.model.PlayerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlayerCsvServiceImplTest {

    PlayerCsvService playerCsvService = new PlayerCsvServiceImpl();

    @Test
    void convertToCSV() throws FileNotFoundException{

        File file = ResourceUtils.getFile("classpath:csvdata/players.csv");

        List<PlayerCSVRecord> recs = playerCsvService.convertCSV(file);

        System.out.println(recs);

        assertThat(recs.size()).isGreaterThan(0);
    }

}