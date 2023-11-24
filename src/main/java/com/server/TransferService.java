package com.server;

import com.pro.grpc.models.TransferRequest;
import com.pro.grpc.models.TransferResponse;
import com.pro.grpc.models.TransferServiceGrpc;
import com.pro.grpc.models.TransferServiceGrpc.TransferServiceImplBase;
import io.grpc.stub.StreamObserver;

public class TransferService  extends TransferServiceImplBase{
    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {

        return new TransferRequestObserver(new AccountRepository(),responseObserver);
    }
}
