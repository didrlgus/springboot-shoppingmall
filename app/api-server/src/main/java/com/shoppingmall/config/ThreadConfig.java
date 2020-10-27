package com.shoppingmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 *  ThreadPoolTaskExecutor 클래스는 DisposableBean 인터페이스를 구현하는 ExecutorConfigurationSupport를 상속하고 있어서,
 *  스프링 애플리케이션이 종료될 때 ExecutorConfigurationSupport의 .destroy()가 자동으로 호출되면서 스레드가 종료되므로
 *  개발자가 직접 별도의 종료 처리를 해주지 않아도 됨.
 *
 */

@Configuration
public class ThreadConfig {

    // 스레드 관리자
//    @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//
//        threadPoolTaskExecutor.setCorePoolSize(Thread.activeCount());   // 스레드 풀 기본 사이즈 설정
//        threadPoolTaskExecutor.setMaxPoolSize(10);                      // 스레드 풀 최대 사이즈 설정
//        threadPoolTaskExecutor.setThreadNamePrefix("Batch-thread-");
//
//        return threadPoolTaskExecutor;
//    }
}
