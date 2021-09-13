package server;

import com.alipay.sofa.jraft.CliService;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.RaftServiceFactory;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.RpcServer;
import com.ruqinhu.RegisterResponse;
import org.apache.commons.io.FileUtils;
import raft.RegisterService;
import raft.RegisterServiceImpl;
import raft.RegisterStateMachine;
import rpc.PullRequestProcessor;
import rpc.RegisterRequestProcessor;
import util.GrpcClassLoadUtil;

import java.io.File;
import java.io.IOException;

public class RegisterServer {

    private final RaftGroupService raftGroupService;
    private final Node node;
    private final RegisterStateMachine fsm;

    private CliService cliService;

    public RegisterServer(final String dataPath, final String groupId, final PeerId serverId,
                         final NodeOptions nodeOptions) throws IOException {
        // 初始化路径
        FileUtils.forceMkdir(new File(dataPath));

        // 这里让 raft RPC 和业务 RPC 使用同一个 RPC server, 通常也可以分开
        final RpcServer rpcServer = RaftRpcServerFactory.createRaftRpcServer(serverId.getEndpoint());
        // 注册业务处理器
        RegisterService counterService = new RegisterServiceImpl(this);
        GrpcClassLoadUtil.LoadClasses();
        rpcServer.registerProcessor(new PullRequestProcessor(counterService));
        rpcServer.registerProcessor(new RegisterRequestProcessor(counterService));
        // 初始化状态机
        this.fsm = new RegisterStateMachine();
        // 设置状态机到启动参数
        nodeOptions.setFsm(this.fsm);
        // 设置存储路径
        // 日志, 必须
        nodeOptions.setLogUri(dataPath + File.separator + "log");
        // 元信息, 必须
        nodeOptions.setRaftMetaUri(dataPath + File.separator + "raft_meta");
        // snapshot, 可选, 一般都推荐
        nodeOptions.setSnapshotUri(dataPath + File.separator + "snapshot");
        // 初始化 raft group 服务框架
        this.raftGroupService = new RaftGroupService(groupId, serverId, nodeOptions, rpcServer);
        // 初始化 cliService ，用来向 leader 增删节点
        this.initCliService();
        // 启动
        this.node = this.raftGroupService.start();
    }

    public RegisterStateMachine getFsm() {
        return this.fsm;
    }

    public Node getNode() {
        return this.node;
    }

    public RaftGroupService getRaftGroupService() {
        return this.raftGroupService;
    }

    public CliService getCliService() {
        return this.cliService;
    }

    /**
     * Redirect request to new leader
     */
    public RegisterResponse redirect() {
        final RegisterResponse.Builder responseBuild = RegisterResponse.newBuilder();
        responseBuild.setSuccess(false);
        if (this.node != null) {
            final PeerId leader = this.node.getLeaderId();
            if (leader != null) {
                responseBuild.setRedirect(leader.toString());
            }
        }
        return responseBuild.build();
    }

    private void initCliService() {
        CliOptions cliOptions = new CliOptions();
        cliOptions.setMaxRetry(2);
        cliOptions.setTimeoutMs(5000);
        this.cliService = RaftServiceFactory.createAndInitCliService(cliOptions);
    }


    public static RegisterServer createInstance(RegisterServerConfig config) throws IOException {
        final NodeOptions nodeOptions = new NodeOptions();
        // 为了测试,调整 snapshot 间隔等参数
        // 设置选举超时时间为 1 秒
        nodeOptions.setElectionTimeoutMs(config.getNodeElectionTimeoutMs());
        // 关闭 CLI 服务。
        nodeOptions.setDisableCli(config.getDisableCli());
        // 每隔30秒做一次 snapshot
        nodeOptions.setSnapshotIntervalSecs(config.getSnapshotIntervalSecs());
        // 解析参数
        final PeerId serverId = new PeerId();
        if (!serverId.parse(config.getServerIdStr())) {
            throw new IllegalArgumentException("Fail to parse serverId:" + config.getServerIdStr());
        }
        final Configuration initConf = new Configuration();
        if (!initConf.parse(config.getInitConfStr())) {
            throw new IllegalArgumentException("Fail to parse initConf:" + config.getInitConfStr());
        }
        // 设置初始集群配置
        nodeOptions.setInitialConf(initConf);

        // 启动
        final RegisterServer counterServer = new RegisterServer(config.getDataPath(), config.getGroupId(), serverId, nodeOptions);
        System.out.println("Started register server at port:"
                + counterServer.getNode().getNodeId().getPeerId().getPort());
        return counterServer;
    }

}
