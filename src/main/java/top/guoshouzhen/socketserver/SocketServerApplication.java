package top.guoshouzhen.socketserver;

import top.guoshouzhen.socketserver.service.ISocketServer;
import top.guoshouzhen.socketserver.service.impl.IdServerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/5/20 16:39
 */
public class SocketServerApplication {
    /**
     * 服务列表
     */
    private static final List<ISocketServer> SERVERS = new ArrayList<>();

    public static void main(String[] args){
        //注册服务
        registerServices();
        //启动服务
        SERVERS.forEach(ISocketServer::start);
    }

    /**
     * TODO 注册socket服务，实现自动化注册
     * @author shouzhen.guo
     * @date 2022/5/21 22:57
     */
    private static void registerServices(){
        //注册分布式id生成服务
        SERVERS.add(new IdServerImpl());
    }
}
