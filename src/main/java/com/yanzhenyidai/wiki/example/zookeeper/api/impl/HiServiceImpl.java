package com.yanzhenyidai.wiki.example.zookeeper.api.impl;

import com.yanzhenyidai.wiki.example.zookeeper.api.HiService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author frank
 * @version 1.0
 * @date 2020-04-07 17:33
 */
public class HiServiceImpl implements HiService {

    @Override
    public String hello(String name) {
        return "hello " + name;
    }


    public HiServiceImpl() throws Exception {

        String discovery = discovery("com.yanzhenyidai.wiki.example.zookeeper.ZkClient");

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new EpollEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel channel) throws Exception {
                channel.pipeline();
            }
        });
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        // 连接 RPC 服务器
        ChannelFuture future = bootstrap.connect("127.0.0.1", 1000).sync();
        // 写入 RPC 请求数据并关闭连接
        Channel channel = future.channel();
//        channel.writeAndFlush(request).sync();
        channel.closeFuture().sync();


    }

    private static String ZK_ADDRESS = "localhost:2181";
    private static int SESSION_TIME = 3000;
    private static int ZK_TIMEOUT = 6000;

    private static String parent = "/hi";

    private static Logger logger = LoggerFactory.getLogger(HiServiceImpl.class);


    public static String discovery(String serverName) throws Exception {

        String discoveryAddress = parent + "/" + serverName;

        org.I0Itec.zkclient.ZkClient zkClient = new org.I0Itec.zkclient.ZkClient(ZK_ADDRESS, SESSION_TIME, ZK_TIMEOUT);

        if (!zkClient.exists(discoveryAddress)) {
            logger.error("discovery - server not exist");
            throw new Exception("server not exist");
        }

        List<String> children = zkClient.getChildren(discoveryAddress);
        if (children.isEmpty()) {
            logger.error("discovery - server dont' have nodes");
            throw new Exception("server dont' have nodes");
        }

        logger.info("discovery - server address is :" + children.get(0));
        return zkClient.readData(discoveryAddress + "/" + children.get(0));
    }

}
