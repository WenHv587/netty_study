package edu.cqupt.netty.eventLoop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author LWenH
 * @create 2021/7/19 - 22:14
 */
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        // 创建一个独立的 EventExecutorGroup, 用于执行耗时较长的任务
        DefaultEventExecutorGroup eventExecutors = new DefaultEventExecutorGroup(3);
        new ServerBootstrap()
                // 明确分工 第一个是服务器的专门用来建立连接的EventLoopGroup 只接受Accept事件。第二个是服务器和客户端读写操作时的EventLoopGroup
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.info(buf.toString(Charset.defaultCharset()));
                                // 触发对下一个handler的channelRead()方法的调用
                                ctx.fireChannelRead(msg);
                            }
                        });

                        /*
                            如果对于某个时间的处理非常耗时，为了防止EventLoop线程的阻塞，影响其他channel
                            将这个事件交给一个另外的线程的去处理。
                            此时这个事件就会从该channel本身的EventLoop中移除，这样就可以执行其他channel的事件了。
                         */
                        ch.pipeline().addLast(eventExecutors,"handler2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.info(buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                }).bind(8080);
    }
}
