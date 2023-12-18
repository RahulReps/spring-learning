package com.rahul.spring.repositories;

import com.rahul.spring.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
    List<Player> findAllByNameIsLikeIgnoreCase(String playerName);

    List<Player> findAllByPlayStyleIsLikeIgnoreCase(String playStyle);
}
