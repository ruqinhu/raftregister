package server;

import com.alipay.sofa.jraft.Status;

public class RaftConfigService {

    public static boolean registerSelf(RegisterServer registerServer) {
        Status status = registerServer.getCliService().addPeer(registerServer.getNode().getNodeId().getGroupId(), registerServer.getNode().getOptions().getInitialConf(), registerServer.getNode().getNodeId().getPeerId());
        return status.isOk();
    }

//    public static void getLeaderConfig(RegisterServer registerServer) {
//        registerServer
//    }


}
