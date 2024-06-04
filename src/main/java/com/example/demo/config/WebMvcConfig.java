package com.example.demo.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(10000);
        httpRequestFactory.setConnectTimeout(10000);
//        httpRequestFactory.setReadTimeout(10000);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        return new RestTemplateBuilder().additionalMessageConverters(messageConverters).build();
    }

//    @Bean
//    public RetryTemplate retryTemplate() {
//        RetryTemplate retryTemplate = new RetryTemplate();
//        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
//        fixedBackOffPolicy.setBackOffPeriod(1001);
//        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
//        Map<Class<? extends Throwable>, Boolean> exceptions = new HashMap<>();
//        exceptions.put(RestClientException.class, v true);
//        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts:2, exceptions);
//        retryTemplate.setRetry Policy (retryPolicy);
//        return retryTemplate;
//    }
}
