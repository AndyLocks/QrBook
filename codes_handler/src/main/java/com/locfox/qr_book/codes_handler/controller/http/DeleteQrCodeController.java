package com.locfox.qr_book.codes_handler.controller.http;

import com.locfox.qr_book.codes_handler.entity.dto.UserIdDTO;
import com.locfox.qr_book.codes_handler.repository.QrCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DeleteQrCodeController {

    private QrCodeRepository repository;

    @Autowired
    public DeleteQrCodeController(QrCodeRepository repository) {
        this.repository = repository;
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteQrCode(@PathVariable String uuid, @AuthenticationPrincipal UserIdDTO userId) {
        UUID key;
        try {
            key = UUID.fromString(uuid);
        } catch (IllegalArgumentException _) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid UUID format"))
                    .build();
        }

        var qrCode = repository.find(key).orElse(null);

        if (qrCode == null) return ResponseEntity.notFound().build();
        if (!qrCode.getOwnerId().toString().equals(userId.userId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        repository.delete(key);

        return ResponseEntity.ok(null);
    }

}
