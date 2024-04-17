package com.rahul.spring.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AccountDTO {
    private UUID id;
    private String name;
    private String email;
}
