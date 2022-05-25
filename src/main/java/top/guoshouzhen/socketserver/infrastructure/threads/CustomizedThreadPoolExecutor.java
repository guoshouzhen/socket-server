package top.guoshouzhen.socketserver.infrastructure.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 自定义线程池，在ThreadPoolExecutor的基础上加些功能
 * @date 2022/5/21 19:10
 */
public class CustomizedThreadPoolExecutor extends ThreadPoolExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CustomizedThreadPoolExecutor.class);

    private final ThreadLocal<Long> thlStartTime = new ThreadLocal<>();
    private final AtomicLong lTaskNums = new AtomicLong();
    private final AtomicLong lTotalTime = new AtomicLong();

    public CustomizedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }



    /**
     * 任务执行前
     * @author shouzhen.guo
     * @date 2022/5/21 22:14
     * @param t thread
     * @param r runable
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        thlStartTime.set(System.nanoTime());
    }

    /**
     * 任务执行后
     * @author shouzhen.guo
     * @date 2022/5/21 22:18
     * @param r r
     * @param t t
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try{
            long endTime = System.nanoTime();
            long taskCostTime = endTime - thlStartTime.get();
            lTaskNums.incrementAndGet();
            lTotalTime.addAndGet(taskCostTime);
            //当前线程本地变量清除
            thlStartTime.remove();
            if(t != null){
                logger.error("处理任务发生异常，异常信息：", t);
            }
        }
        finally {
            super.afterExecute(r, t);
        }
    }

    @Override
    protected void terminated() {
        try{
            logger.info("线程池终止，任务执行平均时间：{}ms", (lTotalTime.get() / lTaskNums.get()) / 1000000);
        }
        finally {
            super.terminated();
        }
    }
}
