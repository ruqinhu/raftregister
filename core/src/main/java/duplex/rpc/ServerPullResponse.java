package duplex.rpc;

import java.io.Serializable;
import java.util.Map;

public class ServerPullResponse implements Serializable {

    private static final long serialVersionUID = -4220017686717146773L;

    private Map<String, String> value;
    private boolean           success;

    private String            errorMsg;

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, String> getValue() {
        return this.value;
    }

    public void setValue(Map<String, String> value) {
        this.value = value;
    }

    public ServerPullResponse(Map<String, String> value, boolean success, String redirect, String errorMsg) {
        super();
        this.value = value;
        this.success = success;
        this.errorMsg = errorMsg;
    }

    public ServerPullResponse() {
        super();
    }

    @Override
    public String toString() {
        return "ValueResponse [value=" + this.value + ", success=" + this.success
                + ", errorMsg=" + this.errorMsg + "]";
    }

}

