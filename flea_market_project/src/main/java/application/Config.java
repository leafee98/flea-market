package application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
@Slf4j
public class Config {
    @Bean(name = "redisTemplate")
    RedisTemplate<String, byte[]> redisTemplate() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName("localhost");
        redisConfiguration.setPort(6379);
        redisConfiguration.setDatabase(0);
        redisConfiguration.setPassword("flea_market");

        JedisClientConfiguration jedisConfiguration = JedisClientConfiguration.builder()
                .usePooling()
                .and()
                .connectTimeout(Duration.ofSeconds(300L))
                .build();

        // JedisConnectionFactory factory = new JedisConnectionFactory(configuration);
        // factory.

        // <bean id="jedisConnectionFactory"
        // class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory"
        // p:use-pool="true" p:database="0" p:host-name="192.168.100.21" p:port="6379"
        // p:timeout="100" />

        JedisConnectionFactory factory = new JedisConnectionFactory(redisConfiguration, jedisConfiguration);

        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }
}
