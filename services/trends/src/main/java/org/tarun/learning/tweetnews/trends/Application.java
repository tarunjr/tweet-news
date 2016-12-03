package org.tarun.learning.tweetnews.trends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

import org.tarun.learning.tweetnews.trends.service.CachingService;
import org.tarun.learning.tweetnews.trends.service.RedisCachingService;

@SpringBootApplication
@EnableAsync
public class Application extends AsyncConfigurerSupport {
    @Bean
    public CachingService cachingService() {
      return new RedisCachingService();
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Articles-");
        executor.initialize();
        return executor;
    }
}
