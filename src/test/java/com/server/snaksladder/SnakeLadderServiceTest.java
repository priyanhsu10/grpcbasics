package com.server.snaksladder;

import com.pro.grpc.models.Role;
import com.pro.grpc.models.ServerResponse;
import com.pro.grpc.models.SnakeLadderServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SnakeLadderServiceTest {

    private SnakeLadderServiceGrpc.SnakeLadderServiceStub serviceStub;

    @BeforeAll
    public void init() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5656).usePlaintext().build();
        serviceStub = SnakeLadderServiceGrpc.newStub(channel);

    }

    @Test
    public void playGame() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ResponseObserver observer = new ResponseObserver(latch);
        StreamObserver<Role> playStreamObserver = serviceStub.play(observer);
        observer.setRoleStreamObserver(playStreamObserver);
        observer.roleDias();
        latch.await();
    }

}

class ResponseObserver implements StreamObserver<ServerResponse> {


    private final CountDownLatch latch;

    public ResponseObserver(CountDownLatch latch) {

        this.latch = latch;
    }

    public void setRoleStreamObserver(StreamObserver<Role> roleStreamObserver) {
        this.roleStreamObserver = roleStreamObserver;

    }


    private StreamObserver<Role> roleStreamObserver;

    public void roleDias() {
        int diasNumber = new Random().ints(1, 7).findFirst().getAsInt();
        Role role = Role.newBuilder().setDiasNumber(diasNumber).build();
        System.out.println("------------------client----------------------");
        System.out.println("client: Dias role to  ="+diasNumber);
        roleStreamObserver.onNext(role);

    }

    @Override
    public void onNext(ServerResponse serverResponse) {

        if (serverResponse.getClientMessage().equals("---- win -----")) {
            System.out.println(serverResponse.getClientMessage());
            roleStreamObserver.onCompleted();
            return;
        }
        System.out.println("client: " + serverResponse.getClientMessage());

        System.out.println("------------------server----------------------");
        System.out.println("server: Dias role to  =" + serverResponse.getServerDiasNumber());
        System.out.println("server: " + serverResponse.getServerMessage());
        if (serverResponse.getServerMessage().equals("---- win -----")) {
            roleStreamObserver.onCompleted();

            return;
        }
      /*  System.out.println("client ="+serverResponse.getCurrentPosition());
        System.out.println("server ="+serverResponse.getCurrentServerPosition());
*/
        roleDias();
//        System.out.println("-----------------");
    }

    @Override
    public void onError(Throwable throwable) {
        roleStreamObserver.onCompleted();
        latch.countDown();

    }

    @Override
    public void onCompleted() {
        roleStreamObserver.onCompleted();
        latch.countDown();
    }
}