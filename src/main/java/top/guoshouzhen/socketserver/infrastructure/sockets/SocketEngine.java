package top.guoshouzhen.socketserver.infrastructure.sockets;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.guoshouzhen.socketserver.infrastructure.threads.ThreadPoolHolder;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/3/27 0:55
 */
public class SocketEngine {
    private static final Logger logger = LoggerFactory.getLogger(SocketEngine.class);
    /**
     * 运行端口
     */
    private final int port;
    /**
     * 保存处理函数
     */
    private final Map<String, ISocketHandler> mapHandlers;
    /**
     * 线程池
     */
    private static final ThreadPoolExecutor THREAD_POOL;

    static{
        THREAD_POOL = ThreadPoolHolder.getThreadPool();
    }

    /**
     * 构造器
     * @author shouzhen.guo
     * @date 2022/5/21 22:29
     * @param port 端口
     */
    public SocketEngine(int port){
        this.port = port;
        mapHandlers = new HashMap<>();
    }

    /**
     * 注册相应method的处理方法
     * @author shouzhen.guo
     * @date 2022/5/21 22:26
     * @param strMethodId methodid
     * @param handler 处理方法
     */
    public void register(String strMethodId, ISocketHandler handler){
        if(handler != null){
            mapHandlers.put(strMethodId, handler);
        }
    }

    /**
     * 启动socket服务
     * @author shouzhen.guo
     * @date 2022/5/21 22:27
     */
    public void run(){
        try{
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("socket服务启动，正在监听端口：" + this.port);
            while(true){
                Socket s = serverSocket.accept();
                //提交到线程池处理
                THREAD_POOL.execute(new RunnableImpl(s, mapHandlers));
            }
        }catch (Exception ex){
            logger.error("socket服务启动或运行失败, 错误信息: " + ex);
        }
    }
}
