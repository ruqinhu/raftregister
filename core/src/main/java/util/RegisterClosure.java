package util;

import com.alipay.sofa.jraft.Closure;
import com.ruqinhu.RegisterResponse;

import java.util.Map;

public abstract class RegisterClosure implements Closure {

    private RegisterOperation registerOperation;

    private RegisterResponse registerResponse;

    public void failure(final String errorMsg, final String redirect) {
        final RegisterResponse response = RegisterResponse.newBuilder()
                .setSuccess(false)
                .setErrorMsg(errorMsg)
                .setRedirect(redirect)
                .build();
        setValueResponse(response);
    }

    public RegisterResponse getRegisterResponse() {
        return registerResponse;
    }

    public void setValueResponse(RegisterResponse valueResponse) {
        this.registerResponse = valueResponse;
    }

    public void setRegisterOperation(RegisterOperation registerOperation) {
        this.registerOperation = registerOperation;
    }

    public RegisterOperation getRegisterOperation() {
        return this.registerOperation;
    }

    public void success(final Map<String, String> value) {
        final RegisterResponse response = RegisterResponse.newBuilder()
                .putAllValue(value)
                .setSuccess(true)
                .build();
        setValueResponse(response);
    }

}
