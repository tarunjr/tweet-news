package org.tarun.learning.tweetnews.trends;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.tarun.learning.tweetnews.trends.service.ExtractionServiceConnectionFactory;


@Profile("local")
@Configuration
public class LocalConfig {
  @Value(value="${spring.redis.host:127.0.0.1}")
  String redisHost;

  @Value("${spring.redis.port:6379}")
  Integer redisPort;

  @Bean
  JedisConnectionFactory connectionFactory() {
      JedisConnectionFactory cf =  new JedisConnectionFactory();
      cf.setHostName(redisHost);
      cf.setPort(redisPort);
      cf.afterPropertiesSet();
      return cf;
  }

  @Value(value="${tn.extraction.host:127.0.0.1}")
  String extractionHost;

  @Value("${tn.extraction.port:8000}")
  Integer extractionPort;

  @Value("${tn.extraction.protocol:tcp}")
  String extractionProtocol;

  @Bean
  ExtractionServiceConnectionFactory extactionServiceFactory() {
    ExtractionServiceConnectionFactory cf = new ExtractionServiceConnectionFactory();
      cf.setHost(extractionHost);
      cf.setPort(extractionPort);
      cf.setProtocol(extractionProtocol);

    return cf;
  }
}
