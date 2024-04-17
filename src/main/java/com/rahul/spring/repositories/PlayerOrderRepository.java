package com.rahul.spring.repositories;

import com.rahul.spring.entities.PlayerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerOrderRepository extends JpaRepository<PlayerOrder, UUID> {
}
