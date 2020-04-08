package com.yanzhenyidai.wiki.example.zookeeper;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author frank
 * @version 1.0
 * @date 2020-04-07 17:02
 */
public class ZookeeperRegister {

    private static String ZK_ADDRESS = "localhost:2181";
    private static int SESSION_TIME = 3000;
    private static int ZK_TIMEOUT = 6000;

    private static String parent = "/hi";

    private Map<String, Object> handlerMap = new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(ZookeeperRegister.class);


    public static void main(String[] args) throws Exception {
        register("com.yanzhenyidai.wiki.example.zookeeper.ZkClient");

        Thread.currentThread().join();
    }

    public static void register(String serverName) throws Exception {

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup());
        bootstrap.channel(NioServerSocketChannel.class);

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline();
//                ChannelPipeline pipeline = socketChannel.pipeline();
//                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                pipeline.addLast(new LengthFieldPrepender(4));
//                pipeline.addLast(new ObjectEncoder());
//                pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
//                pipeline.addLast(new ChannelInboundHandlerAdapter());
            }
        });

        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        org.I0Itec.zkclient.ZkClient zkClient = new org.I0Itec.zkclient.ZkClient(ZK_ADDRESS, SESSION_TIME, ZK_TIMEOUT);

        ChannelFuture future = bootstrap.bind("127.0.0.1", 1000).sync();

        if (!zkClient.exists(parent)) {
            zkClient.createPersistent(parent);
        }

        String registerAddress = parent + "/" + serverName;

        if (zkClient.exists(registerAddress)) {
            logger.error("register - server already exist");
            throw new Exception("server already exist");
        }

        zkClient.createPersistent(registerAddress);
        logger.info("register persistent-node successfully : " + registerAddress);

        zkClient.createEphemeralSequential(registerAddress + "/127.0.0.1:1000", "127.0.0.1:1000");
        logger.info("register ephemeral-node successfully : " + registerAddress + "/127.0.0.1:1000");

//        future.channel().close();
    }

}
