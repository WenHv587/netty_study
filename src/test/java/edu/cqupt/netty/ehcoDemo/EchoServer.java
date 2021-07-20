package edu.cqupt.netty.ehcoDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author LWenH
 * @create 2021/7/20 - 16:53
 */
@Slf4j
public class EchoServer {
    public static void main(String[] args) throws InterruptedException {
        new EchoServer().start();
    }

    private void start() throws InterruptedException {
        EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap boot = new ServerBootstrap();

        boot.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(8080))
                .childHandler(serverHandler);
        boot.bind().sync();
    }
}
