package edu.cqupt.netty.eventLoop;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @author LWenH
 * @create 2021/7/15 - 22:59
 */
@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        // 创建 EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup(2);
        // 获取下一个事件循环对象
        for (int i = 0; i < 4; i++) {
            System.out.println(group.next());
        }
        // 执行普通任务
        group.next().execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("普通任务执行");
        });

        // 执行定时任务
        group.next().scheduleAtFixedRate(() -> log.debug("异步任务执行"),0, 1, TimeUnit.SECONDS);
    }
}
