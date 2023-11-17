package protobuf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pro.grpc.models.Person;
import json.JPerson;

public class PerformanceTest {

    public static void main(String[] args) throws JsonProcessingException {

        JPerson jPerson = new JPerson("priyanshu", 33);
        ObjectMapper mapper = new ObjectMapper();
        String jPersonString = mapper.writeValueAsString(jPerson);
        //json
        Runnable runnable1 = () -> {
            try {
                byte[] bytes = mapper.writeValueAsBytes(jPerson);
                JPerson jPerson1 = mapper.readValue(bytes, JPerson.class);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        };

        //protobuf

        Person person = Person.newBuilder().setAge(33)
                .setName("priyanshu")
                .build();
        Runnable runnable2 = () -> {
            byte[] byteArray = person.toByteArray();
            try {
                Person person1 = Person.parseFrom(byteArray);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }

        };
        for (int i=0;i<5;i++){
            runPerformanceTest(runnable1, "json");
            runPerformanceTest(runnable2, "protobu");
        }
    }

    private static void runPerformanceTest(Runnable runnable, String method) {

        long start = System.currentTimeMillis();

        for (int i = 0; i <= 1_000_000; i++) {

            runnable.run();

        }

        long end = System.currentTimeMillis();

        System.out.println(method + " :" + (end - start) + " ms");

    }
}
