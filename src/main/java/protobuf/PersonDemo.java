package protobuf;
import com.google.protobuf.Int32Value;
import com.pro.grpc.models.Person;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PersonDemo {
    public static void main(String[] args) throws IOException {
//----------basics
        Person priyanshu = Person.newBuilder()
                .setAge(Int32Value.newBuilder().setValue(33).build()).setName("priyanshu").build();
//        Person priyanshu2 = Person.newBuilder()
//                .setAge(10).setName("Priyanshu").build();
//
//        //we cannot ovveride the equals method instead of the implement though comparable interface
//        System.out.println(priyanshu.equals(priyanshu2));
//
//

//---------------------
      //  serialization and deserialization
//        Path path= Paths.get("priyanshu.ser");
//        Files.write(path,priyanshu.toByteArray());

        byte[] bytes = Files.readAllBytes(Path.of("priyanshu.ser"));

        Person newPriyanshu= Person.parseFrom(bytes);
        System.out.println(newPriyanshu);

        System.out.println(priyanshu.hasAge());


    }
}
