package com.locfox.qr_book.account_service.config.grpc;

import com.locfox.qr_book.account_service.controller.grpc.AccountGrpcController;
import org.springframework.context.annotation.Configuration;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Configuration
public class GrpcServerConfiguration {

    private final Logger LOG = LoggerFactory.getLogger(GrpcServerConfiguration.class);

    @Value("${grpc.server.port}")
    private int port;

    @Bean
    public Server grpcServer(AccountGrpcController accountGrpcController) {
        var server = ServerBuilder.forPort(port)
                .addService(accountGrpcController)
                .build();

        LOG.info("gRPC server was configurated with port: [{}]", port);
        LOG.debug("Account GRPC controller: [{}]", accountGrpcController);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Shutting down gRPC server...");
            server.shutdown();
            LOG.info("gRPC server shutdown completed.");
        }));

        return server;
    }

}
