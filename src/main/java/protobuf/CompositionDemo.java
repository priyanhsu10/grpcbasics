package protobuf;

import com.google.protobuf.Int32Value;
import com.pro.grpc.models.Address;
import com.pro.grpc.models.BodyStyle;
import com.pro.grpc.models.Car;
import com.pro.grpc.models.Person;

import java.util.ArrayList;
import java.util.List;

public class CompositionDemo {
    public static void main(String[] args) {

        Address address = Address.newBuilder().setPostbox(8089)
                .setCity("pune")
                .setStreet("main steet")

                .build();
        Car car= Car.newBuilder().setName("baleno")
                .setModel("delta")
                .setStyle(BodyStyle.seadan)
                .setYear(2018)
                .build();
        Car car2= Car.newBuilder().setName("suv700")
                .setModel("delta")
                .setYear(2024)
                .setStyle(BodyStyle.suv)
                .build();
        List<Car> cars= new ArrayList<>();
        cars.add(car);
        cars.add(car2);
        Person priyanshu = Person.newBuilder()
                .setName("priyanshu")
                .setAge(Int32Value.newBuilder().setValue(33).build())
                .setAddress(address)
                .addAllCar(cars)
                .build();
        System.out.println(priyanshu);
        System.out.println(car);
    }
}
