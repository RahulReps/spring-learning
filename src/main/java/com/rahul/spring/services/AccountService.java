package com.rahul.spring.services;

import com.rahul.spring.model.AccountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    Optional<AccountDTO> getAccountById(UUID uuid);

    List<AccountDTO> getAllAccounts();

    AccountDTO saveNewAccount(AccountDTO Account);

    Optional<AccountDTO> updateAccountById(UUID AccountId, AccountDTO Account);

    Boolean deleteAccountById(UUID AccountId);

    Optional<AccountDTO> patchAccountById(UUID AccountId, AccountDTO Account);
}
