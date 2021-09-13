package rpc;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.ruqinhu.RegisterRequest;
import raft.RegisterService;
import server.RegisterServerConfig;
import util.RegisterClosure;

public class RegisterRequestProcessor implements RpcProcessor<RegisterRequest> {

    private final RegisterService registerService;

    public RegisterRequestProcessor(RegisterService registerService) {
        super();
        this.registerService = registerService;
    }

    @Override
    public void handleRequest(RpcContext rpcCtx, RegisterRequest request) {
        RegisterClosure closure = new RegisterClosure() {
            @Override
            public void run(Status status) {
                rpcCtx.sendResponse(getRegisterResponse());
            }
        };

        if (RegisterServerConfig.getInstance().getServerRenew()) {
            this.registerService.addAndGetRegisterAndRenew(request.getData(), closure, rpcCtx.getRemoteAddress());
        } else {
            this.registerService.addAndGetRegister(request.getData(), closure);
        }
    }

    @Override
    public String interest() {
        //  此处是 request 的class名称
        return RegisterRequest.class.getName();
    }
}
