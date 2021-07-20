package client;

import com.alipay.sofa.jraft.RouteTable;
import com.alipay.sofa.jraft.error.RemotingException;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.rpc.impl.cli.CliClientServiceImpl;

import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RegisterClient {

    final RegisterClientServiceImpl registerClientService;

    public RegisterClient(CliOptions cliOptions, RegisterClientConfig registerClientConfig) throws InterruptedException, TimeoutException {
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

    public Map<String, String> addAndGetRegister(final Map<String, String> registerInfo) throws RemotingException, InterruptedException {
        return registerClientService.addAndGetRegister(registerInfo);
    }

    public Map<String, String> getRegister(final boolean readOnlySafe) throws RemotingException, InterruptedException {
        return  registerClientService.getRegister(readOnlySafe);
    }

    // @Todo 改为 start 方法，参数从配置文件中读取
    public static void main(final String[] args) throws Exception {
        final String groupId = "registerGroup";
        final String confStr = "127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083";

        RegisterClientConfig registerClientConfig = new RegisterClientConfig();
        registerClientConfig.setGroupId(groupId);
        registerClientConfig.setConfStr(confStr);
        registerClientConfig.setRpcTimeOut(registerClientConfig.getRpcTimeOut());

        RegisterClient registerClient = new RegisterClient(new CliOptions(), registerClientConfig);
        registerClient.start();

//        int n = 11;
//        for (int i = 0; i < n; i++) {
//            Map<String, String> registerMap = new HashMap<>();
//            registerMap.put("key"+n, "abc");
//            Map<String, String> result = registerClient.addAndGetRegister(registerMap);
//            System.out.println(result);
//        }

        System.out.println(registerClient.getRegister(true));

    }
}
