package com.example.async.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {


    /**
     *  비동기 작업을 수행할 쓰레드 풀을 설정함
     *  @Async 어노테이션이 적용된 메서드에서 비동기 작업을 실행할 때 이 쓰레드 풀이 사용됨
     **/
    @Bean(name = "taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);    // 쓰레드 풀이 유지할 최소 쓰레드 수 ........................... default: 1
        executor.setMaxPoolSize(8);     // 쓰레드 풀이 생성할 수 있는 최대 쓰레드 수 ..................... default: Integer.MAX_VALUE
        executor.setQueueCapacity(100); // 쓰레드 풀이 작업을 바로 처리하지 못하는 경우를 대비한 대기 큐 설정 ... default: Integer.MAX_VALUE
        executor.setThreadNamePrefix("imgThread"); // 쓰레드 이름의 접두사 설정 ....................... default: taskExecutor-
        executor.initialize(); // 쓰레드 풀 초기화
        return executor;
    }
}
