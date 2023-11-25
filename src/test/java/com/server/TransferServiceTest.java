package com.server;

import com.pro.grpc.models.TransferRequest;
import com.pro.grpc.models.TransferServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransferServiceTest {

private TransferServiceGrpc.TransferServiceStub transferServiceStub;
@BeforeAll
public   void init (){
    ManagedChannel channel= ManagedChannelBuilder.forAddress("localhost",5656)
            .usePlaintext()
            .build();
    transferServiceStub= TransferServiceGrpc.newStub(channel);

}
@Test
public  void transferTest() throws InterruptedException {
    CountDownLatch latch= new CountDownLatch(1);
    TransferResposeObserver transferResposeObserver = new TransferResposeObserver(latch);
    StreamObserver<TransferRequest> transfer = transferServiceStub.transfer(transferResposeObserver);
    Random random= new Random();
    for(int i=0;i<=10;i++){
        int fromAccount= random.ints(1,10).findFirst().getAsInt();
        int toAccount= random.ints(1,10).findFirst().getAsInt();
        int amount= random.ints(1,100).findFirst().getAsInt();
        TransferRequest request = TransferRequest.newBuilder().setFromAccount(fromAccount).setToAccount(toAccount).setAmount(amount).build();
        transfer.onNext(request);
    }
    transfer.onCompleted();
    latch.await();
}

}

