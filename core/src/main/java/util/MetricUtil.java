package util;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MetricUtil {

    private static ConcurrentHashMap<String, Timer> timerMap = new ConcurrentHashMap();

    private static final MetricRegistry metricRegistry = new MetricRegistry();

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(
            new MetricsModule(TimeUnit.SECONDS, TimeUnit.MILLISECONDS, true, MetricFilter.ALL));

    public static Timer getTimer(Class<?> klass, String name) {
        String metricName = MetricRegistry.name(klass, name);
        return timerMap.computeIfAbsent(metricName, k -> metricRegistry.timer(metricName));
    }

    public static Map<String, String> metrics() {
        Map<String, String> retMap = new HashMap<>();
        timerMap.forEach( (s, t) -> {
            try {
                String metricsStr = objectMapper.writeValueAsString(t);
                retMap.put(s, metricsStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return retMap;
    }

}
