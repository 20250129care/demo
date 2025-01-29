package com.example.demo.controller;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.service.UserBulkCreateParam;
import com.example.demo.service.UserCreateParam;

// NOTE: Componentのテストは処理の内容が正しく行われるかだけに観点を置く

@ExtendWith(MockitoExtension.class)
class UserBulkCreateRestControllerConverterTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private UserBulkCreateRestControllerConverter converter;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("convertToParam")
    @Nested
    class Method1 {

        // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
        // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

        private static final String BASE_FAMILY_NAME = "苗字";
        private static final String BASE_FIRST_NAME = "名前";
        private static final String DEPT_ID = "01";

        private UserBulkCreateRequest bulkRequest = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            bulkRequest = new UserBulkCreateRequest(List.of(
                    new UserCreateRequest(
                            BASE_FAMILY_NAME + "1",
                            BASE_FIRST_NAME + "1",
                            DEPT_ID),
                    new UserCreateRequest(
                            BASE_FAMILY_NAME + "2",
                            BASE_FIRST_NAME + "2",
                            DEPT_ID),
                    new UserCreateRequest(
                            BASE_FAMILY_NAME + "3",
                            BASE_FIRST_NAME + "3",
                            DEPT_ID)));
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            UserBulkCreateParam bulkParam = converter.convertToParam(bulkRequest);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            List<UserCreateParam> paramList = bulkParam.getList();
            assertThat(paramList).hasSize(3);
            UserCreateParam param1 = paramList.get(0);
            assertThat(param1.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "1");
            assertThat(param1.getFirstName()).isEqualTo(BASE_FIRST_NAME + "1");
            assertThat(param1.getDeptId()).isEqualTo(DEPT_ID);
            UserCreateParam param2 = paramList.get(1);
            assertThat(param2.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "2");
            assertThat(param2.getFirstName()).isEqualTo(BASE_FIRST_NAME + "2");
            assertThat(param2.getDeptId()).isEqualTo(DEPT_ID);
            UserCreateParam param3 = paramList.get(2);
            assertThat(param3.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "3");
            assertThat(param3.getFirstName()).isEqualTo(BASE_FIRST_NAME + "3");
            assertThat(param3.getDeptId()).isEqualTo(DEPT_ID);
        }

    }

}
