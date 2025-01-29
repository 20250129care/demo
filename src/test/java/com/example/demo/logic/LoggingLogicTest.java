package com.example.demo.logic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.example.demo.constant.URLConstants;
import com.example.demo.external.ExternalApi;

// NOTE: Componentのテストは処理の内容が正しく行われるかだけに観点を置く

@ExtendWith(MockitoExtension.class)
class LoggingLogicTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private LoggingLogic externalApiLogic;

    // NOTE: 呼び出されるクラスは@Mock
    // NOTE: Springの設定を読み込んでしまうため@MockitBeanは使わない
    @Mock
    private LoggingLogicConverter converter;

    @Mock
    private MessageSource messageSource;

    @Mock
    private ExternalApi externalApi;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("logOperation")
    @Nested
    class Method1 {

        private static final String URL = URLConstants.LOGGING_URL;
        private static final String OPERATION = "テスト処理";
        private static final String OPERATOR = "OPERATOR";

        private Map<String, Object> requestBody = null;
        private Map<String, Object> responseBody = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            requestBody = new HashMap<>();
            responseBody = new HashMap<>();
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(requestBody)
                    .when(converter)
                    .convertToBody(anyString(), anyString());

            doReturn(responseBody)
                    .when(externalApi)
                    .post(anyString(), any());

            doReturn("テストメッセージ")
                    .when(messageSource)
                    .getMessage(anyString(), isNull(), any());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生しないこと
            assertThatCode(() -> externalApiLogic.logOperation(OPERATION, OPERATOR)).doesNotThrowAnyException();

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToBody(eq(OPERATION), eq(OPERATOR));
            verify(externalApi, times(1)).post(eq(URL), eq(requestBody));
            verify(messageSource, times(1)).getMessage(eq("I01"), isNull(), eq(Locale.JAPAN));
        }

        @DisplayName("異常終了:HttpClientErrorException")
        @Test
        void testNG1() {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(requestBody)
                    .when(converter)
                    .convertToBody(anyString(), anyString());

            doThrow(HttpClientErrorException.class)
                    .when(externalApi)
                    .post(anyString(), any());

            doReturn("テストメッセージ")
                    .when(messageSource)
                    .getMessage(anyString(), isNull(), any());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生しないこと
            assertThatCode(() -> externalApiLogic.logOperation(OPERATION, OPERATOR)).doesNotThrowAnyException();

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToBody(eq(OPERATION), eq(OPERATOR));
            verify(externalApi, times(1)).post(eq(URL), eq(requestBody));
            verify(messageSource, times(1)).getMessage(eq("W01"), isNull(), eq(Locale.JAPAN));
        }

        @DisplayName("異常終了:HttpServerErrorException")
        @Test
        void testNG2() {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(requestBody)
                    .when(converter)
                    .convertToBody(anyString(), anyString());

            doThrow(HttpServerErrorException.class)
                    .when(externalApi)
                    .post(anyString(), any());

            doReturn("テストメッセージ")
                    .when(messageSource)
                    .getMessage(anyString(), isNull(), any());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生しないこと
            assertThatCode(() -> externalApiLogic.logOperation(OPERATION, OPERATOR)).doesNotThrowAnyException();

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToBody(eq(OPERATION), eq(OPERATOR));
            verify(externalApi, times(1)).post(eq(URL), eq(requestBody));
            verify(messageSource, times(1)).getMessage(eq("W02"), isNull(), eq(Locale.JAPAN));
        }

        @DisplayName("異常終了:ResourceAccessException")
        @Test
        void testNG3() {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(requestBody)
                    .when(converter)
                    .convertToBody(anyString(), anyString());

            doThrow(ResourceAccessException.class)
                    .when(externalApi)
                    .post(anyString(), any());

            doReturn("テストメッセージ")
                    .when(messageSource)
                    .getMessage(anyString(), isNull(), any());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生しないこと
            assertThatCode(() -> externalApiLogic.logOperation(OPERATION, OPERATOR)).doesNotThrowAnyException();

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToBody(eq(OPERATION), eq(OPERATOR));
            verify(externalApi, times(1)).post(eq(URL), eq(requestBody));
            verify(messageSource, times(1)).getMessage(eq("W03"), isNull(), eq(Locale.JAPAN));
        }

        @DisplayName("異常終了:RuntimeException")
        @Test
        void testNG4() {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(requestBody)
                    .when(converter)
                    .convertToBody(anyString(), anyString());

            doThrow(RuntimeException.class)
                    .when(externalApi)
                    .post(anyString(), any());

            doReturn("テストメッセージ")
                    .when(messageSource)
                    .getMessage(anyString(), isNull(), any());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生しないこと
            assertThatCode(() -> externalApiLogic.logOperation(OPERATION, OPERATOR)).doesNotThrowAnyException();

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToBody(eq(OPERATION), eq(OPERATOR));
            verify(externalApi, times(1)).post(eq(URL), eq(requestBody));
            verify(messageSource, times(1)).getMessage(eq("W99"), isNull(), eq(Locale.JAPAN));
        }

    }

}
