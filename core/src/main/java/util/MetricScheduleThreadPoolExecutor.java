package util;

import com.codahale.metrics.Timer;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 1. 任务监控：（-支持应用粒度-、、-任务粒度- 未实现） 线程池粒度 的Transaction监控；可以看到线程池的任务执行情况、最大任务执行时间、平均任务执行时间、95/99线等。
 * 2. 告警通知
 * 3. 运行时参数查看
 */
public class MetricScheduleThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    private static final Map<Integer, Timer.Context> timerContextMap = new HashMap<>();

    private static final Map<String, MetricScheduleThreadPoolExecutor> threadPoolExecutorMap = new HashMap<>();

    private final String threadName;

    private final Notification notification;

    public MetricScheduleThreadPoolExecutor(String threadName, int corePoolSize, Notification notification) {
        super(corePoolSize, new DefaultThreadFactory(threadName));
        this.threadName = threadName;
        this.notification = notification;
        threadPoolExecutorMap.put(threadName, this);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        try {
            timerContextMap.put(r.hashCode(), MetricUtil.getTimer(this.getClass(), getPoolName()).time());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        try {
            timerContextMap.get(r.hashCode()).stop();
            timerContextMap.remove(r.hashCode());
            if (this.notification != null) {
                if ((double) getActiveCount() / getMaximumPoolSize() > 0.8) { // 线程池活跃度 = activeCount/maximumPoolSize
                    notification.warningNotify();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPoolName() {
        return this.threadName;
    }

    public static Map<String, Map<String, Object>> runtimeInfo() {
        Map<String,  Map<String, Object>> retMap = new HashMap<>();
        threadPoolExecutorMap.forEach((s, t) -> {
            Map<String, Object> j = new HashMap<>();

            String poolName = t.getPoolName();
            Integer corePoolSize = t.getCorePoolSize();
            int maximumPoolSize = t.getMaximumPoolSize();
            Integer poolSize = t.getPoolSize();
            Integer activeCount = t.getActiveCount();

            double threshold = (double) activeCount / maximumPoolSize;  // 活跃度

            String queueType = t.getQueue().getClass().getSimpleName();
            Integer queueSize = t.getQueue().size();

            Integer completedTaskCount  = Long.valueOf(t.getCompletedTaskCount()).intValue();
            Integer taskCount  = Long.valueOf(t.getTaskCount()).intValue();
            Integer largestPoolSize = t.getLargestPoolSize();

            j.put("poolName", poolName);
            j.put("corePoolSize", corePoolSize);
            j.put("maximumPoolSize", maximumPoolSize);
            j.put("poolSize", poolSize);
            j.put("activeCount", activeCount);
            j.put("threshold", threshold);
            j.put("queueType", queueType);
            j.put("queueSize", queueSize);
            j.put("completedTaskCount", completedTaskCount);
            j.put("taskCount", taskCount);
            j.put("largestPoolSize", largestPoolSize);
            retMap.put(s, j);
        });
        return retMap;
    }



    public interface Notification {
        void warningNotify();
    }

}
