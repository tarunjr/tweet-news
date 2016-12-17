package org.tarun.learning.tweetnews.trends;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.tarun.learning.tweetnews.trends.service.ExtractionServiceConnectionFactory;

@Profile("docker")
@Configuration
public class DockerConfig {
    @Value(value="#{systemEnvironment['REDIS_PORT_6379_TCP_ADDR']}")
    String redisHost;

    @Value("#{systemEnvironment['REDIS_PORT_6379_TCP_PORT']}")
    Integer redisPort;

    @Bean
    JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory cf =  new JedisConnectionFactory();
        cf.setHostName(redisHost);
        cf.setPort(redisPort);
        cf.afterPropertiesSet();
        return cf;
    }

    @Value(value="#{systemEnvironment['EXTRACTION_PORT_8000_TCP_ADDR']}")
    String extractionHost;

    @Value("#{systemEnvironment['EXTRACTION_PORT_8000_TCP_PORT']}")
    Integer extractionPort;

    @Value("#{systemEnvironment['EXTRACTION_PORT_8000_TCP_PROTO']}")
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
