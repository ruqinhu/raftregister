package entity;

import com.alipay.sofa.jraft.Closure;
import rpc.RegisterResponse;

import java.util.Map;

public abstract class RegisterClosure implements Closure {

    private RegisterOperation registerOperation;

    private RegisterResponse registerResponse;

    public void failure(final String errorMsg, final String redirect) {
        final RegisterResponse response = new RegisterResponse();
        response.setSuccess(false);
        response.setErrorMsg(errorMsg);
        response.setRedirect(redirect);
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
        final RegisterResponse response = new RegisterResponse();
        response.setValue(value);
        response.setSuccess(true);
        setValueResponse(response);
    }

}
