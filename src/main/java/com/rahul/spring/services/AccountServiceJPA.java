package com.rahul.spring.services;

import com.rahul.spring.mappers.AccountMapper;
import com.rahul.spring.model.AccountDTO;
import com.rahul.spring.repositories.AccountRepository;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class AccountServiceJPA implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CacheManager cacheManager;

    @Cacheable(cacheNames = "accountCache")
    @Override
    public Optional<AccountDTO> getAccountById(UUID uuid) {
        return Optional.ofNullable(accountMapper
                .accountToAccountDto(accountRepository.findById(uuid).orElse(null)));
    }

    @Cacheable(cacheNames = "accountListCache")
    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::accountToAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO saveNewAccount(AccountDTO account) {
        if(cacheManager.getCache("accountListCache") != null){
            cacheManager.getCache("accountListCache").clear();
        }
        return accountMapper.accountToAccountDto(accountRepository
                .save(accountMapper.accountDtoToAccount(account)));
    }

    @Override
    public Optional<AccountDTO> updateAccountById(UUID accountId, AccountDTO account) {
        clearCache(accountId);

        AtomicReference<Optional<AccountDTO>> atomicReference = new AtomicReference<>();

        accountRepository.findById(accountId).ifPresentOrElse(foundCustomer -> {
            foundCustomer.setName(account.getName());
            atomicReference.set(Optional.of(accountMapper
                    .accountToAccountDto(accountRepository.save(foundCustomer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    private void clearCache(UUID accountId) {
        if(cacheManager.getCache("accountListCache") != null){
            cacheManager.getCache("accountListCache").clear();
        }

        if(cacheManager.getCache("accountCache") != null){
            cacheManager.getCache("accountCache").evict(accountId);
        }
    }

    @Override
    public Boolean deleteAccountById(UUID accountId) {
        clearCache(accountId);
        if(accountRepository.existsById(accountId)){
            accountRepository.deleteById(accountId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<AccountDTO> patchAccountById(UUID accountId, AccountDTO account) {
        clearCache(accountId);

        AtomicReference<Optional<AccountDTO>> atomicReference = new AtomicReference<>();

        accountRepository.findById(accountId).ifPresentOrElse(foundCustomer -> {
            if (StringUtils.hasText(account.getName())){
                foundCustomer.setName(account.getName());
            }
            atomicReference.set(Optional.of(accountMapper
                    .accountToAccountDto(accountRepository.save(foundCustomer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
