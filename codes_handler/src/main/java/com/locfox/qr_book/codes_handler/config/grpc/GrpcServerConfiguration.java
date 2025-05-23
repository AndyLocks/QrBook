package com.locfox.qr_book.codes_handler.config.grpc;

import com.locfox.qr_book.codes_handler.controller.QrCodeHandlerGrpcController;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class GrpcServerConfiguration {

    private final Logger LOG = LoggerFactory.getLogger(GrpcServerConfiguration.class);

    @Value("${grpc.server.port}")
    private int port;

    @Bean
    public Server grpcServer(QrCodeHandlerGrpcController qrCodeHandlerGrpcController) throws IOException {
        var server = ServerBuilder.forPort(port)
                .addService(qrCodeHandlerGrpcController)
                .build();

        LOG.info("gRPC server was configurated with port: [{}]", port);
        LOG.debug("Account GRPC controller: [{}]", qrCodeHandlerGrpcController);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Shutting down gRPC server...");
            server.shutdown();
            LOG.info("gRPC server shutdown completed.");
        }));

        return server;
    }

}
