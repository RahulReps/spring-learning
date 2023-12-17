package com.rahul.spring.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerCSVRecord {

    @CsvBindByName
    private String name;

    @CsvBindByName
    private Integer age;

    @CsvBindByName
    private String birthday_GMT;

    @CsvBindByName
    private String season;

    @CsvBindByName
    private String position;

    @CsvBindByName(column = "Current Club")
    private String current_club;

    @CsvBindByName
    private Integer minutes_played_overall;

    @CsvBindByName
    private String nationality;

    @CsvBindByName
    private Integer appearances_overall;

    @CsvBindByName
    private Integer goals_overall;

    @CsvBindByName
    private Integer assists_overall;

    @CsvBindByName
    private Integer penalty_goals;

    @CsvBindByName
    private Integer penalty_misses;

    @CsvBindByName
    private Integer clean_sheets_overall;

    @CsvBindByName
    private Integer conceded_overall;

    @CsvBindByName
    private Integer yellow_cards_overall;

    @CsvBindByName
    private Integer red_cards_overall;

    @CsvBindByName
    private Integer jersey;

    @CsvBindByName
    private String playstyle;
}
