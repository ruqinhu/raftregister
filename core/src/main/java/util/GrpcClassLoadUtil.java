package util;

import com.alipay.sofa.jraft.rpc.RaftRpcFactory;
import com.alipay.sofa.jraft.rpc.impl.GrpcRaftRpcFactory;
import com.alipay.sofa.jraft.rpc.impl.MarshallerHelper;
import com.alipay.sofa.jraft.util.RpcFactoryHelper;
import com.ruqinhu.PullRequest;
import com.ruqinhu.RegisterRequest;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static java.lang.invoke.MethodType.methodType;

public class GrpcClassLoadUtil {

    public static void LoadClasses() {
        try {
            RaftRpcFactory rpcFactory = RpcFactoryHelper.rpcFactory();
            if (rpcFactory instanceof GrpcRaftRpcFactory) {
                MethodHandle pullRequestHandler = MethodHandles.lookup().findStatic(PullRequest.class,
                        "getDefaultInstance", methodType(PullRequest.class));
                rpcFactory.registerProtobufSerializer(PullRequest.class.getName(), pullRequestHandler.invoke());

                MethodHandle registerRequestHandler = MethodHandles.lookup().findStatic(RegisterRequest.class,
                        "getDefaultInstance", methodType(RegisterRequest.class));
                rpcFactory.registerProtobufSerializer(RegisterRequest.class.getName(), registerRequestHandler.invoke());

                MarshallerHelper.registerRespInstance(PullRequest.class.getName(), PullRequest.getDefaultInstance());
                MarshallerHelper.registerRespInstance(RegisterRequest.class.getName(), RegisterRequest.getDefaultInstance());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
