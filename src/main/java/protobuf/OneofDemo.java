package protobuf;

import com.pro.grpc.models.Credential;
import com.pro.grpc.models.Email;
import com.pro.grpc.models.Phone;

public class OneofDemo {

    public static void main(String[] args) {
        Phone phone = Phone.newBuilder().setMobile("42342343423")
                .setOtp(1233).build();
        Email email = Email.newBuilder().setEmail("abc@gmail.com")
                .setPassword("12321asae").build();

        Credential credential = Credential.newBuilder()
                .setPhone(phone)
                .setEmail(email)
                .build();
        login(credential);
    }

    public static void login(Credential credentials) {


        switch (credentials.getModeCase()) {
            case EMAIL -> System.out.println(credentials.getEmail());
            case PHONE -> System.out.println(credentials.getPhone());
        }
    }
}

