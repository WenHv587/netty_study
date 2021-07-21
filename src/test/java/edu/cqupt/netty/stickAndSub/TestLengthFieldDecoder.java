package edu.cqupt.netty.stickAndSub;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author LWenH
 * @create 2021/7/21 - 16:15
 *
 * 测试
 */
public class TestLengthFieldDecoder {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4),
                new LoggingHandler(LogLevel.DEBUG)
        );

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        build(buf, "hello, world!");
        build(buf, "h1");
        channel.writeInbound(buf);
    }

    private static void build(ByteBuf buf, String content) {
        buf.writeInt(content.length());
        buf.writeBytes(content.getBytes());
    }
}
