package com.rahul.spring.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class PlayerDTO {
    UUID id;
    @NotBlank
    @NotNull
    String name;
    @NotNull
    @NotBlank
    String position;
    @NotNull
    Integer jerseyNo;
    @NotNull
    @NotBlank
    String foot;
    String playStyle;
}
