package com.locfox.qr_book.codes_handler.controller;

import com.locfox.qr_book.QRCodesHandlerGrpc;
import com.locfox.qr_book.Services;
import com.locfox.qr_book.codes_handler.entity.QrCode;
import com.locfox.qr_book.codes_handler.repository.QrCodeRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class QrCodeHandlerGrpcController extends QRCodesHandlerGrpc.QRCodesHandlerImplBase {

    private QrCodeRepository repository;

    @Autowired
    public QrCodeHandlerGrpcController(QrCodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void findAllByUserId(Services.AccountId request, StreamObserver<Services.QrCodeList> responseObserver) {
        var allQrCodes = repository.findAll(request.getId()).stream()
                .map(qrCode -> Services.QrCode.newBuilder()
                        .setUuid(qrCode.getId().toString())
                        .setOwnerId(qrCode.getOwnerId())
                        .setUrl(qrCode.getUrl())
                        .build())
                .toList();

        responseObserver.onNext(Services.QrCodeList.newBuilder()
                .addAllCodes(allQrCodes)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getQrCode(Services.QrCodeUUID request, StreamObserver<Services.QrCode> responseObserver) {
        Optional<QrCode> qrCode = null;
        try {
            qrCode = repository.find(UUID.fromString(request.getUuid()));
        } catch (IllegalArgumentException e) {
            responseObserver.onError(new RuntimeException("UUID format exception", e));
            return;
        }

        qrCode.ifPresentOrElse(q -> responseObserver.onNext(Services.QrCode.newBuilder()
                        .setUuid(q.getId().toString())
                        .setOwnerId(q.getOwnerId())
                        .setUrl(q.getUrl())
                        .build()),
                () -> responseObserver.onNext(null));

        responseObserver.onCompleted();
    }

}
