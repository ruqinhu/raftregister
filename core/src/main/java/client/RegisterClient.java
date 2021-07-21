package client;

import com.alipay.sofa.jraft.RouteTable;
import com.alipay.sofa.jraft.error.RemotingException;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.rpc.impl.cli.CliClientServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RegisterClient {

    final RegisterClientServiceImpl registerClientService;

    public RegisterClient(CliOptions cliOptions, RegisterClientConfig registerClientConfig) {
        final CliClientServiceImpl cliClientService = new CliClientServiceImpl();
        cliClientService.init(cliOptions);
        this.registerClientService = new RegisterClientServiceImpl(cliClientService, registerClientConfig);
    }

    public void start(){
        RouteTable.getInstance().updateConfiguration(registerClientService.getRegisterClientConfig().getGroupId(), registerClientService.getRegisterClientConfig().getConfStr());

        try {
            if (!RouteTable.getInstance().refreshLeader(registerClientService.getCliClientService(), registerClientService.getRegisterClientConfig().getGroupId(), 1000).isOk()) {
                throw new IllegalStateException("Refresh leader failed");
            }
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> addAndGetRegister(final Map<String, String> registerInfo){
        try {
            return registerClientService.addAndGetRegister(registerInfo);
        } catch (RemotingException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> getRegister(final boolean readOnlySafe) {
        try {
            return  registerClientService.getRegister(readOnlySafe);
        } catch (RemotingException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RegisterClient createInstance(RegisterClientConfig config) {
        final CliOptions cliOptions = new CliOptions();
        final CliClientServiceImpl cliClientService = new CliClientServiceImpl();
        cliClientService.init(cliOptions);
        RegisterClient registerClient = new RegisterClient(cliOptions, config);
        registerClient.start();
        return registerClient;
    }

}
