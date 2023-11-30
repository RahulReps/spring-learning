package com.rahul.spring.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Player {
    @Id
    UUID id;
    String name;
    String position;
    Integer jerseyNo;
    String foot;
    String playStyle;
}
