package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.logic.GenerateIdLogic;
import com.example.demo.mapper.User;

// NOTE: Componentのテストは処理の内容が正しく行われるかだけに観点を置く

@ExtendWith(MockitoExtension.class)
class UserCreateServiceImplConverterTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private UserCreateServiceImplConverter converter;

    // NOTE: 呼び出されるクラスは@Mock
    // NOTE: Springの設定を読み込んでしまうため@MockitBeanは使わない
    @Mock
    private GenerateIdLogic generateIdLogic;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("convertToEntity")
    @Nested
    class Method1 {

        // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
        // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

        private static final String BASE_ID = "20250101120055111";
        private static final String BASE_FAMILY_NAME = "苗字";
        private static final String BASE_FIRST_NAME = "名前";
        private static final String DEPT_ID = "01";
        private static final Integer VERSION = 0;

        private UserCreateParam param = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            param = new UserCreateParam(
                    BASE_FAMILY_NAME + "1",
                    BASE_FIRST_NAME + "1",
                    DEPT_ID);
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(BASE_ID)
                    .when(generateIdLogic)
                    .generateId();

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            User entity = converter.convertToEntity(param);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(entity.getId()).isEqualTo(BASE_ID + "_01");
            assertThat(entity.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "1");
            assertThat(entity.getFirstName()).isEqualTo(BASE_FIRST_NAME + "1");
            assertThat(entity.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(entity.getVersion()).isEqualTo(VERSION);

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(generateIdLogic, times(1)).generateId();
        }

    }

}
