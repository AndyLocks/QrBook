package com.locfox.qr_book.codes_handler.config.grpc.client;

import com.locfox.qr_book.AccountsServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountsServiceGrpcClientConfiguration {

    @Value("${grpc.account_service.host}")
    private String host;

    @Value("${grpc.account_service.port}")
    private int port;

    private final Logger LOG = LoggerFactory.getLogger(AccountsServiceGrpcClientConfiguration.class);

    @Bean
    public AccountsServiceGrpc.AccountsServiceBlockingStub accountsServiceBlockingStub() {
        LOG.info("Creating [AccountsServiceGrpc] gRPC channel...");
        LOG.info("[AuthServiceGrpc] gRPC channel: host[{}], port [{}]", host, port);

        var channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        var stub = AccountsServiceGrpc.newBlockingStub(channel);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Shutting down [AuthServiceGrpc] gRPC channel...");
            channel.shutdown();
            LOG.info("[AuthServiceGrpc] gRPC channel shutdown completed.");
        }));

        return stub;
    }

}
