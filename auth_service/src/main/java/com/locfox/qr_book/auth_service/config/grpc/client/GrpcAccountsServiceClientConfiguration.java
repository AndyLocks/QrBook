package com.locfox.qr_book.auth_service.config.grpc.client;

import com.locfox.qr_book.AccountsServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcAccountsServiceClientConfiguration {

    @Value("${grpc.accounts_service.host}")
    private String host;

    @Value("${grpc.accounts_service.port}")
    private int port;

    private final Logger LOG = LoggerFactory.getLogger(GrpcAccountsServiceClientConfiguration.class);

    @Bean
    public AccountsServiceGrpc.AccountsServiceBlockingStub accountsServiceBlockingStub() {
        LOG.info("Creating [AccountsServiceGrpc] gRPC channel...");
        LOG.info("[AccountsServiceGrpc] gRPC channel: host[{}], port [{}]", host, port);

        var channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        var stub = AccountsServiceGrpc.newBlockingStub(channel);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Shutting down [AccountsServiceGrpc] gRPC channel...");
            channel.shutdown();
            LOG.info("[AccountsServiceGrpc] gRPC channel shutdown completed.");
        }));

        return stub;
    }

}
