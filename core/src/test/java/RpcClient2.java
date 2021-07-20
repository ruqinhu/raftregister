import client.RegisterClient;
import client.RegisterClientConfig;
import com.alipay.sofa.jraft.error.RemotingException;
import com.alipay.sofa.jraft.option.CliOptions;

import java.util.concurrent.TimeoutException;

public class RpcClient2 {

    public static void main(String[] args) throws InterruptedException, TimeoutException, RemotingException {
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
//            registerMap.put("key"+i, "abc");
//            Map<String, String> result = registerClient.addAndGetRegister(registerMap);
//            System.out.println(result);
//        }

        int n = 11;
        for (int i = 0; i < n; i++) {
            System.out.println(registerClient.getRegister(true));
        }



    }

}
