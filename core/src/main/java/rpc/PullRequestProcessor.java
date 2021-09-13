package rpc;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.ruqinhu.PullRequest;
import raft.RegisterService;
import util.RegisterClosure;

public class PullRequestProcessor implements RpcProcessor<PullRequest> {

    private final RegisterService registerService;

    public PullRequestProcessor(RegisterService registerService) {
        super();
        this.registerService = registerService;
    }

    @Override
    public void handleRequest(RpcContext rpcCtx, PullRequest request) {
        RegisterClosure closure = new RegisterClosure() {
            @Override
            public void run(Status status) {
                rpcCtx.sendResponse(getRegisterResponse());
            }
        };

        this.registerService.getRegister(request.getIsReadOnlySafe(), closure);
    }

    @Override
    public String interest() {
        //  此处是 request 的class名称
        return PullRequest.class.getName();
    }
}
