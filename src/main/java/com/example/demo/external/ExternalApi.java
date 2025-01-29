package com.example.demo.external;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

/**
 * 外部API呼び出し処理。
 */
@RequiredArgsConstructor
@Component
public class ExternalApi {

    /** RestTemplate */
    private final RestTemplate restTemplate;

    /**
     * POSTで通信。
     * 
     * @param url URL
     * @param body リクエストのボディ
     * @return レスポンスのボディ
     */
    public Map<String, Object> post(String url, Map<String, Object> body) {
        // ヘッダー
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // リクエストエンティティ
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // レスポンスタイプ
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<>() {
        };

        // 通信実行
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, entity,
                responseType);

        // レスポンスのボディ取得
        return response.getBody();
    }

}
