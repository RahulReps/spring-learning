package com.rahul.spring.services;

import com.rahul.spring.model.PlayerCSVRecord;

import java.io.File;
import java.util.List;

public interface PlayerCsvService {
    List<PlayerCSVRecord> convertCSV(File csvFile);
}
