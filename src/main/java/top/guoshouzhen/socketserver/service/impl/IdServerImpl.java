package top.guoshouzhen.socketserver.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.guoshouzhen.socketserver.infrastructure.SnowFlake;
import top.guoshouzhen.socketserver.infrastructure.sockets.SocketEngine;
import top.guoshouzhen.socketserver.model.ErrorCodeEnum;
import top.guoshouzhen.socketserver.model.Result;
import top.guoshouzhen.socketserver.service.ISocketServer;
import top.guoshouzhen.socketserver.utils.StringUtil;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 分布式id生成服务
 * @date 2022/5/21 23:28
 */
public class IdServerImpl implements ISocketServer {
    private static final Logger logger = LoggerFactory.getLogger(IdServerImpl.class);

    /**
     * 启动服务
     * @author shouzhen.guo
     * @date 2022/5/21 23:31
     */
    @Override
    public void start() {
        SocketEngine socketEngine = new SocketEngine(55666);

        //生成用户中心用户表的主键id
        socketEngine.register("100", strReceiveData -> {
            if(StringUtil.isNullOrWhiteSpace(strReceiveData)){
                return Result.fail(ErrorCodeEnum.A0001);
            }
            try{
                String[] aryArgs = strReceiveData.split(StringUtil.SPLIT_16);
                String strTableName = aryArgs[0];
                long dataId = 0L;
                switch (strTableName){
                    //用户表
                    case "t_user":
                        dataId = 1L;
                        break;
                    //博客表
                    case "t_blog":
                        dataId = 2L;
                        break;
                    default:
                        break;
                }
                Long id = new SnowFlake(0L, dataId).getNextId();
                return Result.success(id);
            }catch (Exception ex){
                logger.error("生成id失败，接收参数：{}，错误信息：{}", strReceiveData, ex.getMessage());
                return Result.fail(ErrorCodeEnum.A0000);
            }
        });

        //启动服务，监听端口
        socketEngine.run();
    }
}
