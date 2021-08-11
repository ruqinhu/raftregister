package raft;

import util.RegisterClosure;

import java.util.Map;

public interface RegisterService {

    void addAndGetRegister(final Map<String, String> registerInfo, final RegisterClosure closure);

    void getRegister(final boolean readOnlySafe, final RegisterClosure closure);

    void addAndGetRegisterAndRenew(final Map<String, String> registerInfo, final RegisterClosure closure, final String address);

}
