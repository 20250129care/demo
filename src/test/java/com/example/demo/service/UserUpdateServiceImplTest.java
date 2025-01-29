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
class UserUpdateServiceImplTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private UserUpdateServiceImpl service;

    // NOTE: 呼び出されるクラスは@Mock
    // NOTE: Springの設定を読み込んでしまうため@MockitBeanは使わない
    @Mock
    private UserUpdateServiceImplConverter converter;

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

        private UserUpdateParam param = null;
        private User entity = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            param = new UserUpdateParam(
                    BASE_ID + "_01",
                    BASE_FAMILY_NAME + "101",
                    BASE_FIRST_NAME + "101",
                    DEPT_ID,
                    VERSION);

            entity = new User(
                    BASE_ID + "_01",
                    BASE_FAMILY_NAME + "101",
                    BASE_FIRST_NAME + "101",
                    DEPT_ID,
                    VERSION);
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entity)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsById(anyString());

            doReturn(true)
                    .when(userMapper)
                    .existsById(anyString());

            doReturn(1)
                    .when(userMapper)
                    .update(any());

            doReturn(1)
                    .when(sharedMapper)
                    .modifyFromUser(anyString());

            doNothing()
                    .when(externalApiLogic)
                    .logOperation(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生しないこと
            assertThatCode(() -> service.update(OPERATOR, param)).doesNotThrowAnyException();

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(param));
            verify(departmentMapper, times(1)).existsById(eq(DEPT_ID));
            verify(userMapper, times(1)).existsById(eq(BASE_ID + "_01"));
            verify(userMapper, times(1)).update(eq(entity));
            verify(sharedMapper, times(1)).modifyFromUser(eq(BASE_ID + "_01"));
            verify(externalApiLogic, times(1)).logOperation(eq("ユーザ更新"), eq(OPERATOR));
        }

        @DisplayName("異常終了：existsById：DataAccessException")
        @Test
        void testNG1() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entity)
                    .when(converter)
                    .convertToEntity(any());

            doThrow(new DataAccessException("") {
            })
                    .when(departmentMapper)
                    .existsById(anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.update(OPERATOR, param)).isInstanceOf(DataAccessException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(param));
            verify(departmentMapper, times(1)).existsById(eq(DEPT_ID));
        }

        @DisplayName("異常終了：existsById：ApplicationException")
        @Test
        void testNG2() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entity)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(false)
                    .when(departmentMapper)
                    .existsById(anyString());

            doReturn(new ApplicationException("", ""))
                    .when(exceptionCreator)
                    .create(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.update(OPERATOR, param)).isInstanceOf(ApplicationException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(param));
            verify(departmentMapper, times(1)).existsById(eq(DEPT_ID));
            verify(exceptionCreator, times(1)).create(eq("402"), eq(deptId()));
        }

        @DisplayName("異常終了：existsById：DataAccessException")
        @Test
        void testNG3() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entity)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsById(anyString());

            doThrow(new DataAccessException("") {
            })
                    .when(userMapper)
                    .existsById(anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.update(OPERATOR, param)).isInstanceOf(DataAccessException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(param));
            verify(departmentMapper, times(1)).existsById(eq(DEPT_ID));
            verify(userMapper, times(1)).existsById(eq(BASE_ID + "_01"));
        }

        @DisplayName("異常終了：existsById：ApplicationException")
        @Test
        void testNG4() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entity)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsById(anyString());

            doReturn(false)
                    .when(userMapper)
                    .existsById(anyString());

            doReturn(new ApplicationException("", ""))
                    .when(exceptionCreator)
                    .create(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.update(OPERATOR, param)).isInstanceOf(ApplicationException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(param));
            verify(departmentMapper, times(1)).existsById(eq(DEPT_ID));
            verify(userMapper, times(1)).existsById(eq(BASE_ID + "_01"));
            verify(exceptionCreator, times(1)).create(eq("102"), eq(userId()));
        }

        @DisplayName("異常終了：updateById：DataAccessException")
        @Test
        void testNG5() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entity)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsById(anyString());

            doReturn(true)
                    .when(userMapper)
                    .existsById(anyString());

            doThrow(new DataAccessException("") {
            })
                    .when(userMapper)
                    .update(any());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.update(OPERATOR, param)).isInstanceOf(DataAccessException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(param));
            verify(departmentMapper, times(1)).existsById(eq(DEPT_ID));
            verify(userMapper, times(1)).existsById(eq(BASE_ID + "_01"));
            verify(userMapper, times(1)).update(eq(entity));
        }

        @DisplayName("異常終了：updateById：ApplicationException")
        @Test
        void testNG6() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entity)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsById(anyString());

            doReturn(true)
                    .when(userMapper)
                    .existsById(anyString());

            doReturn(0)
                    .when(userMapper)
                    .update(any());

            doReturn(new ApplicationException("", ""))
                    .when(exceptionCreator)
                    .create(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.update(OPERATOR, param)).isInstanceOf(ApplicationException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(param));
            verify(departmentMapper, times(1)).existsById(eq(DEPT_ID));
            verify(userMapper, times(1)).existsById(eq(BASE_ID + "_01"));
            verify(userMapper, times(1)).update(eq(entity));
            verify(exceptionCreator, times(1)).create(eq("202"), eq(userId()));
        }

        @DisplayName("異常終了：modifyUserToUserSummary：DataAccessException")
        @Test
        void testNG7() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entity)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsById(anyString());

            doReturn(true)
                    .when(userMapper)
                    .existsById(anyString());

            doReturn(1)
                    .when(userMapper)
                    .update(any());

            doThrow(new DataAccessException("") {
            })
                    .when(sharedMapper)
                    .modifyFromUser(anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.update(OPERATOR, param)).isInstanceOf(DataAccessException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(param));
            verify(departmentMapper, times(1)).existsById(eq(DEPT_ID));
            verify(userMapper, times(1)).existsById(eq(BASE_ID + "_01"));
            verify(userMapper, times(1)).update(eq(entity));
            verify(sharedMapper, times(1)).modifyFromUser(eq(BASE_ID + "_01"));
        }

        @DisplayName("異常終了：modifyUserToUserSummary：ApplicationException")
        @Test
        void testNG8() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entity)
                    .when(converter)
                    .convertToEntity(any());

            doReturn(true)
                    .when(departmentMapper)
                    .existsById(anyString());

            doReturn(true)
                    .when(userMapper)
                    .existsById(anyString());

            doReturn(1)
                    .when(userMapper)
                    .update(any());

            doReturn(0)
                    .when(sharedMapper)
                    .modifyFromUser(anyString());

            doReturn(new ApplicationException("", ""))
                    .when(exceptionCreator)
                    .create(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.update(OPERATOR, param)).isInstanceOf(ApplicationException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(converter, times(1)).convertToEntity(eq(param));
            verify(departmentMapper, times(1)).existsById(eq(DEPT_ID));
            verify(userMapper, times(1)).existsById(eq(BASE_ID + "_01"));
            verify(userMapper, times(1)).update(eq(entity));
            verify(sharedMapper, times(1)).modifyFromUser(eq(BASE_ID + "_01"));
            verify(exceptionCreator, times(1)).create(eq("302"), eq(userId()));
        }

        private String userId() {
            return BASE_ID + "_01";
        }

        private String deptId() {
            return DEPT_ID;
        }

    }

}