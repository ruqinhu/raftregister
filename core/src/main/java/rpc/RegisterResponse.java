//package rpc;
//
//import java.io.Serializable;
//import java.util.Map;
//
//public class RegisterResponse implements Serializable {
//
//    private static final long serialVersionUID = -4220017686727146773L;
//
//    private Map<String, String>              value;
//    private boolean           success;
//
//    /**
//     * redirect peer id
//     */
//    private String            redirect;
//
//    private String            errorMsg;
//
//    public String getErrorMsg() {
//        return this.errorMsg;
//    }
//
//    public void setErrorMsg(String errorMsg) {
//        this.errorMsg = errorMsg;
//    }
//
//    public String getRedirect() {
//        return this.redirect;
//    }
//
//    public void setRedirect(String redirect) {
//        this.redirect = redirect;
//    }
//
//    public boolean isSuccess() {
//        return this.success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }
//
//    public Map<String, String> getValue() {
//        return this.value;
//    }
//
//    public void setValue(Map<String, String> value) {
//        this.value = value;
//    }
//
//    public RegisterResponse(Map<String, String> value, boolean success, String redirect, String errorMsg) {
//        super();
//        this.value = value;
//        this.success = success;
//        this.redirect = redirect;
//        this.errorMsg = errorMsg;
//    }
//
//    public RegisterResponse() {
//        super();
//    }
//
//    @Override
//    public String toString() {
//        return "ValueResponse [value=" + this.value + ", success=" + this.success + ", redirect=" + this.redirect
//                + ", errorMsg=" + this.errorMsg + "]";
//    }
//
//}
