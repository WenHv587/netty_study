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
import java.util.Scanner;

/**
 * @author LWenH
 * @create 2021/7/20 - 14:14
 *
 * 测试close的client
 */
@Slf4j
public class CloseFuture {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080));

        Channel channel = channelFuture.sync().channel();
        log.debug("channel={}", channel);
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String input = sc.nextLine();
                if (input.equals("quit")) {
                    channel.close();
                    // 在这里做关闭之后的操作也是错误的。因为close()方法也是异步的
//                    log.debug("处理关闭之后的操作！");
                    break;
                }
                channel.writeAndFlush(input);
            }
        }, "test").start();

        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener(future -> {
            log.debug("处理关闭之后的操作！");
            // shutdownGracefully()也是异步的，阻塞直到关闭完成。
            group.shutdownGracefully().syncUninterruptibly();
        });
    }
}
