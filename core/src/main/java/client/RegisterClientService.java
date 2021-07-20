package client;

import com.alipay.sofa.jraft.error.RemotingException;
import entity.RegisterClosure;

import java.util.Map;

public interface RegisterClientService {

    Map<String, String> addAndGetRegister(final Map<String, String> registerInfo) throws RemotingException, InterruptedException;

    Map<String, String>  getRegister(final boolean readOnlySafe) throws RemotingException, InterruptedException;

}
