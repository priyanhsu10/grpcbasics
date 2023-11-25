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

//termination condition
        if (clientCurrentPosition >= 100 || serverCurrentPosition >= 100) {
            responseObserver.onCompleted();
        }
        ///client turn
        int tempCurrentClientPos = clientCurrentPosition + role.getDiasNumber();

        Integer nextpostion = snakeladerMap.getOrDefault(tempCurrentClientPos, tempCurrentClientPos);

        String clientMessage = null;
        String serverMessage = null;
        int diasNumber = new Random().ints(1, 7).findFirst().getAsInt();
        int tempCurrentServerPos = serverCurrentPosition + diasNumber;
        Integer nextServerPosition = snakeladerMap.getOrDefault(tempCurrentServerPos, tempCurrentServerPos);

        if (nextpostion == 100 ||nextServerPosition == 100) {
            clientCurrentPosition = nextpostion;
            clientMessage = nextpostion==100? "cleint: ":"server: ";

            clientMessage +=" ---- win -----";
            responseObserver.onNext(createResponse(clientMessage));
            return;
        }

        clientMessage = playClient(tempCurrentClientPos, nextpostion);

        serverMessage = playServer(tempCurrentServerPos, nextServerPosition);


        ServerResponse serverResponse = createResponse(clientMessage, serverMessage, diasNumber);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        responseObserver.onNext(serverResponse);

    }

    private String playServer(int tempCurrentServerPos, Integer nextServerPosition) {
        String serverMessage;
        if (nextServerPosition < 100) {

            serverMessage = getClientMeesage(tempCurrentServerPos, nextServerPosition);
            serverCurrentPosition = nextServerPosition;
        } else {

            serverMessage = getClientMeesage(serverCurrentPosition, serverCurrentPosition);

        }
        return serverMessage;
    }

    private String playClient(int tempCurrentClientPos, Integer nextpostion) {
        String clientMessage;
        if (nextpostion < 100) {

            clientMessage = getClientMeesage(tempCurrentClientPos, nextpostion);
            clientCurrentPosition = nextpostion;

        } else {
            clientMessage = getClientMeesage(clientCurrentPosition, clientCurrentPosition);

        }
        return clientMessage;
    }

    private ServerResponse createResponse(String clientMessage, String serverMessage, int diasNumber) {
        return ServerResponse.newBuilder().setClientMessage(clientMessage).setServerMessage(serverMessage)
                .setCurrentPosition(clientCurrentPosition)
                .setServerDiasNumber(diasNumber)
                .setCurrentServerPosition(serverCurrentPosition).build();
    }

    private ServerResponse createResponse(String clientMessage) {
        return ServerResponse.newBuilder().setClientMessage(clientMessage)
                .setCurrentServerPosition(serverCurrentPosition)
                .setCurrentPosition(clientCurrentPosition)
                .build();
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
