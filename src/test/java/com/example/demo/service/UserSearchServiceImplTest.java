package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import com.example.demo.logic.LoggingLogic;
import com.example.demo.mapper.UserSummary;
import com.example.demo.mapper.UserSummaryCondition;
import com.example.demo.mapper.UserSummaryMapper;

// NOTE: Serviceのテストは処理の呼び出しが正しく行われるかだけに観点を置く
// NOTE: 業務処理が想定した順番で行われるかということ
// NOTE: パラメータの変換が正しく行われるかどうかは各メソッドのテストに切り出すことでテスト観点を明確化する

@ExtendWith(MockitoExtension.class)
class UserSearchServiceImplTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // テスト対象のクラスを@InjectMocks
    @InjectMocks
    private UserSearchServiceImpl service;

    // NOTE: 呼び出されるクラスは@Mock
    // NOTE: Springの設定を読み込んでしまうため@MockitBeanは使わない
    @Mock
    private UserSearchServiceImplConverter converter;

    @Mock
    private UserSummaryMapper userSummaryMapper;

    @Mock
    private LoggingLogic externalApiLogic;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("search")
    @Nested
    class Method1 {

        // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
        // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

        private static final String BASE_FAMILY_NAME = "苗字";
        private static final String BASE_FIRST_NAME = "名前";
        private static final String DEPT_ID = "01";
        private static final String DEPT_NAME = "部署名";
        private static final LocalDate BEGIN_UPDATED_AT = LocalDate.of(2025, 1, 1);
        private static final LocalDate END_UPDATED_AT = LocalDate.of(2025, 1, 1);
        private static final String OPERATOR = "OPERATOR";
        private static final LocalDate LAST_UPDATED_AT = LocalDate.of(2025, 1, 1);
        private static final String BASE_USER_ID = "20250101120055111";
        private static final Integer USER_VERSION = 0;

        private UserSearchParam param = null;
        private UserSummaryCondition condition = null;
        private List<UserSummary> entityList = null;
        private UserSearchResult result = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            param = new UserSearchParam(
                    BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1",
                    DEPT_ID,
                    BEGIN_UPDATED_AT,
                    END_UPDATED_AT);

            condition = new UserSummaryCondition(
                    BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1",
                    DEPT_ID,
                    BEGIN_UPDATED_AT,
                    END_UPDATED_AT);

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
        void testOK1() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(condition)
                    .when(converter)
                    .convertToCondition(any());

            doReturn(entityList)
                    .when(userSummaryMapper)
                    .find(any());

            doReturn(result)
                    .when(converter)
                    .convertToResult(any());

            doNothing()
                    .when(externalApiLogic)
                    .logOperation(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            UserSearchResult actual = service.search(OPERATOR, param);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            List<UserSearchResultData> actualList = actual.getList();
            assertThat(actualList).hasSize(3);
            UserSearchResultData actual1 = actualList.get(0);
            assertThat(actual1.getName()).isEqualTo(BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1");
            assertThat(actual1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual1.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(actual1.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(actual1.getUserId()).isEqualTo(BASE_USER_ID + "_01");
            assertThat(actual1.getUserVersion()).isEqualTo(USER_VERSION);
            UserSearchResultData actual2 = actualList.get(1);
            assertThat(actual2.getName()).isEqualTo(BASE_FAMILY_NAME + "2" + BASE_FIRST_NAME + "2");
            assertThat(actual2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual2.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(actual2.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(actual2.getUserId()).isEqualTo(BASE_USER_ID + "_02");
            assertThat(actual2.getUserVersion()).isEqualTo(USER_VERSION);
            UserSearchResultData actual3 = actualList.get(2);
            assertThat(actual3.getName()).isEqualTo(BASE_FAMILY_NAME + "3" + BASE_FIRST_NAME + "3");
            assertThat(actual3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual3.getDeptName()).isEqualTo(DEPT_NAME);
            assertThat(actual3.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT);
            assertThat(actual3.getUserId()).isEqualTo(BASE_USER_ID + "_03");
            assertThat(actual3.getUserVersion()).isEqualTo(USER_VERSION);

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToCondition(eq(param));
            verify(userSummaryMapper, times(1)).find(eq(condition));
            verify(converter, times(1)).convertToResult(eq(entityList));
            verify(externalApiLogic, times(1)).logOperation(eq("ユーザ検索"), eq(OPERATOR));
        }

        @DisplayName("異常終了：DataAccessException")
        @Test
        void testNG1() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(condition)
                    .when(converter)
                    .convertToCondition(any());

            doThrow(new DataAccessException("") {
            })
                    .when(userSummaryMapper)
                    .find(any());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.search(OPERATOR, param)).isInstanceOf(DataAccessException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToCondition(eq(param));
            verify(userSummaryMapper, times(1)).find(eq(condition));
        }

    }

}
