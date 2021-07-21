package storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GuavaStorage implements RegisterStorage {

    private final Cache<String, String> idRegMap;

    public GuavaStorage(Integer cacheSeconds) {
        idRegMap = CacheBuilder.newBuilder().expireAfterAccess(cacheSeconds, TimeUnit.SECONDS).build();
    }

    @Override
    public Map<String, String> getRegisterInfo() {
        return idRegMap.asMap();
    }

    @Override
    public Map<String, String> renewalRegisterInfo(Map<String, String> registerMap) {
        idRegMap.putAll(registerMap);
        return idRegMap.asMap();
    }
}
