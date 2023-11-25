package com.server;

import com.server.snaksladder.SnakeLadderService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(5656)
                .addService(new BankService())
                .addService(new TransferService())
                .addService(new SnakeLadderService())
                .build();
        server.start();
        System.out.println("server started at port 5656...");
    server.awaitTermination();
    }
}
