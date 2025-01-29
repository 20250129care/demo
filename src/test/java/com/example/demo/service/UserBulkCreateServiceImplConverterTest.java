package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

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
class UserBulkCreateServiceImplConverterTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private UserBulkCreateServiceImplConverter converter;

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

        private UserBulkCreateParam bulkParam = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            bulkParam = new UserBulkCreateParam(List.of(
                    new UserCreateParam(
                            BASE_FAMILY_NAME + "1",
                            BASE_FIRST_NAME + "1",
                            DEPT_ID),
                    new UserCreateParam(
                            BASE_FAMILY_NAME + "2",
                            BASE_FIRST_NAME + "2",
                            DEPT_ID),
                    new UserCreateParam(
                            BASE_FAMILY_NAME + "3",
                            BASE_FIRST_NAME + "3",
                            DEPT_ID)));
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

            List<User> entityList = converter.convertToEntity(bulkParam);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(entityList).hasSize(3);
            User entity1 = entityList.get(0);
            assertThat(entity1.getId()).isEqualTo(BASE_ID + "_01");
            assertThat(entity1.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "1");
            assertThat(entity1.getFirstName()).isEqualTo(BASE_FIRST_NAME + "1");
            assertThat(entity1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(entity1.getVersion()).isEqualTo(0);
            User entity2 = entityList.get(1);
            assertThat(entity2.getId()).isEqualTo(BASE_ID + "_02");
            assertThat(entity2.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "2");
            assertThat(entity2.getFirstName()).isEqualTo(BASE_FIRST_NAME + "2");
            assertThat(entity2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(entity2.getVersion()).isEqualTo(0);
            User entity3 = entityList.get(2);
            assertThat(entity3.getId()).isEqualTo(BASE_ID + "_03");
            assertThat(entity3.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "3");
            assertThat(entity3.getFirstName()).isEqualTo(BASE_FIRST_NAME + "3");
            assertThat(entity3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(entity3.getVersion()).isEqualTo(0);

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(generateIdLogic, times(1)).generateId();
        }

    }

}
