package com.rahul.spring.controllers;

import com.rahul.spring.model.AccountDTO;
import com.rahul.spring.repositories.AccountRepository;
import com.rahul.spring.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AccountController {
    public static final String ACCOUNT_PATH = "/api/v1/account";
    public static final String ACCOUNT_PATH_ID = ACCOUNT_PATH + "/{accountId}";

    private final AccountService accountService;

    @PatchMapping(ACCOUNT_PATH_ID)
    public ResponseEntity patchCustomerById(@PathVariable("accountId") UUID accountId,
                                            @RequestBody AccountDTO account){

        accountService.patchAccountById(accountId, account);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(ACCOUNT_PATH_ID)
    public ResponseEntity deleteCustomerById(@PathVariable("accountId") UUID accountId){

        if (!accountService.deleteAccountById(accountId)){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(ACCOUNT_PATH_ID)
    public ResponseEntity updateCustomerByID(@PathVariable("accountId") UUID accountId,
                                             @RequestBody AccountDTO account){

        if (accountService.updateAccountById(accountId, account).isEmpty()){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(ACCOUNT_PATH)
    public ResponseEntity handlePost(@RequestBody AccountDTO account){
        AccountDTO savedCustomer = accountService.saveNewAccount(account);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", ACCOUNT_PATH + "/" + savedCustomer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(ACCOUNT_PATH)
    public List<AccountDTO> listAllCustomers(){
        return accountService.getAllAccounts();
    }

    @GetMapping(value = ACCOUNT_PATH_ID)
    public AccountDTO getCustomerById(@PathVariable("accountId") UUID id){
        return accountService.getAccountById(id).orElseThrow(NotFoundException::new);
    }
}
