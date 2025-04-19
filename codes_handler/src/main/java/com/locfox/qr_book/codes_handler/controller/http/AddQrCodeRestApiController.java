package com.locfox.qr_book.codes_handler.controller.http;

import com.locfox.qr_book.codes_handler.entity.QrCode;
import com.locfox.qr_book.codes_handler.entity.dto.AddQrCodeDTO;
import com.locfox.qr_book.codes_handler.entity.dto.UserIdDTO;
import com.locfox.qr_book.codes_handler.repository.QrCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddQrCodeRestApiController {

    private QrCodeRepository repository;

    @Autowired
    public AddQrCodeRestApiController(QrCodeRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<QrCode> addQrCode(@AuthenticationPrincipal UserIdDTO userId, @RequestBody AddQrCodeDTO addQrCodeDTO) {
        var savedQrCode = repository.add(QrCode.builder()
                .url(addQrCodeDTO.url())
                .ownerId(Long.valueOf(userId.userId()))
                .build());

        return ResponseEntity.ok(savedQrCode);
    }

}
