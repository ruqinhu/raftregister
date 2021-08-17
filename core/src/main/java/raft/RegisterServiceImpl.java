package raft;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.serialization.SerializerManager;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.closure.ReadIndexClosure;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.rpc.impl.BoltRpcServer;
import duplex.rpc.ServerPullRequest;
import duplex.rpc.ServerPullResponse;
import org.apache.commons.lang.StringUtils;
import server.RegisterServer;
import util.MetricScheduleThreadPoolExecutor;
import util.RegisterClosure;
import util.RegisterOperation;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RegisterServiceImpl implements RegisterService {

    private final RegisterServer registerServer;
    private final Executor readIndexExecutor;

    private final Integer renewTimeout = 2;

    private final Integer rpcTimeout = 5000;

    private final MetricScheduleThreadPoolExecutor executor = new MetricScheduleThreadPoolExecutor("renew", 8, null);

    public RegisterServiceImpl(RegisterServer counterServer) {
        this.registerServer = counterServer;
        this.readIndexExecutor = createReadIndexExecutor();
    }

    private Executor createReadIndexExecutor() {
        return Executors.newFixedThreadPool(8);
    }

    @Override
    public void addAndGetRegister(Map<String, String> registerInfo, RegisterClosure closure) {
        applyOperation(RegisterOperation.createRegister(registerInfo), closure);
    }

    @Override
    public void getRegister(boolean readOnlySafe, RegisterClosure closure) {
        if (!readOnlySafe) {
            closure.success(getRegisterInfo());
            closure.run(Status.OK());
            return;
        }

        this.registerServer.getNode().readIndex(null, new ReadIndexClosure() {
            @Override
            public void run(Status status, long index, byte[] reqCtx) {
                if (status.isOk()) {
                    System.out.println("peer is leader, not use fsm");
                    closure.success(getRegisterInfo());
                    closure.run(Status.OK());
                    return;
                }
                RegisterServiceImpl.this.readIndexExecutor.execute(() -> {
                    if (isLeader()) {
                        System.out.println("Fail to get value with 'ReadIndex': %s, try to applying to the state machine." + status);
                        applyOperation(RegisterOperation.createPull(), closure);
                    } else {
                        handlerNotLeaderError(closure);
                    }
                });

            }
        });
    }

    //  jraft 的 rpcserver 默认打开了 manageConnection，在 doinit() 之前，可以 addConnection(), 注册一个客户端连接管理器进去
    //  也可以直接保存客户端的连接，通过保存的连接来进行客户端服务端之间的访问
    @Override
    public void addAndGetRegisterAndRenew(Map<String, String> registerInfo, RegisterClosure closure, final String address) {
        this.addAndGetRegister(registerInfo, closure);

        executor.scheduleAtFixedRate(() -> {
            System.out.println("拉取客户端注册信息定时任务开始执行：");
            if (registerServer.getRaftGroupService().getRpcServer() instanceof BoltRpcServer) {
                BoltRpcServer boltRpcServer = (BoltRpcServer) registerServer.getRaftGroupService().getRpcServer();
                try {
                    ServerPullResponse response = (ServerPullResponse) boltRpcServer.getServer().invokeSync(address, new ServerPullRequest(), rpcTimeout);
                    System.out.println("response: " + registerInfo);
                    this.addAndGetRegister(response.getValue(), new RegisterClosure() {
                        //  只是应用拉取的结果，不需要调用回调
                        @Override
                        public void run(Status status) {
                            System.out.println(status);
                        }
                    });
                } catch (RemotingException | InterruptedException e) {
                    System.out.println("拉取客户端注册信息失败");
                    e.printStackTrace();
                }
            }
        }, renewTimeout, renewTimeout, TimeUnit.SECONDS);
    }

    private void applyOperation(final RegisterOperation op, final RegisterClosure closure) {
        if (!isLeader()) {
            handlerNotLeaderError(closure);
            return;
        }

        try {
            closure.setRegisterOperation(op);
            final Task task = new Task();
            task.setData(ByteBuffer.wrap(SerializerManager.getSerializer(SerializerManager.Hessian2).serialize(op)));
            task.setDone(closure);
            this.registerServer.getNode().apply(task);
        } catch (CodecException e) {
            String errorMsg = "Fail to encode CounterOperation";
            System.out.println(errorMsg + e);
            closure.failure(errorMsg, StringUtils.EMPTY);
            closure.run(new Status(RaftError.EINTERNAL, errorMsg));
        }

    }

    private Map<String, String> getRegisterInfo() {
        return this.registerServer.getFsm().getRegisterInfo();
    }

    private boolean isLeader() {
        return this.registerServer.getNode().isLeader();
    }

    private String getRedirect() {
        return this.registerServer.redirect().getRedirect();
    }

    private void handlerNotLeaderError(RegisterClosure closure) {
        closure.failure("Not leader.", getRedirect());
        closure.run(new Status(RaftError.EPERM, "Not leader"));
    }
}
