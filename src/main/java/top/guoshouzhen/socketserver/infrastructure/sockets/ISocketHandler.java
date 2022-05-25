package top.guoshouzhen.socketserver.infrastructure.sockets;


import top.guoshouzhen.socketserver.model.Result;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 具体处理接口数据并返回结果的处理方法约定
 * @date 2022/3/27 0:02
 */
public interface ISocketHandler {
    /**
     * 具体处理方法
     * @author shouzhen.guo
     * @date 2022/5/21 19:14
     * @param strReceiveData 接手数据
     * @return top.guoshouzhen.idserver.model.Result
     */
    Result handle(String strReceiveData);
}
