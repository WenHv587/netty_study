package edu.cqupt.netty.start;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author LWenH
 * @create 2021/7/15 - 21:09
 * <p>
 * netty 服务端
 */
public class HelloServer {
    public static void main(String[] args) {
        // 启动器 负责组装netty组件 启动服务器
        new ServerBootstrap()
                // EventLoop 包含 Selector 和 thread
                .group(new NioEventLoopGroup())
                // 选择服务器 ServerSocketChannel 的实现  有NIO OIO(BIO)
                .channel(NioServerSocketChannel.class)
                // boss 负责处理连接 worker(child) 负责处理读写，决定了 worker(child) 能执行哪些操作（handler）
                .childHandler(
                    // channel是服务端和客户端进行数据读写的通道。Initializer做初始化，负责添加别的handler
                    new ChannelInitializer<NioSocketChannel>() {
                        @Override // 不会立即初始化执行 在连接建立以后才会被调用
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            // 添加具体handler
                            // 将ByteBuf转换为String
                            ch.pipeline().addLast(new StringDecoder());
                            // 使用Adapter自定义handler
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                // 处理读事件
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println(msg);
                                }
                            });
                        }
                }).bind(8080);
    }
}
