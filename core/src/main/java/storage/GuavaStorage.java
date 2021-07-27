package storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GuavaStorage implements RegisterStorage {

    private final Cache<String, String> idRegMap;

    private Integer cacheSeconds = 5;

    public GuavaStorage(Integer cacheSeconds) {
        this.cacheSeconds = cacheSeconds;
        idRegMap = CacheBuilder.newBuilder().expireAfterAccess(this.cacheSeconds, TimeUnit.SECONDS).build();
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

    public static void main(String[] args) throws InterruptedException {
        final RegisterStorage storage = new GuavaStorage(5);
        Map<String, String> param = new HashMap<>();
        param.put("aaa", "111");
        param.put("bbb", "222");
        storage.renewalRegisterInfo(param);

        System.out.println(storage.getRegisterInfo());

        Thread.sleep(6000);

        System.out.println(storage.getRegisterInfo());

        Thread.sleep(2000);

        System.out.println(storage.getRegisterInfo());
    }
}
