package com.server;

import com.pro.grpc.models.TransferRequest;
import com.pro.grpc.models.TransferResponse;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class TransferResposeObserver implements StreamObserver<TransferResponse> {
    private final CountDownLatch latch;

    public TransferResposeObserver(CountDownLatch latch) {

        this.latch = latch;
    }

    @Override
    public void onNext(TransferResponse transferResponse) {
        System.out.println("trnansfer amount :"+transferResponse.getAmount()+" form "+transferResponse.getFromAccount().getAccountNumber()+ " to " +transferResponse.getToAccount().getAccountNumber()+" is " +transferResponse.getStatus().toString());
        System.out.println("  account  || balance ");
        System.out.println(+transferResponse.getFromAccount().getAccountNumber()+"      || " +transferResponse.getFromAccount().getAmount());
        System.out.println(+transferResponse.getToAccount().getAccountNumber()+"      || " +transferResponse.getToAccount().getAmount());

        System.out.println("-----------------------------");
    }

    @Override
    public void onError(Throwable throwable) {
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        latch.countDown();

    }
}
