package com.locfox.qr_book.account_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import io.grpc.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/// Starts the gRPC server: [com.locfox.qr_book.account_service.config.grpc.GrpcServerConfiguration]
@Component
public class GrpcServerStarter implements CommandLineRunner {

    private final Server server;
    private final Logger LOG = LoggerFactory.getLogger(GrpcServerStarter.class);

    @Autowired
    public GrpcServerStarter(Server server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Starting grpc server...");
        server.start();
        LOG.info("grpc server was finally started!");
        server.awaitTermination();
    }

}
