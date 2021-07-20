package snapshot;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.SerializerManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class RegisterSnapshot {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterSnapshot.class);

    private String              path;

    public RegisterSnapshot(String path) {
        super();
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    /**
     * Save value to snapshot file.
     */
    public boolean save(final Map<String, String> value) {
        try {
            byte[] byteValue = SerializerManager.getSerializer(SerializerManager.Hessian2).serialize(value);
            FileUtils.writeStringToFile(new File(path), Arrays.toString(byteValue), Charset.defaultCharset());
            return true;
        } catch (IOException e) {
            LOG.error("Fail to save snapshot", e);
            return false;
        } catch (CodecException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<String, String> load() throws IOException {
        final String fileStr = FileUtils.readFileToString(new File(path), Charset.defaultCharset());
        try {
            Map<String, String> map = SerializerManager.getSerializer(SerializerManager.Hessian2).deserialize(fileStr.getBytes(Charset.defaultCharset()), String.valueOf(Map.class));
            return map;
        } catch (CodecException e) {
            e.printStackTrace();
        }

        throw new IOException("Fail to load snapshot from " + path + ",content: " + fileStr);
    }
}
