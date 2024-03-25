package xyz.kbws.config;

import io.github.briqt.spark4j.SparkClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kbws
 * @date 2024/3/25
 * @description: 星火大模型 API 配置
 */
@Configuration
@ConfigurationProperties(prefix = "xunfei.client")
@Data
public class XingHuoConfig {
    private String appid;

    private String apiSecret;

    private String apiKey;

    @Bean
    public SparkClient sparkClient() {
        SparkClient sparkClient = new SparkClient();
        sparkClient.apiKey = apiKey;
        sparkClient.apiSecret = apiSecret;
        sparkClient.appid = appid;
        return sparkClient;
    }
}
