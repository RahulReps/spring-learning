package com.rahul.spring.repositories;

import com.rahul.spring.entities.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
    Page<Player> findAllByNameIsLikeIgnoreCase(String playerName, Pageable pageable);

    Page<Player> findAllByPlayStyleIsLikeIgnoreCase(String playStyle, Pageable pageable);

    Page<Player> findAllByNameIsLikeIgnoreCaseAndPlayStyleIsLikeIgnoreCase(String playerName, String playerStyle, Pageable pageable);
}
