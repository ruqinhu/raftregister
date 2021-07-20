package server;

import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.RpcServer;
import org.apache.commons.io.FileUtils;
import raft.RegisterService;
import raft.RegisterServiceImpl;
import raft.RegisterStateMachine;
import rpc.PullRequestProcessor;
import rpc.RegisterRequestProcessor;
import rpc.RegisterResponse;

import java.io.File;
import java.io.IOException;

public class RegisterServer {

    private RaftGroupService raftGroupService;
    private Node node;
    private RegisterStateMachine fsm;

    public RegisterServer(final String dataPath, final String groupId, final PeerId serverId,
                         final NodeOptions nodeOptions) throws IOException {
        // 初始化路径
        FileUtils.forceMkdir(new File(dataPath));

        // 这里让 raft RPC 和业务 RPC 使用同一个 RPC server, 通常也可以分开
        final RpcServer rpcServer = RaftRpcServerFactory.createRaftRpcServer(serverId.getEndpoint());
        // 注册业务处理器
        RegisterService counterService = new RegisterServiceImpl(this);
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
        // 启动
        this.node = this.raftGroupService.start();
    }

    public RegisterStateMachine getFsm() {
        return this.fsm;
    }

    public Node getNode() {
        return this.node;
    }

    public RaftGroupService RaftGroupService() {
        return this.raftGroupService;
    }

    /**
     * Redirect request to new leader
     */
    public RegisterResponse redirect() {
        final RegisterResponse response = new RegisterResponse();
        response.setSuccess(false);
        if (this.node != null) {
            final PeerId leader = this.node.getLeaderId();
            if (leader != null) {
                response.setRedirect(leader.toString());
            }
        }
        return response;
    }

    // @Todo 改为 start 方法，参数从配置文件中读取
    public static void main(final String[] args) throws IOException {
//        if (args.length != 4) {
//            System.out
//                    .println("Useage : java com.alipay.sofa.jraft.example.counter.CounterServer {dataPath} {groupId} {serverId} {initConf}");
//            System.out
//                    .println("Example: java com.alipay.sofa.jraft.example.counter.CounterServer /tmp/server1 counter 127.0.0.1:8081 127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083");
//            System.exit(1);
//        }
        final String dataPath = "D:\\log";
        final String groupId = "registerGroup";
        final String serverIdStr = "127.0.0.1:8081";
        final String initConfStr = "127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083";

        final NodeOptions nodeOptions = new NodeOptions();
        // 为了测试,调整 snapshot 间隔等参数
        // 设置选举超时时间为 1 秒
        nodeOptions.setElectionTimeoutMs(1000);
        // 关闭 CLI 服务。
        nodeOptions.setDisableCli(false);
        // 每隔30秒做一次 snapshot
        nodeOptions.setSnapshotIntervalSecs(30);
        // 解析参数
        final PeerId serverId = new PeerId();
        if (!serverId.parse(serverIdStr)) {
            throw new IllegalArgumentException("Fail to parse serverId:" + serverIdStr);
        }
        final Configuration initConf = new Configuration();
        if (!initConf.parse(initConfStr)) {
            throw new IllegalArgumentException("Fail to parse initConf:" + initConfStr);
        }
        // 设置初始集群配置
        nodeOptions.setInitialConf(initConf);

        // 启动
        final RegisterServer counterServer = new RegisterServer(dataPath, groupId, serverId, nodeOptions);
        System.out.println("Started counter server at port:"
                + counterServer.getNode().getNodeId().getPeerId().getPort());
    }
}
