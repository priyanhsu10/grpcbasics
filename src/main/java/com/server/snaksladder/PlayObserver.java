package com.server.snaksladder;

import com.pro.grpc.models.Role;
import com.pro.grpc.models.ServerResponse;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlayObserver implements StreamObserver<Role> {
    private final StreamObserver<ServerResponse> responseObserver;
    Map<Integer, Integer> snakeladerMap = getSnakeladerMap();
    int clientCurrentPosition = 0;
    int serverCurrentPosition = 0;

    public PlayObserver(StreamObserver<ServerResponse> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(Role role) {


        if (clientCurrentPosition >= 100 || serverCurrentPosition >= 100) {
            responseObserver.onCompleted();
        }
        ///client turn
        int tempCurrentClientPos = clientCurrentPosition + role.getDiasNumber();

        Integer nextpostion = snakeladerMap.getOrDefault(tempCurrentClientPos, tempCurrentClientPos);
        //ternmination conditon
        String clientMessage = null;
        String serverMessage = null;
        int diasNumber = new Random().ints(1, 7).findFirst().getAsInt();
        int tempCurrentServerPos = serverCurrentPosition + diasNumber;
        Integer nextServertposition = snakeladerMap.getOrDefault(tempCurrentServerPos, tempCurrentServerPos);
        if (nextpostion == 100) {
            clientCurrentPosition = nextpostion;
            clientMessage = "user: ---- win -----";
            responseObserver.onNext(ServerResponse.newBuilder().setClientMessage(clientMessage)
                    .setCurrentServerPosition(serverCurrentPosition)
                    .setCurrentPosition(clientCurrentPosition)
                    .build());
            return;
        }

        if (nextServertposition == 100) {

            clientCurrentPosition = nextpostion;
            serverMessage = "server: ---- win -----";
            responseObserver.onNext(ServerResponse.newBuilder().setClientMessage(serverMessage)
                    .setCurrentServerPosition(serverCurrentPosition)
                    .setCurrentPosition(clientCurrentPosition)
                    .build());
            return;
        }
        if (nextpostion < 100) {

            clientMessage = getClientMeesage(tempCurrentClientPos, nextpostion);
            clientCurrentPosition = nextpostion;

        } else {
            clientMessage = getClientMeesage(clientCurrentPosition, clientCurrentPosition);

        }


        // server tern


        if (nextServertposition < 100) {

            serverMessage = getClientMeesage(tempCurrentServerPos, nextServertposition);
            serverCurrentPosition = nextServertposition;

            //return current position and message
        } else {

            serverMessage = getClientMeesage(serverCurrentPosition, serverCurrentPosition);

        }


        ServerResponse serverResponse = ServerResponse.newBuilder().setClientMessage(clientMessage).setServerMessage(serverMessage)
                .setCurrentPosition(clientCurrentPosition)
                .setServerDiasNumber(diasNumber)
                .setCurrentServerPosition(serverCurrentPosition).build();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        responseObserver.onNext(serverResponse);

    }

    private static String getClientMeesage(int tempCurrentClientPos, Integer nextpostion) {
        String message;
        if (nextpostion == tempCurrentClientPos) {
            //no ladder no snake
            message = "current position at " + nextpostion;

        } else if (nextpostion < tempCurrentClientPos) {
            //snake bype
            message = " snake Byte at " + tempCurrentClientPos + " current position at " + nextpostion;
        } else {
            //ladder
            message = " Hurray.... Ladder found at " + tempCurrentClientPos + " current position at " + nextpostion;
        }
        return message;
    }

    @Override
    public void onError(Throwable throwable) {
        responseObserver.onCompleted();
    }

    @Override
    public void onCompleted() {
        responseObserver.onCompleted();
        ;
    }

    private static Map<Integer, Integer> getSnakeladerMap() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(80, 99);
        map.put(97, 78);
        map.put(95, 56);
        map.put(71, 92);
        map.put(88, 24);
        map.put(28, 76);
        map.put(66, 18);
        map.put(50, 67);
        map.put(48, 26);
        map.put(21, 42);
        map.put(1, 38);
        map.put(36, 6);
        map.put(32, 10);
        map.put(8, 30);
        map.put(4, 14);
        return map;

    }
}
