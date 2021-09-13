package client;

import com.alipay.sofa.jraft.RouteTable;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.error.RemotingException;
import com.alipay.sofa.jraft.rpc.impl.cli.CliClientServiceImpl;
import com.ruqinhu.PullRequest;
import com.ruqinhu.RegisterRequest;
import com.ruqinhu.RegisterResponse;
import util.MetricScheduleThreadPoolExecutor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterClientServiceImpl implements RegisterClientService{

    private final CliClientServiceImpl cliClientService;

    private final RegisterClientConfig registerClientConfig;

    private final MetricScheduleThreadPoolExecutor executor = new MetricScheduleThreadPoolExecutor("renew", 8, null);

    public RegisterClientServiceImpl(CliClientServiceImpl cliClientService, RegisterClientConfig registerClientConfig) {
        this.cliClientService = cliClientService;
        this.registerClientConfig = registerClientConfig;
    }

    @Override
    public Map<String, String> addAndGetRegister(Map<String, String> registerInfo) throws RemotingException, InterruptedException {
        final RegisterRequest registerRequest = RegisterRequest.newBuilder().putAllData(registerInfo).build();
        RegisterResponse response = (RegisterResponse) cliClientService.getRpcClient().invokeSync(getLeader().getEndpoint(), registerRequest, registerClientConfig.getRpcTimeOut());
        if (!registerClientConfig.getServerRenew()) {
            executor.scheduleAtFixedRate(() -> {
                try {
                    cliClientService.getRpcClient().invokeSync(getLeader().getEndpoint(), registerRequest, registerClientConfig.getRpcTimeOut());
                } catch (InterruptedException | RemotingException e) {
                    e.printStackTrace();
                }
            }, registerClientConfig.getRenewSeconds(), registerClientConfig.getRenewSeconds(), TimeUnit.SECONDS);
        }
        return response.getValueMap();
    }

    @Override
    public Map<String, String> getRegister(boolean readOnlySafe) throws RemotingException, InterruptedException {
        final PullRequest pullRequest = PullRequest.newBuilder().setIsReadOnlySafe(readOnlySafe).build();
        RegisterResponse response = (RegisterResponse) cliClientService.getRpcClient().invokeSync(getLeader().getEndpoint(), pullRequest, registerClientConfig.getRpcTimeOut());
        return response.getValueMap();
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
