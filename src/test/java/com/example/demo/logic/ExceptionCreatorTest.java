package com.example.demo.logic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.example.demo.common.exception.ApplicationException;

// NOTE: Componentのテストは処理の内容が正しく行われるかだけに観点を置く

@ExtendWith(MockitoExtension.class)
class ExceptionCreatorTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private ExceptionCreator exceptionCreator;

    // NOTE: 呼び出されるクラスは@Mock
    // NOTE: Springの設定を読み込んでしまうため@MockitBeanは使わない
    @Mock
    private MessageSource messageSource;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("create")
    @Nested
    class Method1 {

        private static final String ERROR_ID = "001";
        private static final String ERROR_MESSAGE = "テストメッセージ";

        @DisplayName("引数なし")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(ERROR_MESSAGE)
                    .when(messageSource)
                    .getMessage(anyString(), any(), any());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            ApplicationException actual = exceptionCreator.create(ERROR_ID);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(actual.getErrorId()).isEqualTo(ERROR_ID);
            assertThat(actual.getErrorMessage()).isEqualTo(ERROR_MESSAGE);

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(messageSource, times(1)).getMessage(ERROR_ID, new Object[] {}, Locale.JAPAN);
        }

        @DisplayName("引数あり")
        @Test
        void testOK2() {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(ERROR_MESSAGE)
                    .when(messageSource)
                    .getMessage(anyString(), any(), any());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            ApplicationException actual = exceptionCreator.create(ERROR_ID, "引数", 1);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(actual.getErrorId()).isEqualTo(ERROR_ID);
            assertThat(actual.getErrorMessage()).isEqualTo(ERROR_MESSAGE);

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(messageSource, times(1)).getMessage(ERROR_ID, new Object[] { "引数", 1 }, Locale.JAPAN);
        }

    }

}
