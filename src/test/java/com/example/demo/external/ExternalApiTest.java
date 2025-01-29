package com.example.demo.external;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class ExternalApiTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@Autowired
    @Autowired
    private ExternalApi externalApi;

    @Autowired
    private RestTemplate restTemplate;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("post")
    @Nested
    class Method1 {

        private Map<String, Object> requestBody = null;

        private MockRestServiceServer mockServer;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            requestBody = new HashMap<>();

            mockServer = MockRestServiceServer.createServer(restTemplate);
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            mockServer.expect(requestTo("http://localhost:8081/logging"))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(withSuccess("""
                            {"message": "OK"}
                            """, MediaType.APPLICATION_JSON));

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生しないこと
            Map<String, Object> responseBody = externalApi.post("http://localhost:8081/logging", requestBody);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(responseBody).isNotNull();
            assertThat(responseBody).hasFieldOrPropertyWithValue("message", "OK");
        }

    }

}
