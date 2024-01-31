package com.rahul.spring.controllers;

import com.rahul.spring.entities.Account;
import com.rahul.spring.mappers.AccountMapper;
import com.rahul.spring.model.AccountDTO;
import com.rahul.spring.repositories.AccountRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AccountControllerIT {
    @Autowired
    AccountController accountController;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountMapper accountMapper;

    @Rollback
    @org.springframework.transaction.annotation.Transactional
    @Test
    void deleteByIdFound() {
        Account customer = accountRepository.findAll().get(0);

        ResponseEntity responseEntity = accountController.deleteCustomerById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(accountRepository.findById(customer.getId()).isEmpty());
    }


    @Test
    void testDeleteNotFound() {
        assertThrows(NotFoundException.class, () -> {
            accountController.deleteCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            accountController.updateCustomerByID(UUID.randomUUID(), AccountDTO.builder().build());
        });
    }

    @Rollback
    @org.springframework.transaction.annotation.Transactional
    @Test
    void testUpdateExistingAccount() {
        Account customer = accountRepository.findAll().get(0);
        AccountDTO customerDTO = accountMapper.accountToAccountDto(customer);
        customerDTO.setId(null);
        final String customerName = "UPDATED";
        customerDTO.setName(customerName);

        ResponseEntity responseEntity = accountController.updateCustomerByID(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Account updatedCustomer = accountRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewAccount() {
        AccountDTO customerDTO = AccountDTO.builder()
                .name("TEST")
                .build();

        ResponseEntity responseEntity = accountController.handlePost(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Account account = accountRepository.findById(savedUUID).get();
        assertThat(account).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList(){
        accountRepository.deleteAll();
        List<AccountDTO> list = accountController.listAllCustomers();
        assertThat(list.size()).isEqualTo(0);
    }
    @Test
    void testGetAll(){
        List<AccountDTO> list = accountController.listAllCustomers();
        assertThat(list.size()).isEqualTo(3);
    }

    @Test
    void testInvalidAccountID(){
        assertThrows(NotFoundException.class, ()->{
            accountController.getCustomerById(UUID.randomUUID());
        });
    }
    @Test
    void testGetById(){
        Account account = accountRepository.findAll().get(0);
        AccountDTO accountDTO = accountController.getCustomerById(account.getId());
        assertThat(accountDTO).isNotNull();
        assertThat(accountDTO.getId()).isEqualTo(account.getId());
    }
}
