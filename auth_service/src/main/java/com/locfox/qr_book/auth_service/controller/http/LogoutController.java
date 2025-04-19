package com.locfox.qr_book.auth_service.controller.http;

import com.locfox.qr_book.auth_service.service.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    private final Logger LOG = LoggerFactory.getLogger(LogoutController.class);
    private JwtUtils jwtUtils;

    @Autowired
    public LogoutController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        LOG.debug("Logout request: {}", request);

        if (request.getHeader("Authorization") == null) {
            LOG.info("Header Authorization is null: {}", request);

            var problems = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
            problems.setTitle("No authorization header");
            problems.setDetail("Add Bearer token to the header");

            return ResponseEntity.of(problems).build();
        }

        var token = request.getHeader("Authorization").split(" ")[1];

        if (!jwtUtils.verifyToken(token)) {
            LOG.debug("Token is invalid: {}", request);

            var problems = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
            problems.setTitle("Token is not valid");
            problems.setDetail("Login to get a token");

            return ResponseEntity.of(problems).build();
        }

        jwtUtils.addToBlackList(token);

        LOG.debug("New token added to token black list: {}", token);
        return ResponseEntity.ok().build();
    }

}
