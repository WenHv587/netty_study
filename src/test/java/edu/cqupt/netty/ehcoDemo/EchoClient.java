package edu.cqupt.netty.ehcoDemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author LWenH
 * @create 2021/7/20 - 16:53
 */
@Slf4j
public class EchoClient {

    public static void main(String[] args) throws InterruptedException {
        new EchoClient().start();
    }

    private void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        EchoClientHandler clientHandler = new EchoClientHandler();

        Bootstrap boot = new Bootstrap();
        ChannelFuture channelFuture = boot.group(group)
                .channel(NioSocketChannel.class)
                .handler(clientHandler)
                .connect("localhost", 8080).sync();
        ChannelFuture closeFuture = channelFuture.channel().closeFuture();

        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.debug("关闭之后的操作");
                group.shutdownGracefully();
            }
        });

    }
}
