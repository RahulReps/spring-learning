package com.rahul.spring.mappers;

import com.rahul.spring.entities.Account;
import com.rahul.spring.model.AccountDTO;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {
    Account accountDtoToAccount(AccountDTO dto);

    AccountDTO accountToAccountDto(Account account);
}
