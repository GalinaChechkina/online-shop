package de.ait.os.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 8/28/2024
 * OnlineShop
 *
 * @author Chechkina (AIT TR)
 */
@Configuration
@ConfigurationProperties(prefix = "s3")
@Data
public class S3ConfigurationProperties {

    private String accessKey;

    private String secretKey;

    private String endpoint;

    private String region;
}
