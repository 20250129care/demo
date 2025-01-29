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

import com.example.demo.logic.LoggingLogic;
import com.example.demo.mapper.Department;
import com.example.demo.mapper.DepartmentMapper;

// NOTE: Serviceのテストは処理の呼び出しが正しく行われるかだけに観点を置く
// NOTE: 業務処理が想定した順番で行われるかということ
// NOTE: パラメータの変換が正しく行われるかどうかは各メソッドのテストに切り出すことでテスト観点を明確化する

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private DepartmentServiceImpl service;

    // NOTE: 呼び出されるクラスは@Mock
    // NOTE: Springの設定を読み込んでしまうため@MockitBeanは使わない
    @Mock
    private DepartmentServiceImplConverter converter;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private LoggingLogic externalApiLogic;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("fetchAll")
    @Nested
    class Method1 {

        // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
        // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

        private static final String OPERATOR = "OPERATOR";

        private List<Department> entityList = null;
        private DepartmentResult result = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

            entityList = List.of(
                    new Department(
                            "01",
                            "部署1",
                            false),
                    new Department(
                            "02",
                            "部署2",
                            false),
                    new Department(
                            "03",
                            "部署3",
                            false));

            result = new DepartmentResult(List.of(
                    new DepartmentResultData(
                            "01",
                            "部署1",
                            false),
                    new DepartmentResultData(
                            "02",
                            "部署2",
                            false),
                    new DepartmentResultData(
                            "03",
                            "部署3",
                            false)));
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doReturn(entityList)
                    .when(departmentMapper)
                    .findAll();

            doReturn(result)
                    .when(converter)
                    .convertToResult(any());

            doNothing()
                    .when(externalApiLogic)
                    .logOperation(anyString(), anyString());

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            DepartmentResult actual = service.fetchAll(OPERATOR);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            List<DepartmentResultData> actualList = actual.getList();
            assertThat(actualList).hasSize(3);
            DepartmentResultData actual1 = actualList.get(0);
            assertThat(actual1.getId()).isEqualTo("01");
            assertThat(actual1.getName()).isEqualTo("部署1");
            assertThat(actual1.getIsDeleted()).isFalse();
            DepartmentResultData actual2 = actualList.get(1);
            assertThat(actual2.getId()).isEqualTo("02");
            assertThat(actual2.getName()).isEqualTo("部署2");
            assertThat(actual2.getIsDeleted()).isFalse();
            DepartmentResultData actual3 = actualList.get(2);
            assertThat(actual3.getId()).isEqualTo("03");
            assertThat(actual3.getName()).isEqualTo("部署3");
            assertThat(actual3.getIsDeleted()).isFalse();

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(departmentMapper, times(1)).findAll();
            verify(converter, times(1)).convertToResult(eq(entityList));
            verify(externalApiLogic, times(1)).logOperation(eq("部署全件取得"), eq(OPERATOR));
        }

        @DisplayName("異常終了：DataAccessException")
        @Test
        void testNG1() throws Exception {
            // -----------------------------------------------------------------
            // モックの振る舞い設定
            // -----------------------------------------------------------------

            doThrow(new DataAccessException("") {
            })
                    .when(departmentMapper)
                    .findAll();

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            // 例外が発生すること
            assertThatThrownBy(() -> service.fetchAll(OPERATOR)).isInstanceOf(DataAccessException.class);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            // NOTE: 呼び出されるメソッドが想定通りであること
            verify(departmentMapper, times(1)).findAll();
            verify(converter, never()).convertToResult(anyList());
            verify(externalApiLogic, never()).logOperation(anyString(), anyString());
        }

    }

}
