package ru.mirea.dms.storage.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("http://minio:9000") private String endpoint;
    @Value("63UDU4sSMgtG8pjS754i") private String accessKey;
    @Value("5vtaetOewoevIj7uDXp2W7MR1EF5EFHr6x2GZZfI") private String secretKey;
    
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }
}
