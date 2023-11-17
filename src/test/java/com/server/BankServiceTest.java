package com.server;

import com.google.common.util.concurrent.ListenableFuture;
import com.pro.grpc.models.Balance;
import com.pro.grpc.models.BalanceCheckRequest;
import com.pro.grpc.models.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankServiceTest {
    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceFutureStub asycblockingStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 5656)
                .usePlaintext()
                .build();
        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.asycblockingStub = BankServiceGrpc.newFutureStub(managedChannel);
    }

    @Test
    public void checkBalance() {

        Balance balance = this.blockingStub.getBalance(BalanceCheckRequest.newBuilder().setAccountNumber(4).build());

        assertEquals(balance.getAmount(), 400);

    }
    @Test
    public void checkBalanceAsync() throws ExecutionException, InterruptedException {
var balenceRequest= BalanceCheckRequest.newBuilder().setAccountNumber(15).build()  ;

        Balance balance = this.asycblockingStub.getBalance(balenceRequest)
                .get();


        assertEquals(balance.getAmount(), 1500);

    }

}