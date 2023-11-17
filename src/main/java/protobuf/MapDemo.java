package protobuf;

import com.pro.grpc.models.Car;
import com.pro.grpc.models.Dealer;

public class MapDemo {
    public static void main(String[] args) {
        Dealer dealer = Dealer.newBuilder()
                .putModel(2018, Car.newBuilder().setModel("baleno").setYear(2018).build())
                .putModel(2024, Car.newBuilder().setModel("suv700").setYear(2024).build())

                .build();
        System.out.println(


                dealer.getModelOrThrow(2025)

        );
//        System.out.println(dealer);

    }
}
