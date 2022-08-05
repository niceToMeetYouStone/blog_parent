package com.mszlu.blog.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {


    @Bean("taskExecutor")
    public Executor asyncServiceExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心的数目
        executor.setCorePoolSize(5);
        // 设置最大线程数
        executor.setMaxPoolSize(20);
        // 配置队列的大小
        executor.setQueueCapacity(Integer.MAX_VALUE);
        // 设置线程的活跃时间 单位s
        executor.setKeepAliveSeconds(60);
        //设置线程的名称
        executor.setThreadNamePrefix("码神之路");
        // 等待所有的任务完成之后关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 执行初始化
        executor.initialize();
        return executor;
    }
}
