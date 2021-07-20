package rpc;

import java.io.Serializable;

public class PullRequest implements Serializable {

    private static final long serialVersionUID = -5623664785560971849L;

    private boolean isReadOnlySafe;

    public boolean isReadOnlySafe() {
        return isReadOnlySafe;
    }

    public void setReadOnlySafe(boolean readOnlySafe) {
        isReadOnlySafe = readOnlySafe;
    }
}
