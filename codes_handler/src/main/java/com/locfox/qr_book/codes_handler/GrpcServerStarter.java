package com.locfox.qr_book.codes_handler;

import io.grpc.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
