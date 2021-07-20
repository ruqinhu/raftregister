package raft;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.SerializerManager;
import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;
import com.alipay.sofa.jraft.util.Utils;
import entity.RegisterClosure;
import entity.RegisterOperation;
import snapshot.RegisterSnapshot;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static entity.RegisterOperation.PULL;
import static entity.RegisterOperation.REGISTER;

public class RegisterStateMachine extends StateMachineAdapter {

    private Map<String, String> idRegMap = new HashMap<>();

    /**
     * Leader term
     */
    private final AtomicLong leaderTerm = new AtomicLong(-1);

    @Override
    public void onApply(Iterator iter) {
        Map<String, String> current = new HashMap<>();
        RegisterOperation registerOperation = null;

        RegisterClosure closure = null;
        if (iter.done() != null) {
            // This task is applied by this node, get value from closure to avoid additional parsing.
            closure = (RegisterClosure) iter.done();
            registerOperation = closure.getRegisterOperation();
        } else {
            // Have to parse FetchAddRequest from this user log.
            final ByteBuffer data = iter.getData();
            try {
                registerOperation = SerializerManager.getSerializer(SerializerManager.Hessian2).deserialize(
                        data.array(), RegisterOperation.class.getName());
            } catch (final CodecException e) {
                System.out.println("Fail to decode IncrementAndGetRequest");
                e.printStackTrace();
            }
        }
        if (registerOperation != null) {
            switch (registerOperation.getOp()) {
                case REGISTER:
                    final Map<String, String> registerInfo = registerOperation.getData();
                    idRegMap.putAll(registerInfo);
                    current.putAll(idRegMap);
                    System.out.printf("Added value=%s by delta=%s at logIndex=%s", idRegMap, registerInfo, iter.getIndex());
                    break;
                case PULL:
                    current.putAll(idRegMap);
                    System.out.printf("Get value=%s at logIndex=%s%n", current, iter.getIndex());
                    break;
            }

            if (closure != null) {
                closure.success(current);
                closure.run(Status.OK());
            }
        }
        iter.next();
    }


    @Override
    public void onSnapshotSave(final SnapshotWriter writer, final Closure done) {
        final Map<String, String> snapshotMap = this.idRegMap;
        Utils.runInThread(() -> {
            final RegisterSnapshot snapshot = new RegisterSnapshot(writer.getPath() + File.separator + "data");
            if (snapshot.save(snapshotMap)) {
                if (writer.addFile("data")) {
                    done.run(Status.OK());
                } else {
                    done.run(new Status(RaftError.EIO, "Fail to add file to writer"));
                }
            } else {
                done.run(new Status(RaftError.EIO, "Fail to save counter snapshot %s", snapshot.getPath()));
            }
        });
    }

    @Override
    public boolean onSnapshotLoad(final SnapshotReader reader) {
        if (isLeader()) {
            System.out.println("Leader is not supposed to load snapshot");
            return false;
        }
        if (reader.getFileMeta("data") == null) {
            System.out.println("Fail to find data file in %s" + reader.getPath());
            return false;
        }
        final RegisterSnapshot snapshot = new RegisterSnapshot(reader.getPath() + File.separator + "data");
        try {
            this.idRegMap.putAll(snapshot.load());
            return true;
        } catch (final IOException e) {
            System.out.println("Fail to load snapshot from %s" + snapshot.getPath());
            return false;
        }

    }

    @Override
    public void onLeaderStart(final long term) {
        this.leaderTerm.set(term);
        super.onLeaderStart(term);

    }

    @Override
    public void onLeaderStop(final Status status) {
        this.leaderTerm.set(-1);
        super.onLeaderStop(status);
    }

    public boolean isLeader() {
        return this.leaderTerm.get() > 0;
    }


    public Map<String, String> getRegisterInfo() {
        return new HashMap<>(idRegMap);
    }
}
