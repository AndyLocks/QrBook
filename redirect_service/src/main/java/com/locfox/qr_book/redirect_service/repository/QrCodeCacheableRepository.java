package com.locfox.qr_book.redirect_service.repository;

import com.locfox.qr_book.QRCodesHandlerGrpc;
import com.locfox.qr_book.Services;
import com.locfox.qr_book.redirect_service.entity.kafka.QrCode;
import com.locfox.qr_book.redirect_service.exceptions.CodesHandlerMicroserviceStatusException;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/// Позволяет получить информацию о qr кодах
@Repository
public class QrCodeCacheableRepository {

    private QRCodesHandlerGrpc.QRCodesHandlerBlockingStub repository;

    @Autowired
    public QrCodeCacheableRepository(QRCodesHandlerGrpc.QRCodesHandlerBlockingStub repository) {
        this.repository = repository;
    }

    /// Accesses the `codes_handler` microservice and requests the qr code by uuid
    ///
    /// @param id qr code uuid
    /// @throws CodesHandlerMicroserviceStatusException by any gRPC error
    /// @return can be null
    @Cacheable(value = "codes", key = "#id")
    public QrCode find(String id) {
        Services.QrCode qrCode = null;
        try {
            qrCode = repository.getQrCode(Services.QrCodeUUID.newBuilder().setUuid(id).build());
        } catch (StatusRuntimeException e) {
            throw new CodesHandlerMicroserviceStatusException(e.getMessage());
        }

        if (qrCode == null) return null;

        return new QrCode(qrCode.getUuid(), qrCode.getOwnerId(), qrCode.getUrl());
    }

}
