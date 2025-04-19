package com.locfox.qr_book.redirect_service.config.grpc.client;

import com.locfox.qr_book.QRCodesHandlerGrpc;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodesHandlerGrpcClientConfiguration {

    @Value("${grpc.codes_handler.host}")
    private String host;

    @Value("${grpc.codes_handler.port}")
    private int port;

    private final Logger LOG = LoggerFactory.getLogger(CodesHandlerGrpcClientConfiguration.class);

    @Bean
    public QRCodesHandlerGrpc.QRCodesHandlerBlockingStub accountsServiceBlockingStub() {
        LOG.info("Creating [QRCodesHandlerGrpc] gRPC channel...");
        LOG.info("[QRCodesHandlerGrpc] gRPC channel: host[{}], port [{}]", host, port);

        var channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        var stub = QRCodesHandlerGrpc.newBlockingStub(channel);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Shutting down [QRCodesHandlerGrpc] gRPC channel...");
            channel.shutdown();
            LOG.info("[QRCodesHandlerGrpc] gRPC channel shutdown completed.");
        }));

        return stub;
    }

}
