package com.rahul.spring.repositories;

import com.rahul.spring.entities.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
public class AccountRepositoryTest {
    @Autowired
    AccountRepository accountRepository;

    @Test
    void testSaveAccount(){
        Account account = accountRepository.save(Account.builder().name("Account 4").email("maiil4@gmail.com").build());
        assertThat(account.getId()).isNotNull();
    }
}
