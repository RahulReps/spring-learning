package com.rahul.spring.services;

import com.rahul.spring.entities.Player;
import com.rahul.spring.mappers.PlayerMapper;
import com.rahul.spring.model.PlayerDTO;
import com.rahul.spring.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class PlayerServiceJPA implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final CacheManager cacheManager;

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;

    @Cacheable(cacheNames = "playerListCache")
    @Override
    public Page<PlayerDTO> getAllPlayers(String playerName, String playStyle, Integer pageNumber, Integer pageSize) {

        log.info("List Beers - service");

        Page<Player> playerList;
        PageRequest pageRequest = this.pageRequestBuilder(pageNumber, pageSize);

        if (StringUtils.hasText(playerName) && playStyle==null){
            playerList = getPlayersByName(playerName, pageRequest);
        }
        else if(StringUtils.hasText(playStyle) && playerName==null){
            playerList = getPlayersByPlayStyle(playStyle, pageRequest);
        }
        else if(StringUtils.hasText(playStyle) && StringUtils.hasText(playerName)){
            playerList = getPlayersByNameAndPlayStyle(playerName, playStyle, pageRequest);
        }
        else{
            playerList = playerRepository.findAll(pageRequest);
        }
        return playerList.map(playerMapper::playerToPlayerDto);
    }

    public PageRequest pageRequestBuilder(Integer pageNumber, Integer pageSize){
        int queryPageNumber = DEFAULT_PAGE;
        int queryPageSize = DEFAULT_PAGE_SIZE;

        if(pageNumber != null && pageNumber > 0){
            queryPageNumber = pageNumber-1;
        }

        if(pageSize != null){
            if(pageSize > 100){
                queryPageSize = 100;
            }
            else{
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("club"));
        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<Player> getPlayersByNameAndPlayStyle(String playerName, String playStyle, PageRequest pageRequest) {
        return playerRepository.findAllByNameIsLikeIgnoreCaseAndPlayStyleIsLikeIgnoreCase("%" + playerName + "%", "%" + playStyle + "%", pageRequest);
    }

    public Page<Player> getPlayersByPlayStyle(String playStyle, PageRequest pageRequest){
        return playerRepository.findAllByPlayStyleIsLikeIgnoreCase("%" + playStyle + "%", pageRequest);
    }

    public Page<Player> getPlayersByName(String playerName, PageRequest pageRequest){
        return playerRepository.findAllByNameIsLikeIgnoreCase("%" + playerName + "%", pageRequest);
    }

    @Cacheable(cacheNames = "playerCache", key = "#id")
    @Override
    public Optional<PlayerDTO> getPlayerById(UUID id) {
        log.info("Get play by id - service");
        return Optional.ofNullable(playerMapper.playerToPlayerDto(playerRepository.findById(id).orElse(null)));
    }

    @Override
    public PlayerDTO addPlayer(PlayerDTO player) {
        if(cacheManager.getCache("playerListCache") != null){
            cacheManager.getCache("playerListCache").clear();
        }

        return playerMapper.playerToPlayerDto(playerRepository.save(playerMapper.playerDtoToPlayer(player)));
    }

    @Override
    public Optional<PlayerDTO> editPlayer(UUID id, PlayerDTO playerDTO) {
        clearCache(id);

        AtomicReference<Optional<PlayerDTO>> atomicReference = new AtomicReference<>();

        playerRepository.findById(id).ifPresentOrElse(player -> {
            player.setName(playerDTO.getName());
            player.setFoot(playerDTO.getFoot());
            player.setJerseyNo(playerDTO.getJerseyNo());
            player.setPlayStyle(playerDTO.getPlayStyle());
            player.setPosition(playerDTO.getPosition());
            atomicReference.set(Optional.of(playerMapper.playerToPlayerDto(playerRepository.save(player))));
        }, () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }

    private void clearCache(UUID id) {
        if(cacheManager.getCache("playerListCache") != null){
            cacheManager.getCache("playerListCache").clear();
        }

        if(cacheManager.getCache("playerCache") != null){
            cacheManager.getCache("playerCache").evict(id);
        }
    }

    //    @Caching(
//            evict = {
//                    @CacheEvict(cacheNames = "playerCache", key = "#id"),
//                    @CacheEvict(cacheNames = "playerListCache")
//            }
//    )
    @Override
    public Boolean removePlayer(UUID id) {
        clearCache(id);
        if(playerRepository.existsById(id)){
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean patchPlayer(UUID id, PlayerDTO playerDTO) {
        clearCache(id);
        cacheManager.getCache("playerCache").evict(id);
        cacheManager.getCache("playerListCache").clear();

        AtomicReference<Boolean> result= new AtomicReference<>(true);
        playerRepository.findById(id).ifPresentOrElse(player -> {
            if(playerDTO.getName() != null){
                player.setName(playerDTO.getName());
            }
            if(playerDTO.getFoot() != null){
                player.setFoot(playerDTO.getFoot());
            }
            if(playerDTO.getJerseyNo() != null){
                player.setJerseyNo(playerDTO.getJerseyNo());
            }
            if(playerDTO.getPlayStyle() != null){
                player.setPlayStyle(playerDTO.getPlayStyle());
            }
            if(playerDTO.getPosition() != null){
                player.setPosition(playerDTO.getPosition());
            }
            playerRepository.save(player);
            result.set(true);
        }, ()->{
            result.set(false);
        });
        return result.get();
    }
}
