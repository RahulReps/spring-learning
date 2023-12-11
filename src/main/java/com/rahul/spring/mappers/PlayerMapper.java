package com.rahul.spring.mappers;

import com.rahul.spring.entities.Player;
import com.rahul.spring.model.PlayerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface PlayerMapper {

    Player playerDtoToPlayer (PlayerDTO playerDTO);
    PlayerDTO playerToPlayerDto (Player player);

}
