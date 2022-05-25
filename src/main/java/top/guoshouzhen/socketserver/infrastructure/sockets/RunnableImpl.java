package top.guoshouzhen.socketserver.infrastructure.sockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.guoshouzhen.socketserver.model.Result;
import top.guoshouzhen.socketserver.utils.IntegerUtil;
import top.guoshouzhen.socketserver.utils.JacksonUtil;
import top.guoshouzhen.socketserver.utils.StringUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 处理socket的线程类
 * @date 2022/3/27 19:07
 */
public class RunnableImpl implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RunnableImpl.class);

    /**
     * 要处理的socket实例
     */
    private final Socket socket;
    /**
     * 具体处理函数表
     */
    private final Map<String, ISocketHandler> handlerMap;

    public RunnableImpl(Socket socket, Map<String, ISocketHandler> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;
        String strMethodId = "";
        String strReceiveData = "";

        try {
            if (this.socket == null) {
                throw new Exception("Socket handled must be not null");
            }
            //读取数据
            is = this.socket.getInputStream();
            //读取数据长度
            byte[] bytesLen = new byte[4];
            int n = is.read(bytesLen);
            //接受到数据才处理
            if (n != -1) {
                //实际有效报文长度
                int len = IntegerUtil.toInt(bytesLen);
                byte[] inputData = new byte[len];
                n = is.read(inputData);
                //关闭socket输入流
                this.socket.shutdownInput();
                if (n != -1) {
                    String strInputString = new String(inputData, StandardCharsets.UTF_8);
                    String[] aryData = strInputString.split(StringUtil.SPLIT_26);
                    strMethodId = aryData[0];
                    strReceiveData = aryData[1];
                    //调用函数处理
                    ISocketHandler handler = handlerMap.get(strMethodId);
                    Result result;
                    if (handler != null) {
                        result = handler.handle(strReceiveData);
                    } else {
                        //404，定义json格式
                        result = Result.fail("404", "not find such method");
                    }

                    os = this.socket.getOutputStream();
                    response(os, result);
                    //关闭socket输出流
                    this.socket.shutdownOutput();
                }
            }

        } catch (Exception ex) {
            logger.error("处理Socket连接时发生异常，异常信息{}，方法id：{}，接收数据：{}", ex, strMethodId, strReceiveData);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (Exception ex) {
                logger.error("socket关闭连接失败");
            }

        }
    }

    /**
     * 将处理结果写入socket输出流
     *
     * @param os     socket输出流
     * @param result 结果
     * @author shouzhen.guo
     * @date 2022/5/21 22:50
     */
    private static void response(OutputStream os, Result result) throws Exception {
        if (os == null) {
            return;
        }
        //写入数据
        String jsonData = JacksonUtil.obj2Json(result);
        byte[] aryReturnData = jsonData.getBytes(StandardCharsets.UTF_8);
        byte[] aryLen = IntegerUtil.toByteAry(aryReturnData.length);
        int rLen = aryReturnData.length + 4;
        byte[] aryAllResult = new byte[rLen];

        System.arraycopy(aryLen, 0, aryAllResult, 0, 4);
        System.arraycopy(aryReturnData, 0, aryAllResult, 4, aryReturnData.length);

        os.write(aryAllResult);
    }
}
