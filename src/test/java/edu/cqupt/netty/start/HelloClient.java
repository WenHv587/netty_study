package edu.cqupt.netty.start;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author LWenH
 * @create 2021/7/15 - 21:09
 *
 * 客户端
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        // 启动器
        new Bootstrap()
                // 添加EventLoopGroup
                .group(new NioEventLoopGroup())
                // 选择客户端的SocketChannel实现
                .channel(NioSocketChannel.class)
                // 初始化channel，添加别的处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override // 连接建立后调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 连接到服务器
                .connect(new InetSocketAddress("localhost", 8080))
                // 阻塞 直到连接建立
                .sync()
                // 得到连接的通道
                .channel()
                // 发送数据
                .writeAndFlush("hello, netty!");
    }
}
