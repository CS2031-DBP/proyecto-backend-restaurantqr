package com.example.proydbp.configuration;

import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3ClientConfig {
    //     @Value("${amazonS3.accessKey}")
    //    private String accessKey;
    //
    //    @Value("${amazonS3.secretKey}")
    //    private String secretKey;
    //
    //    @Value("${amazonS3.sessionToken}")
    //    private String accesSessionToken;
    //
    //    @Value("${amazonS3.region}")
    //    private String region;
    //
    //    @Bean
    //    public AmazonS3 getAmazonS3Client() {
    //        final var basicSessionCredentials = new BasicSessionCredentials(accessKey, secretKey, accesSessionToken);
    //
    //        return AmazonS3ClientBuilder
    //                .standard()
    //                .withRegion(String.valueOf(RegionUtils.getRegion(region)))
    //                .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
    //                .build();
    //    }

}
