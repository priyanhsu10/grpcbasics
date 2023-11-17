package com.server;

import com.pro.grpc.models.Balance;
import com.pro.grpc.models.BalanceCheckRequest;
import com.pro.grpc.models.BankServiceGrpc;
import com.pro.grpc.models.BankServiceOuterClass;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {

        int accoutNumber= request.getAccountNumber();
        Balance balance=    Balance.newBuilder().setAmount(100*accoutNumber).build();
        responseObserver.onNext(balance);
        System.out.println("called for account "+ request.getAccountNumber());
        responseObserver.onCompleted();

    }
}
