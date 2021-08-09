import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.NodeOptions;
import server.RegisterServer;

import java.io.IOException;

public class RegisterServer1 {

    public static void main(String[] args) throws IOException {
//        if (args.length != 4) {
//            System.out
//                    .println("Useage : java com.alipay.sofa.jraft.example.counter.CounterServer {dataPath} {groupId} {serverId} {initConf}");
//            System.out
//                    .println("Example: java com.alipay.sofa.jraft.example.counter.CounterServer /tmp/server1 counter 127.0.0.1:8081 127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083");
//            System.exit(1);
//        }
        final String dataPath = "D:\\log1";
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
        final RegisterServer registerServer   = new RegisterServer(dataPath, groupId, serverId, nodeOptions);
        System.out.println("Started counter server at port:"
                + registerServer.getNode().getNodeId().getPeerId().getPort());
    }

}
