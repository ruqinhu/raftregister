package client;

import com.alipay.sofa.jraft.RouteTable;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.error.RemotingException;
import com.alipay.sofa.jraft.rpc.impl.cli.CliClientServiceImpl;
import rpc.PullRequest;
import rpc.RegisterRequest;
import rpc.RegisterResponse;

import java.util.Map;

public class RegisterClientServiceImpl implements RegisterClientService{

    private final CliClientServiceImpl cliClientService;

    private final RegisterClientConfig registerClientConfig;

    public RegisterClientServiceImpl(CliClientServiceImpl cliClientService, RegisterClientConfig registerClientConfig) {
        this.cliClientService = cliClientService;
        this.registerClientConfig = registerClientConfig;
    }

    @Override
    public Map<String, String> addAndGetRegister(Map<String, String> registerInfo) throws RemotingException, InterruptedException {
        final RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setData(registerInfo);
        RegisterResponse response = (RegisterResponse) cliClientService.getRpcClient().invokeSync(getLeader().getEndpoint(), registerRequest, registerClientConfig.getRpcTimeOut());
        return response.getValue();
    }

    @Override
    public Map<String, String> getRegister(boolean readOnlySafe) throws RemotingException, InterruptedException {
        final PullRequest pullRequest = new PullRequest();
        pullRequest.setReadOnlySafe(readOnlySafe);
        RegisterResponse response = (RegisterResponse) cliClientService.getRpcClient().invokeSync(getLeader().getEndpoint(), pullRequest, registerClientConfig.getRpcTimeOut());
        return response.getValue();
    }

    private PeerId getLeader() {
        return RouteTable.getInstance().selectLeader(registerClientConfig.getGroupId());
    }

    public CliClientServiceImpl getCliClientService() {
        return cliClientService;
    }

    public RegisterClientConfig getRegisterClientConfig() {
        return registerClientConfig;
    }
}
