package client;

public class RegisterClientConfig {

    private String groupId = "registerGroup";

    private String confStr = "127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083";

    private Long rpcTimeOut = 1000L;  //  超时时间要比续约时间短


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getConfStr() {
        return confStr;
    }

    public void setConfStr(String confStr) {
        this.confStr = confStr;
    }

    public Long getRpcTimeOut() {
        return rpcTimeOut;
    }

    public void setRpcTimeOut(Long rpcTimeOut) {
        this.rpcTimeOut = rpcTimeOut;
    }
}
