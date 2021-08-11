package util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RenewScheduleTask implements Runnable{

    private final ScheduledExecutorService executorService;

    private final Integer delaySeconds;

    private final Runnable task;

    public RenewScheduleTask(ScheduledExecutorService executorService, Integer delaySeconds, Runnable task) {
        this.executorService = executorService;
        this.delaySeconds =  delaySeconds;
        this.task = task;
    }

    @Override
    public void run() {
        try {
//            System.out.println("RenewScheduleTask +");
            task.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.schedule(this, delaySeconds, TimeUnit.SECONDS);
        }

    }

    public static void singleThreadRenew(Runnable runnable, int renewTimeout) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        RenewScheduleTask renewScheduleTask = new RenewScheduleTask(executorService, renewTimeout, runnable);
        renewScheduleTask.run();
    }


    public static void main(String[] args) throws InterruptedException {
        RenewScheduleTask.singleThreadRenew(() -> System.out.println("++++++++++++++++++++++++"), 1);
        Thread.sleep(200000);
    }

}
