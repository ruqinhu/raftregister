package server;

public class RegisterServerConfig {

    private String dataPath = "D:\\log";

    private String groupId = "registerGroup";

    private String serverIdStr = "127.0.0.1:8081";

    private String initConfStr = "127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083";

    private Integer nodeElectionTimeoutMs = 1000;  //设置选举超时时间为

    private Boolean disableCli = false;  //  CLI 服务

    private Integer snapshotIntervalSecs = 30;  //  snapshot

    private Boolean serverRenew = true;

    private static RegisterServerConfig config = null;

    private RegisterServerConfig() {

    }

    public static RegisterServerConfig getInstance() {
        if (config == null) config = new RegisterServerConfig();
        return config;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getServerIdStr() {
        return serverIdStr;
    }

    public void setServerIdStr(String serverIdStr) {
        this.serverIdStr = serverIdStr;
    }

    public String getInitConfStr() {
        return initConfStr;
    }

    public void setInitConfStr(String initConfStr) {
        this.initConfStr = initConfStr;
    }

    public Integer getNodeElectionTimeoutMs() {
        return nodeElectionTimeoutMs;
    }

    public void setNodeElectionTimeoutMs(Integer nodeElectionTimeoutMs) {
        this.nodeElectionTimeoutMs = nodeElectionTimeoutMs;
    }

    public Boolean getDisableCli() {
        return disableCli;
    }

    public void setDisableCli(Boolean disableCli) {
        this.disableCli = disableCli;
    }

    public Integer getSnapshotIntervalSecs() {
        return snapshotIntervalSecs;
    }

    public void setSnapshotIntervalSecs(Integer snapshotIntervalSecs) {
        this.snapshotIntervalSecs = snapshotIntervalSecs;
    }

    public Boolean getServerRenew() {
        return serverRenew;
    }

    public void setServerRenew(Boolean serverRenew) {
        this.serverRenew = serverRenew;
    }
}
