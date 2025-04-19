package com.locfox.qr_book.redirect_service.exceptions;

/// Any error of connection to the gRPC server
public class MicroserviceStatusException extends RuntimeException {
    public MicroserviceStatusException(String message) {
        super(message);
    }
}
