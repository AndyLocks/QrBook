package com.locfox.qr_book.account_service.controller.grpc;

import com.locfox.qr_book.AccountsServiceGrpc;
import com.locfox.qr_book.Services;
import com.locfox.qr_book.account_service.repository.AccountCacheableRepository;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountGrpcController extends AccountsServiceGrpc.AccountsServiceImplBase {

    private AccountCacheableRepository accountCacheableRepository;
    private final Logger LOG = LoggerFactory.getLogger(AccountGrpcController.class);

    @Autowired
    public AccountGrpcController(AccountCacheableRepository accountCacheableRepository) {
        this.accountCacheableRepository = accountCacheableRepository;
    }

    @Override
    public void getAccountByNickname(Services.AccountNickname request, StreamObserver<Services.Account> responseObserver) {
        LOG.debug("New gRPC request: GetAccount: [{}]", request);

        try {
            var account = accountCacheableRepository.find(request.getNickname());
            account.ifPresentOrElse(a -> responseObserver.onNext(Services.Account.newBuilder()
                            .setId(a.getId())
                            .setNickname(a.getNickname())
                            .setPasswordHash(a.getPasswordHash())
                            .build()),
                    () -> responseObserver.onNext(null));
        } catch (Exception e) {
            responseObserver.onError(e);
            return;
        }

        responseObserver.onCompleted();
    }

    @Override
    public void getAccount(Services.AccountId request, StreamObserver<Services.Account> responseObserver) {
        LOG.debug("New gRPC request: GetAccount: [{}]", request);

        try {
            var account = accountCacheableRepository.find(request.getId());
            account.ifPresentOrElse(a -> responseObserver.onNext(Services.Account.newBuilder()
                            .setId(a.getId())
                            .setNickname(a.getNickname())
                            .setPasswordHash(a.getPasswordHash())
                            .build()),
                    () -> responseObserver.onNext(null));
        } catch (Exception e) {
            responseObserver.onError(e);
            return;
        }

        responseObserver.onCompleted();
    }

}
