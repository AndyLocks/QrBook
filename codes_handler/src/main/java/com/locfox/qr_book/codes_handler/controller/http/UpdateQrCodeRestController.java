package com.locfox.qr_book.codes_handler.controller.http;

import com.locfox.qr_book.codes_handler.entity.dto.UpdateQrCodeDTO;
import com.locfox.qr_book.codes_handler.entity.dto.UserIdDTO;
import com.locfox.qr_book.codes_handler.repository.QrCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UpdateQrCodeRestController {

    private QrCodeRepository repository;

    @Autowired
    public UpdateQrCodeRestController(QrCodeRepository repository) {
        this.repository = repository;
    }

    @PutMapping("{qrCodeId}")
    public ResponseEntity<Void> updateQrCode(@AuthenticationPrincipal UserIdDTO userId, @RequestBody UpdateQrCodeDTO updateQrCodeDTO, @PathVariable String qrCodeId) {
        UUID qrCodeUuid = null;
        try {
            qrCodeUuid = UUID.fromString(qrCodeId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        var qrCode = repository.find(qrCodeUuid).orElse(null);

        if (qrCode == null) return ResponseEntity.notFound().build();
        if (!qrCode.getOwnerId().toString().equals(userId.userId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        repository.update(qrCode.getId(), updateQrCodeDTO.url());

        return ResponseEntity.ok(null);
    }

}
