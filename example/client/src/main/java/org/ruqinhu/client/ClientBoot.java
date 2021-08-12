package org.ruqinhu.client;

import client.RegisterClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ClientBoot implements ApplicationRunner {

    final
    RegisterClient registerClient;

    public ClientBoot(RegisterClient registerClient) {
        this.registerClient = registerClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientBoot.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, String> map = new HashMap<>();
        map.put("config", "value");
        System.out.println(registerClient.addAndGetRegister(map));

        System.out.println(registerClient.getRegister(true));
    }
}
