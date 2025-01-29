package com.example.demo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * RestTemplateのBean設定。
 */
@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplateのBeanを作成する。
     * 
     * @param baseUrl ベースURL
     * @param apiKey APIキー
     * @return RestTemplateのBean
     */
    @Bean
    RestTemplate restTemplate(
            @Value("${external.baseUrl}") String baseUrl,
            @Value("${external.apiKey}") String apiKey) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            request.getHeaders().add("X-Api-Key", apiKey);
            return execution.execute(request, body);
        });

        RestTemplate restTemplate = new RestTemplateBuilder()
                .requestFactory(SimpleClientHttpRequestFactory.class)
                .uriTemplateHandler(new DefaultUriBuilderFactory(baseUrl))
                .additionalInterceptors(interceptors)
                .build();
        return restTemplate;
    }

}
