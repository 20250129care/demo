package com.example.demo.service;

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

import com.example.demo.mapper.UserSummary;
import com.example.demo.mapper.UserSummaryCondition;

// NOTE: Componentのテストは処理の内容が正しく行われるかだけに観点を置く

@ExtendWith(MockitoExtension.class)
class UserSearchServiceImplConverterTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private UserSearchServiceImplConverter converter;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("convertToCondition")
    @Nested
    class Method1 {

        // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
        // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

        private static final String BASE_FAMILY_NAME = "苗字";
        private static final String BASE_FIRST_NAME = "名前";
        private static final String DEPT_ID = "01";
        private static final LocalDate BEGIN_UPDATED_AT = LocalDate.of(2025, 1, 1);
        private static final LocalDate END_UPDATED_AT = LocalDate.of(2025, 1, 1);

        private static final Integer PAGE_NO = 1;
        private static final Integer PAGE_SIZE = 100;

        private UserSearchParam param = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            param = new UserSearchParam(
                    BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1",
                    DEPT_ID,
                    BEGIN_UPDATED_AT,
                    END_UPDATED_AT,
                    PAGE_NO,
                    PAGE_SIZE);
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            UserSummaryCondition condition = converter.convertToCondition(param);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(condition.getName()).isEqualTo(BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1");
            assertThat(condition.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(condition.getBeginUpdatedAt()).isEqualTo(BEGIN_UPDATED_AT);
            assertThat(condition.getEndUpdatedAt()).isEqualTo(END_UPDATED_AT);
        }

    }

    @DisplayName("convertToResult")
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

        private List<UserSummary> entityList = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            entityList = List.of(
                    new UserSummary(
                            BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1",
                            DEPT_ID,
                            DEPT_NAME,
                            LAST_UPDATED_AT,
                            BASE_USER_ID + "_01",
                            USER_VERSION),
                    new UserSummary(
                            BASE_FAMILY_NAME + "2" + BASE_FIRST_NAME + "2",
                            DEPT_ID,
                            DEPT_NAME,
                            LAST_UPDATED_AT,
                            BASE_USER_ID + "_02",
                            USER_VERSION),
                    new UserSummary(
                            BASE_FAMILY_NAME + "3" + BASE_FIRST_NAME + "3",
                            DEPT_ID,
                            DEPT_NAME,
                            LAST_UPDATED_AT,
                            BASE_USER_ID + "_03",
                            USER_VERSION));
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            UserSearchResult result = converter.convertToResult(entityList, 1, 3);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(result.getPrevPageNo()).isNull();
            assertThat(result.getNextPageNo()).isNull();
            assertThat(result.getPageSize()).isEqualTo(3);

            List<UserSearchResultData> resultList = result.getList();
            assertThat(resultList).hasSize(3);
            UserSearchResultData result1 = resultList.get(0);
            assertThat(result1.getName()).isEqualTo(BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1");
            assertThat(result1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(result1.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(result1.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(result1.getUserId()).isEqualTo(BASE_USER_ID + "_01");
            assertThat(result1.getUserVersion()).isEqualTo(USER_VERSION);
            UserSearchResultData result2 = resultList.get(1);
            assertThat(result2.getName()).isEqualTo(BASE_FAMILY_NAME + "2" + BASE_FIRST_NAME + "2");
            assertThat(result2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(result2.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(result2.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(result2.getUserId()).isEqualTo(BASE_USER_ID + "_02");
            assertThat(result2.getUserVersion()).isEqualTo(USER_VERSION);
            UserSearchResultData result3 = resultList.get(2);
            assertThat(result3.getName()).isEqualTo(BASE_FAMILY_NAME + "3" + BASE_FIRST_NAME + "3");
            assertThat(result3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(result3.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(result3.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(result3.getUserId()).isEqualTo(BASE_USER_ID + "_03");
            assertThat(result3.getUserVersion()).isEqualTo(USER_VERSION);
        }

        @DisplayName("正常終了")
        @Test
        void testOK2() {
            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            UserSearchResult result = converter.convertToResult(entityList, 1, 2);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(result.getPrevPageNo()).isNull();
            assertThat(result.getNextPageNo()).isEqualTo(2);
            assertThat(result.getPageSize()).isEqualTo(2);

            List<UserSearchResultData> resultList = result.getList();
            assertThat(resultList).hasSize(2);
            UserSearchResultData result1 = resultList.get(0);
            assertThat(result1.getName()).isEqualTo(BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1");
            assertThat(result1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(result1.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(result1.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(result1.getUserId()).isEqualTo(BASE_USER_ID + "_01");
            assertThat(result1.getUserVersion()).isEqualTo(USER_VERSION);
            UserSearchResultData result2 = resultList.get(1);
            assertThat(result2.getName()).isEqualTo(BASE_FAMILY_NAME + "2" + BASE_FIRST_NAME + "2");
            assertThat(result2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(result2.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(result2.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(result2.getUserId()).isEqualTo(BASE_USER_ID + "_02");
            assertThat(result2.getUserVersion()).isEqualTo(USER_VERSION);
        }

        @DisplayName("正常終了")
        @Test
        void testOK3() {
            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            UserSearchResult result = converter.convertToResult(entityList, 2, 2);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(result.getPrevPageNo()).isEqualTo(1);
            assertThat(result.getNextPageNo()).isEqualTo(3);
            assertThat(result.getPageSize()).isEqualTo(2);

            List<UserSearchResultData> resultList = result.getList();
            assertThat(resultList).hasSize(2);
            UserSearchResultData result1 = resultList.get(0);
            assertThat(result1.getName()).isEqualTo(BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1");
            assertThat(result1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(result1.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(result1.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(result1.getUserId()).isEqualTo(BASE_USER_ID + "_01");
            assertThat(result1.getUserVersion()).isEqualTo(USER_VERSION);
            UserSearchResultData result2 = resultList.get(1);
            assertThat(result2.getName()).isEqualTo(BASE_FAMILY_NAME + "2" + BASE_FIRST_NAME + "2");
            assertThat(result2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(result2.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(result2.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(result2.getUserId()).isEqualTo(BASE_USER_ID + "_02");
            assertThat(result2.getUserVersion()).isEqualTo(USER_VERSION);
        }
        
        @DisplayName("正常終了")
        @Test
        void testOK4() {
            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            UserSearchResult result = converter.convertToResult(entityList, 2, 3);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(result.getPrevPageNo()).isEqualTo(1);
            assertThat(result.getNextPageNo()).isNull();
            assertThat(result.getPageSize()).isEqualTo(3);

            List<UserSearchResultData> resultList = result.getList();
            assertThat(resultList).hasSize(3);
            UserSearchResultData result1 = resultList.get(0);
            assertThat(result1.getName()).isEqualTo(BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1");
            assertThat(result1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(result1.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(result1.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(result1.getUserId()).isEqualTo(BASE_USER_ID + "_01");
            assertThat(result1.getUserVersion()).isEqualTo(USER_VERSION);
            UserSearchResultData result2 = resultList.get(1);
            assertThat(result2.getName()).isEqualTo(BASE_FAMILY_NAME + "2" + BASE_FIRST_NAME + "2");
            assertThat(result2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(result2.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(result2.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(result2.getUserId()).isEqualTo(BASE_USER_ID + "_02");
            assertThat(result2.getUserVersion()).isEqualTo(USER_VERSION);
            UserSearchResultData result3 = resultList.get(2);
            assertThat(result3.getName()).isEqualTo(BASE_FAMILY_NAME + "3" + BASE_FIRST_NAME + "3");
            assertThat(result3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(result3.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(result3.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(result3.getUserId()).isEqualTo(BASE_USER_ID + "_03");
            assertThat(result3.getUserVersion()).isEqualTo(USER_VERSION);
        }

    }

}
