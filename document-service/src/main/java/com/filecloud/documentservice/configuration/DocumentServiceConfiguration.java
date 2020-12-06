package com.filecloud.documentservice.configuration;

import com.filecloud.documentservice.properties.DocumentServiceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
@EnableConfigurationProperties({
        DocumentServiceProperties.class
})
public class DocumentServiceConfiguration {

    @LoadBalanced
    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    public Validator getValidator() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        return factory.getValidator();
//    }
}
