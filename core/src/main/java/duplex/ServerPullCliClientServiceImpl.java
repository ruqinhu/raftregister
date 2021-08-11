package duplex;

import client.RegisterClient;
import com.alipay.sofa.jraft.rpc.RpcClient;
import com.alipay.sofa.jraft.rpc.impl.BoltRpcClient;
import com.alipay.sofa.jraft.rpc.impl.cli.CliClientServiceImpl;
import duplex.rpc.ServerPullRequestProcessor;

public class ServerPullCliClientServiceImpl extends CliClientServiceImpl {

    private final RegisterClient registerClient;

    public ServerPullCliClientServiceImpl(RegisterClient registerClient) {
        super();
        this.registerClient = registerClient;
    }

    @Override
    protected void configRpcClient(final RpcClient rpcClient) {
        // 注册 ServerPull 的请求响应信息
        if (rpcClient instanceof BoltRpcClient) {
            ((BoltRpcClient) rpcClient).getRpcClient().registerUserProcessor(new ServerPullRequestProcessor(this.registerClient));
        }
    }
}
