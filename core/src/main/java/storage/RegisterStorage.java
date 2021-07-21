package storage;

import java.util.Map;

/**
 * map:
 *
 *
 */
public interface RegisterStorage {

    Map<String, String> getRegisterInfo();

    Map<String, String> renewalRegisterInfo(Map<String, String> registerMap);

}
