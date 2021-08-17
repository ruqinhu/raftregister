package util;

import java.util.concurrent.TimeUnit;

public class MetricScheduleThreadPoolExecutorTest {

    public static void main(String[] args) throws InterruptedException {
        final MetricScheduleThreadPoolExecutor executor = new MetricScheduleThreadPoolExecutor("renew", 8, null);
        executor.scheduleAtFixedRate(()-> System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++"), 2, 2, TimeUnit.SECONDS);
        Thread.sleep(111111111);
    }

}
