package com.locfox.qr_book.account_service.controller.rest;

import com.locfox.qr_book.account_service.entity.Account;
import com.locfox.qr_book.account_service.entity.dto.AccountInfoAfterRegisterDTO;
import com.locfox.qr_book.account_service.entity.dto.CreateAccountDTO;
import com.locfox.qr_book.account_service.repository.AccountCacheableRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private AccountCacheableRepository repository;
    private PasswordEncoder passwordEncoder;
    private final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(AccountCacheableRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<AccountInfoAfterRegisterDTO> register(@RequestBody CreateAccountDTO dto) {
        Account savedAccount = null;
        try {
            savedAccount = repository.createAccount(dto.nickname(), passwordEncoder.encode(dto.password()));
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "User with this nickname already exists"))
                    .build();
        }

        LOG.debug("New account was created: [{}]", savedAccount);

        return ResponseEntity.ok(AccountInfoAfterRegisterDTO.from(savedAccount));
    }

}
