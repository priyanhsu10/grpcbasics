package com.server;

import com.pro.grpc.models.Balance;
import com.pro.grpc.models.BalanceCheckRequest;
import com.pro.grpc.models.BankServiceGrpc;
import com.pro.grpc.models.CreditDebitRequest;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {
    public static AccountRepository accountRepository = new AccountRepository();

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {

        Balance balance = Balance.newBuilder().setAmount(accountRepository.getBalance(request.getAccountNumber())).build();

        responseObserver.onNext(balance);
        System.out.println("called for account " + request.getAccountNumber());
        responseObserver.onCompleted();

    }

    @Override
    public void getData(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {

        int balance = accountRepository.getBalance(request.getAccountNumber());
        for (int i = 0; i <= 40; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            responseObserver.onNext(Balance.newBuilder().setAmount(i * balance).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void credit(CreditDebitRequest request, StreamObserver<Balance> responseObserver) {

        int amount = accountRepository.credit(request.getAccountNumber(), request.getAmount());

        responseObserver.onNext(Balance.newBuilder().setAmount(amount).build());
        responseObserver.onCompleted();
    }

    @Override
    public void debit(CreditDebitRequest request, StreamObserver<Balance> responseObserver) {
        int amount = accountRepository.debit(request.getAccountNumber(), request.getAmount());

        responseObserver.onNext(Balance.newBuilder().setAmount(amount).build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<CreditDebitRequest> deposit(StreamObserver<Balance> responseObserver) {
        return new RequestStreamObserver(responseObserver, accountRepository);
    }
}
