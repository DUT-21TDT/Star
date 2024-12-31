package com.pbl.star.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class EventListenerConfig {
    @Bean
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Cấu hình Thread Pool
        executor.setCorePoolSize(10);           // Số thread luôn sẵn sàng
        executor.setMaxPoolSize(50);           // Số thread tối đa
        executor.setQueueCapacity(1000);       // Dung lượng hàng đợi chờ tác vụ
        executor.setKeepAliveSeconds(60);      // Thời gian giữ thread nhàn rỗi
        executor.setThreadNamePrefix("Async-"); // Tiền tố tên thread

        // Đảm bảo executor chờ khi shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        executor.initialize();
        return executor;
    }
}
