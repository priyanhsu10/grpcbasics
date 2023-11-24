package com.server;

import com.pro.grpc.models.Account;
import com.pro.grpc.models.TransferRequest;
import com.pro.grpc.models.TransferResponse;
import com.pro.grpc.models.TransferStatus;
import io.grpc.stub.StreamObserver;

public class TransferRequestObserver implements StreamObserver<TransferRequest> {
    private final AccountRepository accountRepository;
    private final StreamObserver<TransferResponse> transferResponseStreamObserver;

    public TransferRequestObserver(AccountRepository accountRepository, StreamObserver<TransferResponse> transferResponseStreamObserver) {
        this.accountRepository = accountRepository;
        this.transferResponseStreamObserver = transferResponseStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {

        int balance = accountRepository.getBalance(transferRequest.getFromAccount());
        TransferResponse transferResponse = null;
        boolean canTransfer = balance >= transferRequest.getAmount() && transferRequest.getFromAccount() != transferRequest.getToAccount();
        System.out.println("transfer Reuqeust : frm account:" + transferRequest.getFromAccount() + "  to account: " + transferRequest.getToAccount() + "amount :" + transferRequest.getAmount());
        if (canTransfer) {
            accountRepository.debit(transferRequest.getFromAccount(), transferRequest.getAmount());
            accountRepository.credit(transferRequest.getToAccount(), transferRequest.getAmount());

            transferResponse = TransferResponse.newBuilder()
                    .setStatus(TransferStatus.Success)
                    .setAmount(transferRequest.getAmount())
                    .setFromAccount(getAccount(transferRequest.getFromAccount()))
                    .setToAccount(getAccount(transferRequest.getToAccount()))
                    .build();
        } else {
            transferResponse = TransferResponse.newBuilder()
                    .setStatus(TransferStatus.Failed)
                    .setAmount(transferRequest.getAmount())
                    .setFromAccount(getAccount(transferRequest.getFromAccount()))
                    .setToAccount(getAccount(transferRequest.getToAccount()))
                    .build();

        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        transferResponseStreamObserver.onNext(transferResponse);

    }

    private Account getAccount(int accountNumber) {
        return Account.newBuilder()
                .setAccountNumber(accountNumber)
                .setAmount(accountRepository.getBalance(accountNumber))
                .build();
    }

    @Override
    public void onError(Throwable throwable) {
        transferResponseStreamObserver.onError(throwable);
        transferResponseStreamObserver.onCompleted();
        ;
    }

    @Override
    public void onCompleted() {
        transferResponseStreamObserver.onCompleted();
    }
}
