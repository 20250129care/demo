package com.example.demo.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.service.UserUpdateParam;

// NOTE: Componentのテストは処理の内容が正しく行われるかだけに観点を置く

@ExtendWith(MockitoExtension.class)
class UserUpdateRestControllerConverterTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private UserUpdateRestControllerConverter converter;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("convertToParam")
    @Nested
    class Method1 {

        // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
        // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

        private static final String BASE_ID = "20250101120055111";
        private static final String BASE_FAMILY_NAME = "苗字";
        private static final String BASE_FIRST_NAME = "名前";
        private static final String DEPT_ID = "01";
        private static final Integer VERSION = 0;

        private UserUpdateRequest request = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            request = new UserUpdateRequest(
                    BASE_ID + "_01",
                    BASE_FAMILY_NAME + "101",
                    BASE_FIRST_NAME + "101",
                    DEPT_ID,
                    VERSION);
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            UserUpdateParam param = converter.convertToParam(request);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(param.getId()).isEqualTo(BASE_ID + "_01");
            assertThat(param.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "101");
            assertThat(param.getFirstName()).isEqualTo(BASE_FIRST_NAME + "101");
            assertThat(param.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(param.getVersion()).isEqualTo(VERSION);
        }

    }

}
