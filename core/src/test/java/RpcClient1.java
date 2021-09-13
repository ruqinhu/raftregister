import client.RegisterClient;
import client.RegisterClientConfig;
import com.alipay.sofa.jraft.error.RemotingException;
import com.alipay.sofa.jraft.option.CliOptions;
import util.GrpcClassLoadUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RpcClient1 {

    public static void main(String[] args) throws InterruptedException, TimeoutException, RemotingException {
        final String groupId = "registerGroup";
        final String confStr = "127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083";

        RegisterClientConfig registerClientConfig = new RegisterClientConfig();
        registerClientConfig.setGroupId(groupId);
        registerClientConfig.setConfStr(confStr);
        registerClientConfig.setRpcTimeOut(registerClientConfig.getRpcTimeOut());
        registerClientConfig.setServerRenew(false);

        RegisterClient registerClient = new RegisterClient(new CliOptions(), registerClientConfig);
        registerClient.start();

        GrpcClassLoadUtil.LoadClasses();

        int n = 3;
        for (int i = 0; i < n; i++) {
            Map<String, String> registerMap = new HashMap<>();
            registerMap.put("key"+i, "abc");
            Map<String, String> result = registerClient.addAndGetRegister(registerMap);
            System.out.println(result);
            Thread.sleep(1000);
        }

        System.out.println(registerClient.getRegister(true));

    }

}
