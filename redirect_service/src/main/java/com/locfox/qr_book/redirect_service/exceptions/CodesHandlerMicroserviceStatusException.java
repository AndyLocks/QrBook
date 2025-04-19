package com.locfox.qr_book.redirect_service.exceptions;

/// Any error of connection to the `codes_handler` gRPC server
public class CodesHandlerMicroserviceStatusException extends MicroserviceStatusException {
    public CodesHandlerMicroserviceStatusException(String message) {
        super(message);
    }
}
