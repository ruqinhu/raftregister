package grpc.entity;

import com.ruqinhu.RegisterRequest;

import java.util.HashMap;

public class GrpcEntityTest {

    public static void main(String[] args) {
        RegisterRequest registerRequest =  RegisterRequest.newBuilder().putAllData(new HashMap<>()).build();
        System.out.println(registerRequest);
    }

}
