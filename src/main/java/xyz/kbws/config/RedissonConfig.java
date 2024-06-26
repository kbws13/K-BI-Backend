package xyz.kbws.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kbws
 * @date 2024/4/14
 * @description: RedissonConfig 配置类，用于初始化 RedissonClient 对象单例
 */
@ConfigurationProperties(prefix = "spring.redis")
@Configuration
@Data
public class RedissonConfig {

    private Integer database;

    private String host;

    private Integer port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setDatabase(database)
                .setAddress("redis://" + host + ":" + port);
        return Redisson.create(config);
    }
}
