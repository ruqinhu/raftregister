package org.ruqinhu.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.RegisterServerConfig;

import java.io.IOException;

@Configuration
public class RegisterServer {

    @Bean
    public RegisterServerConfig getConfig() {
        return RegisterServerConfig.getInstance();
    }

    @Bean
    public server.RegisterServer getRegisterServer() throws IOException {
        return server.RegisterServer.createInstance(getConfig());
    }

}
