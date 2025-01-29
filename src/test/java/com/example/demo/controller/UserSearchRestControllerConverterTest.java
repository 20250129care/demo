package com.example.demo.controller;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.service.UserSearchParam;
import com.example.demo.service.UserSearchResult;
import com.example.demo.service.UserSearchResultData;

// NOTE: Componentのテストは処理の内容が正しく行われるかだけに観点を置く

@ExtendWith(MockitoExtension.class)
class UserSearchRestControllerConverterTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    @InjectMocks
    private UserSearchRestControllerConverter converter;

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
        private static final LocalDate BEGIN_UPDATED_AT = LocalDate.of(2025, 1, 1);
        private static final LocalDate END_UPDATED_AT = LocalDate.of(2025, 1, 1);

        private UserSearchRequest request = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            request = new UserSearchRequest(
                    BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1",
                    DEPT_ID,
                    BEGIN_UPDATED_AT,
                    END_UPDATED_AT);
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            UserSearchParam param = converter.convertToParam(request);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(param.getName()).isEqualTo(BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1");
            assertThat(param.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(param.getBeginUpdatedAt()).isEqualTo(BEGIN_UPDATED_AT);
            assertThat(param.getEndUpdatedAt()).isEqualTo(END_UPDATED_AT);
        }

    }

    @DisplayName("convertToResponse")
    @Nested
    class Method2 {

        // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
        // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

        private static final String BASE_FAMILY_NAME = "苗字";
        private static final String BASE_FIRST_NAME = "名前";
        private static final String DEPT_ID = "01";
        private static final String DEPT_NAME = "部署名";
        private static final LocalDate LAST_UPDATED_AT = LocalDate.of(2025, 1, 1);
        private static final String BASE_USER_ID = "20250101120055111";
        private static final Integer USER_VERSION = 0;

        private UserSearchResult result = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            result = new UserSearchResult(List.of(
                    new UserSearchResultData(
                            BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1",
                            DEPT_ID,
                            DEPT_NAME,
                            LAST_UPDATED_AT,
                            BASE_USER_ID + "_01",
                            USER_VERSION),
                    new UserSearchResultData(
                            BASE_FAMILY_NAME + "2" + BASE_FIRST_NAME + "2",
                            DEPT_ID,
                            DEPT_NAME,
                            LAST_UPDATED_AT,
                            BASE_USER_ID + "_02",
                            USER_VERSION),
                    new UserSearchResultData(
                            BASE_FAMILY_NAME + "3" + BASE_FIRST_NAME + "3",
                            DEPT_ID,
                            DEPT_NAME,
                            LAST_UPDATED_AT,
                            BASE_USER_ID + "_03",
                            USER_VERSION)));
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            UserSearchResponse response = converter.convertToResponse(result);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            List<UserSearchResponseData> responseList = response.getList();
            assertThat(responseList).hasSize(3);
            UserSearchResponseData response1 = responseList.get(0);
            assertThat(response1.getName()).isEqualTo(BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1");
            assertThat(response1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(response1.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(response1.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(response1.getUserId()).isEqualTo(BASE_USER_ID + "_01");
            assertThat(response1.getUserVersion()).isEqualTo(USER_VERSION);
            UserSearchResponseData response2 = responseList.get(1);
            assertThat(response2.getName()).isEqualTo(BASE_FAMILY_NAME + "2" + BASE_FIRST_NAME + "2");
            assertThat(response2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(response2.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(response2.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(response2.getUserId()).isEqualTo(BASE_USER_ID + "_02");
            assertThat(response2.getUserVersion()).isEqualTo(USER_VERSION);
            UserSearchResponseData response3 = responseList.get(2);
            assertThat(response3.getName()).isEqualTo(BASE_FAMILY_NAME + "3" + BASE_FIRST_NAME + "3");
            assertThat(response3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(response3.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(response3.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(response3.getUserId()).isEqualTo(BASE_USER_ID + "_03");
            assertThat(response3.getUserVersion()).isEqualTo(USER_VERSION);
        }

    }

}
