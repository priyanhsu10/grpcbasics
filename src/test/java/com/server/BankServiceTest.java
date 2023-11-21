package com.server;

import com.pro.grpc.models.Balance;
import com.pro.grpc.models.BalanceCheckRequest;
import com.pro.grpc.models.BankServiceGrpc;
import com.pro.grpc.models.CreditDebitRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankServiceTest {
    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 5656)
                .usePlaintext()
                .build();
        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void checkBalance() {

        Balance balance = this.blockingStub.getBalance(BalanceCheckRequest.newBuilder().setAccountNumber(4).build());

        assertEquals(balance.getAmount(), 400);

    }

    @Test
    public void creditBalance() {
        var request = CreditDebitRequest.newBuilder().setAccountNumber(4).setAmount(40).build();
        Balance balance = this.blockingStub.credit(request);
        System.out.println(balance);
        assertEquals(balance.getAmount(), 80);

    }

    @Test
    public void getData() {
        var request = BalanceCheckRequest.newBuilder().setAccountNumber(4).build();
        this.blockingStub.getData(request).forEachRemaining(x -> System.out.println(x.getAmount()));


    }

    @Test
    public void debitBalance() {
        var request = CreditDebitRequest.newBuilder().setAccountNumber(6).setAmount(40).build();
        Balance balance = this.blockingStub.debit(request);
        System.out.println(balance);
        assertEquals(balance.getAmount(), 20);

    }

    @Test
    public void checkBalanceAsync() throws ExecutionException, InterruptedException {
        var balenceRequest = BalanceCheckRequest.newBuilder().setAccountNumber(15).build();
        CountDownLatch latch = new CountDownLatch(1);
        this.bankServiceStub.getData(balenceRequest, new BalanceResponseObserver(latch));


        latch.await();

    }
    @Test
    public void creditBalanceStream() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        var request = CreditDebitRequest.newBuilder().setAccountNumber(4).setAmount(40).build();
        BalanceResponseObserver balanceResponseObserver = new BalanceResponseObserver(latch);
        StreamObserver<CreditDebitRequest> deposit = this.bankServiceStub.deposit(balanceResponseObserver);

        for(int i = 1;i<=10;i++){
            deposit.onNext(CreditDebitRequest.newBuilder().setAccountNumber(4).setAmount(getRandomNumberUsingInts(10,100)).build());
            Thread.sleep(500);
        }

        deposit.onCompleted();
        latch.await();

    }
    public int getRandomNumberUsingInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }
}

class BalanceResponseObserver implements StreamObserver<Balance> {
    private final CountDownLatch latch;

    public BalanceResponseObserver(CountDownLatch latch) {

        this.latch = latch;
    }

    @Override
    public void onNext(Balance balance) {
        System.out.println(balance.getAmount());

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
class RequestObserver implements StreamObserver<CreditDebitRequest> {
    private final CountDownLatch latch;

    public RequestObserver(CountDownLatch latch) {

        this.latch = latch;
    }

    @Override
    public void onNext(CreditDebitRequest balance) {
        System.out.println(balance.getAmount());

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