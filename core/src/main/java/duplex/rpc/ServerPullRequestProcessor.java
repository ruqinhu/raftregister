package duplex.rpc;

import client.RegisterClient;
import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;

import java.util.Map;

public class ServerPullRequestProcessor extends AsyncUserProcessor<ServerPullRequest> {

    private final RegisterClient registerClient;

    public ServerPullRequestProcessor(RegisterClient registerClient) {
        super();
        this.registerClient = registerClient;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, ServerPullRequest request) {
        Map<String, String> registerMap = registerClient.getRegisterMap();
        ServerPullResponse response = new ServerPullResponse();
        response.setSuccess(true);
        response.setValue(registerMap);
        asyncCtx.sendResponse(response);
        System.out.println("服务端拉取注册信息: " + registerMap);
    }

    @Override
    public String interest() {
        //  此处是 request 的class名称
        return ServerPullRequest.class.getName();
    }
}
