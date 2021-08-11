import com.alipay.sofa.jraft.CliService;
import com.alipay.sofa.jraft.RaftServiceFactory;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.option.NodeOptions;
import server.RaftConfigService;
import server.RegisterServer;
import server.RegisterServerConfig;

import java.io.IOException;

public class RegisterServer4 {

    public static void main(String[] args) throws IOException {
//        if (args.length != 4) {
//            System.out
//                    .println("Useage : java com.alipay.sofa.jraft.example.counter.CounterServer {dataPath} {groupId} {serverId} {initConf}");
//            System.out
//                    .println("Example: java com.alipay.sofa.jraft.example.counter.CounterServer /tmp/server1 counter 127.0.0.1:8081 127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083");
//            System.exit(1);
//        }
        final String dataPath = "D:\\log4";
        final String groupId = "registerGroup";
        final String serverIdStr = "127.0.0.1:8084";
        final String initConfStr = "127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083,127.0.0.1:8084";

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
        RegisterServerConfig.getInstance().setServerRenew(false);
        final RegisterServer registerServer = new RegisterServer(dataPath, groupId, serverId, nodeOptions);


        RaftConfigService.registerSelf(registerServer);


//        PeerId peerId = new PeerId("127.0.0.1", 8084);
//        CliOptions cliOptions = new CliOptions();
//        cliOptions.setMaxRetry(2);
//        cliOptions.setTimeoutMs(5000);
//        CliService cliService = RaftServiceFactory.createAndInitCliService(cliOptions);
//        Status status = cliService.addPeer(counterServer.getNode().getGroupId(), nodeOptions.getInitialConf(), peerId);
//        System.out.println("aaa" + status);;

        System.out.println("Started register server at port:"
                + registerServer.getNode().getNodeId().getPeerId().getPort());
    }

}
