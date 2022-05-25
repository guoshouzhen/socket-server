package top.guoshouzhen.socketserver.infrastructure.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.guoshouzhen.socketserver.utils.StaticPropertiesUtil;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 集中管理线程池，只创建一个
 * @date 2022/5/22 2:36
 */
public class ThreadPoolHolder {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolHolder.class);

    private static final ThreadPoolExecutor THREAD_POOL;

    static{
        int coreSize = Integer.parseInt(StaticPropertiesUtil.getValue("threadpool.coresize"));
        int maxSize = Integer.parseInt(StaticPropertiesUtil.getValue("threadpool.maxsize"));
        int maxqueue = Integer.parseInt(StaticPropertiesUtil.getValue("threadpol.maxqueue"));
        THREAD_POOL = new CustomizedThreadPoolExecutor(coreSize, maxSize, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>(maxqueue));
        logger.debug("线程池初始化完毕");
    }

    /**
     * 创建线程池，用来处理任务
     * @author shouzhen.guo
     * @date 2022/5/21 22:30
     */
    public static ThreadPoolExecutor getThreadPool(){
        return THREAD_POOL;
    }
}
