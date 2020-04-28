package com.gmsj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@MapperScan({"com.gmsj.dao"})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        MediaType[] mediaTypeJson = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8, MediaType.TEXT_PLAIN};
        messageConverter.setSupportedMediaTypes(Arrays.asList(mediaTypeJson));

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(messageConverter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }
}
