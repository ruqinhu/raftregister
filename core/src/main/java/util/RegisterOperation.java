package util;

import java.io.Serializable;
import java.util.Map;

public class RegisterOperation implements Serializable {

    public static final byte PULL = 0x02;   //   get
    public static final byte REGISTER = 0x01;   //   set

    private byte op;
    private Map<String,String> data;

    public static RegisterOperation createPull() {
        return new RegisterOperation(PULL);
    }

    public static RegisterOperation createRegister(Map<String,String> data) {
        return new RegisterOperation(REGISTER, data);
    }

    public RegisterOperation(byte op) {
        this(op, null);
    }

    public RegisterOperation(byte op, Map<String,String> data) {
        this.op = op;
        this.data = data;
    }

    public byte getOp() {
        return op;
    }

    public void setOp(byte op) {
        this.op = op;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
