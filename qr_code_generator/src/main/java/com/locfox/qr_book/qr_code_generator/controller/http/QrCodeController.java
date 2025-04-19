package com.locfox.qr_book.qr_code_generator.controller.http;

import com.google.zxing.WriterException;
import com.locfox.qr_book.qr_code_generator.repository.QrCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
public class QrCodeController {

    private QrCodeGenerator generator;
    private final Logger LOG = LoggerFactory.getLogger(QrCodeController.class);

    @Autowired
    public QrCodeController(QrCodeGenerator generator) {
        this.generator = generator;
    }

    @GetMapping("{uuid}/qr_code")
    public ResponseEntity<byte[]> getQrCode(@PathVariable String uuid) {
        LOG.debug("GetQrCode request");
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            LOG.debug("UUID format exception", e);

            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST, "UUID format error"
            )).build();
        }

        byte[] qrCodeBytes = null;
        try {
            qrCodeBytes = generator.generate(uuid);
        } catch (WriterException | IOException e) {
            LOG.error("Qr-Code convertation error", e);

            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Qr-Code convertation error"
            )).build();
        }

        if (qrCodeBytes.length == 0) return ResponseEntity.noContent().build();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrCodeBytes.length);

        return new ResponseEntity<>(qrCodeBytes, headers, 200);
    }

}
