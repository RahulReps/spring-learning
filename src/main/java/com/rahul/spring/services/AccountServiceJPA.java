package com.rahul.spring.services;

import com.rahul.spring.mappers.AccountMapper;
import com.rahul.spring.model.AccountDTO;
import com.rahul.spring.repositories.AccountRepository;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class AccountServiceJPA implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    @Override
    public Optional<AccountDTO> getAccountById(UUID uuid) {
        return Optional.ofNullable(accountMapper
                .accountToAccountDto(accountRepository.findById(uuid).orElse(null)));
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::accountToAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO saveNewAccount(AccountDTO account) {
        return accountMapper.accountToAccountDto(accountRepository
                .save(accountMapper.accountDtoToAccount(account)));
    }

    @Override
    public Optional<AccountDTO> updateAccountById(UUID accountId, AccountDTO account) {
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

    @Override
    public Boolean deleteAccountById(UUID accountId) {
        if(accountRepository.existsById(accountId)){
            accountRepository.deleteById(accountId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<AccountDTO> patchAccountById(UUID accountId, AccountDTO account) {
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
