package edu.cqupt.netty.protocol;

import edu.cqupt.chatDemo.message.LoginRequestMessage;
import edu.cqupt.chatDemo.protocol.MessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author LWenH
 * @create 2021/7/21 - 20:26
 * <p>
 * 测试聊天业务中的自定义编码器
 */
public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(),
                // 遇到分包情况时，会读取错误。因此需要添加基于长度字段的帧解码器
                new LengthFieldBasedFrameDecoder(1024,12, 4, 0, 0),
                new MessageCodec()
        );

        // 测试出站encode
        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("liwenhao", "liwenhao");
//        channel.writeOutbound(loginRequestMessage);

        // 测试入站decode
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        // 调用编码方法为buffer填充内容
        new MessageCodec().encode(null, loginRequestMessage, buffer);
//        channel.writeInbound(buffer);

        // 测试分包 必须要使用 LengthFieldBasedFrameDecoder 来防止分包
        ByteBuf s1 = buffer.slice(0, 100);
        ByteBuf s2 = buffer.slice(100, buffer.readableBytes() - 100);
        s1.retain(); // writeInbound()会使引用计数-1，造成buffer第二次不可用。
        channel.writeInbound(s1); // release 1
        channel.writeInbound(s2);
    }
}
