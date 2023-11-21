package com.server;

import com.pro.grpc.models.Balance;
import com.pro.grpc.models.CreditDebitRequest;
import io.grpc.stub.StreamObserver;

public class RequestStreamObserver implements StreamObserver<CreditDebitRequest> {
    private final StreamObserver<Balance> balanceStreamObserver;
    private final AccountRepository repository;
    private int accountNumber;


    public RequestStreamObserver(StreamObserver<Balance> balanceStreamObserver, AccountRepository repository) {
        this.balanceStreamObserver = balanceStreamObserver;
        this.repository = repository;
    }

    @Override
    public void onNext(CreditDebitRequest creditDebitRequest) {
        System.out.println("money coming for account "+creditDebitRequest.getAccountNumber()+ " amount:"+creditDebitRequest.getAmount());
        this.accountNumber = creditDebitRequest.getAccountNumber();
        repository.credit(this.accountNumber, creditDebitRequest.getAmount());

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        balanceStreamObserver.onNext(Balance.newBuilder().setAmount(repository.getBalance(this.accountNumber)).build());
        balanceStreamObserver.onCompleted();
    }
}
