package edu.cqupt.netty.eventLoop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author LWenH
 * @create 2021/7/19 - 22:27
 */
@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture connectFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080));

        // 同步处理 connect()方法是异步的 获取到的channel也许还没有建立起连接，因此发不出去消息
//        connectFuture.sync();
//        Channel channel = connectFuture.channel();
//        log.debug("channel={}", channel);
//        channel.writeAndFlush("hello, eventLoopGroup");

        // 监听器回调处理
        connectFuture.addListener(new ChannelFutureListener() {
            // 这个方法会由nio线程调用，而不是主线程。
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                // connectFuture和参数中的future是同一个对象
                log.debug("是否相同？={}", connectFuture == future);
                Channel channel = future.channel();
                log.debug("channel={}", channel);
                channel.writeAndFlush("hello, eventLoopGroup");
            }
        });
    }
}
