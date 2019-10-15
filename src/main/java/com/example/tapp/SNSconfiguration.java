package com.example.tapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.example.tapp.common.utils.YamlPropertySourceFactory;

@Configuration
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:utils.yml")
public class SNSconfiguration {


    @Value("${tapp.application.snsconfiguration.accesskey}")
    private String ACCESS_KEY;

    @Value("${tapp.application.snsconfiguration.secretkey}")
    private String SECRET_KEY;

    @Value("${tapp.application.snsconfiguration.region}")
    private String REGION;

    @Bean
    public AmazonSNS amazonSNS() {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonSNS amazonSNS = AmazonSNSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(REGION).build();
        return amazonSNS;
    }
}