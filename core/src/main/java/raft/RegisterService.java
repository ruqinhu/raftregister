package raft;

import entity.RegisterClosure;

import java.util.Map;

public interface RegisterService {

    void addAndGetRegister(final Map<String, String> registerInfo, final RegisterClosure closure);

    void getRegister(final boolean readOnlySafe, final RegisterClosure closure);

}
