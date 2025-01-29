package com.example.demo.logic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

// NOTE: Componentのテストは処理の内容が正しく行われるかだけに観点を置く

@ExtendWith(MockitoExtension.class)
class LoggingLogicConverterTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private LoggingLogicConverter converter;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("convertToBodyFromArgs")
    @Nested
    class Method1 {

        // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
        // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

        private static final LocalDateTime NOW = LocalDateTime.of(2025, 1, 1, 12, 30, 23, 123456789);
        private static final String OPERATION = "テスト処理";
        private static final String OPERATOR = "OPERATOR";
        private static final String OPERATE_DATE = "2025-01-01T12:30:23";

        private static MockedStatic<LocalDateTime> mock = null;

        @BeforeAll
        static void setUpAll() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            // LocalDateTimeをモック化する
            mock = Mockito.mockStatic(LocalDateTime.class);
        }

        @AfterAll
        static void tearDownAll() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            // Mockito.mockStaticでモック化したモックは複数実行したテストのスレッド内で残るため毎回closeする必要がある
            mock.close();
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            mock.when(LocalDateTime::now).thenReturn(NOW);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            Map<String, Object> body = converter.convertToBody(OPERATION, OPERATOR);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(body).containsEntry("operation", OPERATION);
            assertThat(body).containsEntry("operator", OPERATOR);
            assertThat(body).containsEntry("operation_date", OPERATE_DATE);

            // NOTE: 呼び出されるメソッドが想定通りであること
            mock.verify(LocalDateTime::now, times(1));
        }

    }

}
