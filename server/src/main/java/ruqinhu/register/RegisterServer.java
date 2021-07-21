package ruqinhu.register;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.RegisterServerConfig;

@Configuration
public class RegisterServer implements ApplicationRunner {

    @Bean
    public RegisterServerConfig getConfig() {
        RegisterServerConfig config = new RegisterServerConfig();
        return config;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        server.RegisterServer registerServer = server.RegisterServer.createInstance(getConfig());
    }

}
