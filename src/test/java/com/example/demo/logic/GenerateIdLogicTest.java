package com.example.demo.logic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

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
class GenerateIdLogicTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private GenerateIdLogic generateIdLogic;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("create")
    @Nested
    class Method1 {

        private static final String ID = "20250101123023123";

        private static final LocalDateTime NOW = LocalDateTime.of(2025, 1, 1, 12, 30, 23, 123456789);

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

            String actual = generateIdLogic.generateId();

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------
            assertThat(actual).isEqualTo(ID);

            // NOTE: 呼び出されるメソッドが想定通りであること
            mock.verify(LocalDateTime::now, times(1));
        }

    }

}
