package edu.cqupt.netty.ehcoDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author LWenH
 * @create 2021/7/20 - 17:24
 */
@Slf4j
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("connect successful");
        new Thread(() -> {
            log.debug("请输入消息");
            Scanner sc = new Scanner(System.in);
            while (true) {
                String input = sc.nextLine();
                if ("quit".equals(input)) {
                    ctx.close();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer(input.getBytes()));
            }
        }).start();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        log.debug("client receive: {}", msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
