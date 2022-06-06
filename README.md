# socket-server

#### 介绍
一个快速开发socket接口的简单框架，内含一个分布式id生成服务，基于雪花算法，通过socket调用。

#### 程序结构说明
```
socket-server
│  .gitignore
│  Dockerfile -------------dockerfile
│  pom.xml ----------------pom文件
│  
└─src
    ├─main
    │  ├─java
    │  │  └─top
    │  │      └─guoshouzhen
    │  │          └─socketserver
    │  │              │  SocketServerApplication.java -------主类，程序入口
    │  │              │  
    │  │              ├─infrastructure ----------------------基础架构层
    │  │              │  │  SnowFlake.java ------------------雪花算法实现
    │  │              │  │  
    │  │              │  ├─sockets --------------------------socket封装层
    │  │              │  │      ISocketHandler.java ---------函数式接口，约定实际处理socket请求的方式，用于注册处理方法
    │  │              │  │      RunnableImpl.java -----------Runnable接口的实现类，线程所执行的任务
    │  │              │  │      SocketEngine.java -----------监听socket请求的封装，持有一个线程池和一个Handler Map，前者用于处理请求的任务，后者根据用户注册的handler方法处理请求数据，并返回结果
    │  │              │  │      
    │  │              │  └─threads
    │  │              │          CustomizedThreadPoolExecutor.java --ThreadPoolExecutor子类，重写部分方法，添加自定义功能
    │  │              │          ThreadPoolHolder.java -------单列模式，全局创建一个线程池，集中管理
    │  │              │          
    │  │              ├─model --------------------------------model类定义
    │  │              │      ErrorCodeEnum.java --------------错误代码枚举类
    │  │              │      Result.java ---------------------接口结果规范定义
    │  │              │      
    │  │              ├─service ------------------------------服务层，具体服务实现
    │  │              │  │  ISocketServer.java ---------------服务接口约定
    │  │              │  │  
    │  │              │  └─impl
    │  │              │          IdServerImpl.java -----------分布式id服务实现
    │  │              │          
    │  │              └─utils
    │  │                      IntegerUtil.java ---------------Integer工具类
    │  │                      JacksonUtil.java ---------------Json工具类
    │  │                      StaticPropertiesUtil.java ------配置文件工具类
    │  │                      StringUtil.java ----------------String工具类
    │  │                      
    │  └─resources
    │          application.properties ------------------------程序配置文件
    │          logback.xml -----------------------------------日志配置文件
    │          
    └─test
        └─java
                TestSonwFlake.java ---------------------------单元测试
                
```
#### 框架说明
由于某些业务场景请求的数据比较简单或者性能要求较高，而且又不需要Http请求报文中的请求头等数据，则可以通过socket方式，自定义请求报文格式，减少不必要的数据包，减小通信开销。
##### 报文协定
* **分割符定义**  
split26：(char)26  
split16：(char)16
* **请求报文格式**  
	长度 + 正文
	- 长度：占用4个字节，为请求报文正文的字节长度，不包括表示长度的四个字节（字节序列大端方式存储）
	- 正文：methodId + split26 + 请求参数1 + split16 + 请求参数2...
* **响应报文格式**  
	长度 + 正文
	- 长度：占用4个字节，为响应正文的字节长度，不包括表示长度的四个字节（字节序列大端方式存储）
	- 正文: json字符串  
		格式:
```json
	"result": 1, 		//操作结果，0-成功，1-失败
  	"code": "200", 		//成功时为200，失败时为500或者是自定义的错误码
	"msg": "success", 	//成功为success，失败为failed或者是自定义的错误信息
	"data": null 		//需要返回给client的数据
```

#### 使用说明
* server：实现`ISocketServer`接口，实例化`SocketEngine`，指定监听端口，并注册methodId及其对应的handler，调用SocketEngine实例的`run()`方法运行，并在程序入口中注册该服务服务。  
    示例：
    ```java
  public class MyServerImpl implements ISocketServer {
      /**
       * 启动服务
       * @author shouzhen.guo
       * @date 2022/5/21 23:31
       */
      @Override
      public void start() {
          SocketEngine socketEngine = new SocketEngine(55666);
  
          socketEngine.register("1", strReceiveData -> {
              //TODO ...
              return Result.success();
          });
          
          socketEngine.register("2", strReceiveData -> {
                //TODO ...
                return Result.success();
          });
  
         ...
  
          //启动服务，监听端口
          socketEngine.run();
      }
  }
  
  //主程序类中修改registerServices方法注册新增服务：
  public class SocketServerApplication { 
      ...
   
      /**
       * 注册socket服务
       * @author shouzhen.guo
       * @date 2022/5/21 22:57
       */
      private static void registerServices(){
          //注册分布式id生成服务
          SERVERS.add(new MyServerImpl());
      }
  
      ...
  }
    ```
* client：按照请求报文格式,使用指定分割符拼接请求数据,通过socket client发送请求数据数据,并接收返回数据。
#### 部署说明
* docker容器部署:
	- 使用`mvn clean compile assembly:single`命令打成一个jar包
	- 使用dockerfile-maven插件进行build,构建镜像
	- 使用dockerfile-maven插件进行push,推送到远程镜像仓库
	- 在服务器上拉取镜像,并运行
	- 运行示例:
		```
		docker run --name socket-server  \
		-p 55666:55666 \
		--net commonnetwork \
		--ip 172.18.0.20 \
		-v /root/appdata/blog/logs/socketserver:/root/appdata/blog/logs/socketserver \
		-d registry.cn-hangzhou.aliyuncs.com/blog-regs/socket-server
		```
