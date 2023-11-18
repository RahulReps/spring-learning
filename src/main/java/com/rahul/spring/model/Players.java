package com.rahul.spring.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class Players {
    UUID id;
    String name;
    String position;
    Integer jerseyNo;
    String foot;
    String playStyle;
}
