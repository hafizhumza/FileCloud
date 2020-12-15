package com.filecloud.uiservice.configuration;

import com.filecloud.uiservice.properties.UiServiceProperties;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableConfigurationProperties({
        UiServiceProperties.class
})
public class UiServiceConfiguration {

    @LoadBalanced
    @Bean
    @Primary
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * This configuration is for x-www-form-urlencoded type requests.
     *
     * @param converters Http message converters
     * @return Feign encoder bean which will encode map to x-www-form-urlencoded type requests.
     */
    @Bean
    Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }

}
