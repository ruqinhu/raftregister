package ruqinhu.register;

import client.RegisterClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegisterClient {

    @Bean
    public RegisterClientConfig getConfig() {
        client.RegisterClientConfig config = new RegisterClientConfig();
        return config;
    }

    @Bean
    public client.RegisterClient getRegisterClient(RegisterClientConfig config) {
        client.RegisterClient registerClient = client.RegisterClient.createInstance(config);
        return registerClient;
    }

}
