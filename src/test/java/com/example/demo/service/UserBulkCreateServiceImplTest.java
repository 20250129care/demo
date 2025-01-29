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
import org.springframework.dao.DataAccessException;

import com.example.demo.common.exception.ApplicationException;
import com.example.demo.logic.ExceptionCreator;
import com.example.demo.logic.LoggingLogic;
import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.mapper.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserSummaryMapper;

// NOTE: Serviceのテストは処理の呼び出しが正しく行われるかだけに観点を置く
// NOTE: 業務処理が想定した順番で行われるかということ
// NOTE: パラメータの変換が正しく行われるかどうかは各メソッドのテストに切り出すことでテスト観点を明確化する

@ExtendWith(MockitoExtension.class)
class UserBulkCreateServiceImplTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private UserBulkCreateServiceImpl service;

    // NOTE: 呼び出されるクラスは@Mock
    // NOTE: Springの設定を読み込んでしまうため@MockitBeanは使わない
    @Mock
    private UserBulkCreateServiceImplConverter converter;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserSummaryMapper sharedMapper;

    @Mock
    private ExceptionCreator exceptionCreator;

    @Mock
    private LoggingLogic externalApiLogic;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("update")
    @Nested
    class Method1 {

        // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
        // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

        private static final String BASE_ID = "20250101120055111";
        private static final String BASE_FAMILY_NAME = "苗字";
        private static final String BASE_FIRST_NAME = "名前";
        private static final String DEPT_ID = "01";
        private static final Integer VERSION = 0;
        private static final String OPERATOR = "OPERATOR";

        private UserBulkCreateParam bulkParam = null;
        private List<User> entityList = null;

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

            entityList = List.of(
                    new User(
                            BASE_ID + "_01",
                            BASE_FAMILY_NAME + "1",
                            BASE_FIRST_NAME + "1",
                            DEPT_ID,
                            VERSION),
                    new User(
                            BASE_ID + "_02",
                            BASE_FAMILY_NAME + "2",
                            BASE_FIRST_NAME + "2",
                            DEPT_ID,
                            VERSION),
                    new User(
                            BASE_ID + "_03",
                            BASE_FAMILY_NAME + "3",
                            BASE_FIRST_NAME + "3",
                            DEPT_ID,
                            VERSION));
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entityList)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsByIdList(anyList());

            doReturn(false)
                    .when(userMapper)
                    .existsByIdList(anyList());

            doReturn(3)
                    .when(userMapper)
                    .insertList(anyList());

            doReturn(3)
                    .when(sharedMapper)
                    .copyListFromUser(anyList());

            doNothing()
                    .when(externalApiLogic)
                    .logOperation(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生しないこと
            assertThatCode(() -> service.bulkCreate(OPERATOR, bulkParam)).doesNotThrowAnyException();

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(bulkParam));
            verify(departmentMapper, times(1)).existsByIdList(eq(List.of(DEPT_ID)));
            verify(userMapper, times(1)).existsByIdList(eq(List.of(BASE_ID + "_01", BASE_ID + "_02", BASE_ID + "_03")));
            verify(userMapper, times(1)).insertList(entityList);
            verify(sharedMapper, times(1)).copyListFromUser(List.of(BASE_ID + "_01", BASE_ID + "_02", BASE_ID + "_03"));
            verify(externalApiLogic, times(1)).logOperation(eq("ユーザ登録（複数）"), eq(OPERATOR));
        }

        @DisplayName("異常終了：existsList：DataAccessException")
        @Test
        void testNG1() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entityList)
                    .when(converter)
                    .convertToEntity(any());

            doThrow(new DataAccessException("") {
            })
                    .when(departmentMapper)
                    .existsByIdList(anyList());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.bulkCreate(OPERATOR, bulkParam)).isInstanceOf(DataAccessException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(bulkParam));
            verify(departmentMapper, times(1)).existsByIdList(eq(List.of(DEPT_ID)));
        }

        @DisplayName("異常終了：existsList：ApplicationException")
        @Test
        void testNG2() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entityList)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(false)
                    .when(departmentMapper)
                    .existsByIdList(anyList());

            doReturn(new ApplicationException("", ""))
                    .when(exceptionCreator)
                    .create(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.bulkCreate(OPERATOR, bulkParam)).isInstanceOf(ApplicationException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(bulkParam));
            verify(departmentMapper, times(1)).existsByIdList(eq(List.of(DEPT_ID)));
            verify(exceptionCreator, times(1)).create(eq("401"), eq(deptIds()));
        }

        @DisplayName("異常終了：existsList：DataAccessException")
        @Test
        void testNG3() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entityList)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsByIdList(anyList());

            doThrow(new DataAccessException("") {
            })
                    .when(userMapper)
                    .existsByIdList(anyList());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.bulkCreate(OPERATOR, bulkParam)).isInstanceOf(DataAccessException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(bulkParam));
            verify(departmentMapper, times(1)).existsByIdList(List.of(DEPT_ID));
            verify(userMapper, times(1)).existsByIdList(eq(List.of(BASE_ID + "_01", BASE_ID + "_02", BASE_ID + "_03")));
        }

        @DisplayName("異常終了：existsList：ApplicationException")
        @Test
        void testNG4() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entityList)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsByIdList(anyList());

            doReturn(true)
                    .when(userMapper)
                    .existsByIdList(anyList());

            doReturn(new ApplicationException("", ""))
                    .when(exceptionCreator)
                    .create(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.bulkCreate(OPERATOR, bulkParam)).isInstanceOf(ApplicationException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(bulkParam));
            verify(departmentMapper, times(1)).existsByIdList(List.of(DEPT_ID));
            verify(userMapper, times(1)).existsByIdList(eq(List.of(BASE_ID + "_01", BASE_ID + "_02", BASE_ID + "_03")));
            verify(exceptionCreator, times(1)).create(eq("101"), eq(userIds()));
        }

        @DisplayName("異常終了：insertList：DataAccessException")
        @Test
        void testNG5() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entityList)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsByIdList(anyList());

            doReturn(false)
                    .when(userMapper)
                    .existsByIdList(anyList());

            doThrow(new DataAccessException("") {
            })
                    .when(userMapper)
                    .insertList(anyList());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.bulkCreate(OPERATOR, bulkParam)).isInstanceOf(DataAccessException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(bulkParam));
            verify(departmentMapper, times(1)).existsByIdList(List.of(DEPT_ID));
            verify(userMapper, times(1)).existsByIdList(eq(List.of(BASE_ID + "_01", BASE_ID + "_02", BASE_ID + "_03")));
            verify(userMapper, times(1)).insertList(eq(entityList));
        }

        @DisplayName("異常終了：insertList：ApplicationException")
        @Test
        void testNG6() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entityList)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsByIdList(anyList());

            doReturn(false)
                    .when(userMapper)
                    .existsByIdList(anyList());

            doReturn(0)
                    .when(userMapper)
                    .insertList(anyList());

            doReturn(new ApplicationException("", ""))
                    .when(exceptionCreator)
                    .create(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.bulkCreate(OPERATOR, bulkParam)).isInstanceOf(ApplicationException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(bulkParam));
            verify(departmentMapper, times(1)).existsByIdList(eq(List.of(DEPT_ID)));
            verify(userMapper, times(1)).existsByIdList(eq(List.of(BASE_ID + "_01", BASE_ID + "_02", BASE_ID + "_03")));
            verify(userMapper, times(1)).insertList(eq(entityList));
            verify(exceptionCreator, times(1)).create(eq("201"), eq(userIds()));
        }

        @DisplayName("異常終了：copyListUserToUserSummary：DataAccessException")
        @Test
        void testNG7() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entityList)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsByIdList(anyList());

            doReturn(false)
                    .when(userMapper)
                    .existsByIdList(anyList());

            doReturn(3)
                    .when(userMapper)
                    .insertList(anyList());

            doThrow(new DataAccessException("") {
            })
                    .when(sharedMapper)
                    .copyListFromUser(anyList());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.bulkCreate(OPERATOR, bulkParam)).isInstanceOf(DataAccessException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(bulkParam));
            verify(departmentMapper, times(1)).existsByIdList(eq(List.of(DEPT_ID)));
            verify(userMapper, times(1)).existsByIdList(eq(List.of(BASE_ID + "_01", BASE_ID + "_02", BASE_ID + "_03")));
            verify(userMapper, times(1)).insertList(eq(entityList));
            verify(sharedMapper, times(1))
                    .copyListFromUser(eq(List.of(BASE_ID + "_01", BASE_ID + "_02", BASE_ID + "_03")));
        }

        @DisplayName("異常終了：copyListUserToUserSummary：ApplicationException")
        @Test
        void testNG8() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entityList)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsByIdList(anyList());

            doReturn(false)
                    .when(userMapper)
                    .existsByIdList(anyList());

            doReturn(3)
                    .when(userMapper)
                    .insertList(anyList());

            doReturn(0)
                    .when(sharedMapper)
                    .copyListFromUser(anyList());

            doReturn(new ApplicationException("", ""))
                    .when(exceptionCreator)
                    .create(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.bulkCreate(OPERATOR, bulkParam)).isInstanceOf(ApplicationException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(bulkParam));
            verify(departmentMapper, times(1)).existsByIdList(eq(List.of(DEPT_ID)));
            verify(userMapper, times(1)).existsByIdList(eq(List.of(BASE_ID + "_01", BASE_ID + "_02", BASE_ID + "_03")));
            verify(userMapper, times(1)).insertList(eq(entityList));
            verify(sharedMapper, times(1)).copyListFromUser(anyList());
            verify(exceptionCreator, times(1)).create(eq("301"), eq(userIds()));
        }

        private String userIds() {
            return BASE_ID + "_01," + BASE_ID + "_02," + BASE_ID + "_03";
        }

        private String deptIds() {
            return DEPT_ID + "," + DEPT_ID + "," + DEPT_ID;
        }

    }

}
