package com.locfox.qr_book.account_service.controller.rest;

import com.locfox.qr_book.QRCodesHandlerGrpc;
import com.locfox.qr_book.Services;
import com.locfox.qr_book.account_service.entity.Account;
import com.locfox.qr_book.account_service.entity.dto.AccountUpdateDTO;
import com.locfox.qr_book.account_service.entity.dto.AccountWithQrCodesDTO;
import com.locfox.qr_book.account_service.entity.dto.QrCodeDTO;
import com.locfox.qr_book.account_service.repository.AccountCacheableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
public class AccountController {

    private AccountCacheableRepository repository;
    private final Logger LOG = LoggerFactory.getLogger(AccountController.class);
    private PasswordEncoder passwordEncoder;
    private QRCodesHandlerGrpc.QRCodesHandlerBlockingStub qrCodesHandlerBlockingStub;

    @Autowired
    public AccountController(AccountCacheableRepository repository, PasswordEncoder passwordEncoder, QRCodesHandlerGrpc.QRCodesHandlerBlockingStub qrCodesHandlerBlockingStub) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.qrCodesHandlerBlockingStub = qrCodesHandlerBlockingStub;
    }

    @GetMapping
    public ResponseEntity<AccountWithQrCodesDTO> getAccount(@AuthenticationPrincipal Account account) {
        LOG.debug("GET getAccount request: returning: [{}]", account);
        var codes = qrCodesHandlerBlockingStub.findAllByUserId(Services.AccountId.newBuilder()
                .setId(account.getId())
                .build());

        return ResponseEntity.ok(new AccountWithQrCodesDTO(account.getNickname(), codes.getCodesList().stream()
                .map(QrCodeDTO::from)
                .toList()));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal Account account) {
        LOG.debug("DELETE deleteAccount request: deleting: [{}]", account);

        repository.deleteAccount(account.getId());

        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<Void> updateAccount(@AuthenticationPrincipal Account account, @RequestBody AccountUpdateDTO dto) {
        repository.updateData(account.getId(), dto.nickname(), passwordEncoder.encode(dto.password()));

        return ResponseEntity.ok(null);
    }

}
