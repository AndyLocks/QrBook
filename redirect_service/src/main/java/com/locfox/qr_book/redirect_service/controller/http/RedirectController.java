package com.locfox.qr_book.redirect_service.controller.http;

import com.locfox.qr_book.redirect_service.entity.kafka.QrCode;
import com.locfox.qr_book.redirect_service.exceptions.CodesHandlerMicroserviceStatusException;
import com.locfox.qr_book.redirect_service.repository.QrCodeCacheableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class RedirectController {

    private QrCodeCacheableRepository repository;
    private final Logger LOG = LoggerFactory.getLogger(RedirectController.class);

    @Autowired
    public RedirectController(QrCodeCacheableRepository repository) {
        this.repository = repository;
    }

    @GetMapping("{codeId}")
    public ResponseEntity<Void> redirect(@PathVariable String codeId) {
        LOG.debug("New redirect request");
        QrCode qrCode = null;
        try {
            qrCode = repository.find(codeId);
        } catch (CodesHandlerMicroserviceStatusException e) {
            var problems = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            problems.setTitle("Error");
            problems.setDetail(e.getMessage());

            return ResponseEntity.of(problems).build();
        }
        LOG.debug("QrCode: [{}]", qrCode);

        if (qrCode == null || qrCode.url() == null || qrCode.url().isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(qrCode.url()))
                .build();
    }

}
