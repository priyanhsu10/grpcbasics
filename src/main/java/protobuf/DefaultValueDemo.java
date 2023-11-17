package protobuf;

import com.pro.grpc.models.Person;

public class DefaultValueDemo {

    public static void main(String[] args) {
        Person person=Person.newBuilder().build();

        System.out.println(person.hasAddress());
    }
}
