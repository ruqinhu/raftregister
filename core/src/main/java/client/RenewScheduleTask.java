package client;

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
            System.out.println("RenewScheduleTask +");
            task.run();
        } finally {
            executorService.schedule(this, delaySeconds, TimeUnit.SECONDS);
        }

    }

}
