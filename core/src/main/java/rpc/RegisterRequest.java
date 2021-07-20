package rpc;

import java.io.Serializable;
import java.util.Map;

public class RegisterRequest implements Serializable {

    private static final long serialVersionUID = 9218253805003988802L;

    private Map<String, String> data;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
