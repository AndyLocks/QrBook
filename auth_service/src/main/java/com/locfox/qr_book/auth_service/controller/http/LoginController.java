package com.locfox.qr_book.auth_service.controller.http;

import com.locfox.qr_book.AccountsServiceGrpc;
import com.locfox.qr_book.Services;
import com.locfox.qr_book.auth_service.entity.dto.LoginDTO;
import com.locfox.qr_book.auth_service.service.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private PasswordEncoder passwordEncoder;
    private AccountsServiceGrpc.AccountsServiceBlockingStub accountsService;
    private JwtUtils jwtUtils;
    private Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(PasswordEncoder passwordEncoder, AccountsServiceGrpc.AccountsServiceBlockingStub accountsService, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.accountsService = accountsService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        LOG.debug("Login: [{}]", loginDTO);
        var account = accountsService.getAccountByNickname(Services.AccountNickname.newBuilder().setNickname(loginDTO.nickname()).build());

        LOG.debug("Checking password...");

        if (!passwordEncoder.matches(loginDTO.password(), account.getPasswordHash()))
            return ResponseEntity.notFound().build();

        LOG.debug("Password is correct");

        return ResponseEntity.ok(jwtUtils.generateJwtToken(account.getId()));
    }

}
