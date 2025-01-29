package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.mapper.Department;

// NOTE: Componentのテストは処理の内容が正しく行われるかだけに観点を置く

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplConverterTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@InjectMocks
    @InjectMocks
    private DepartmentServiceImplConverter converter;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("convertToResult")
    @Nested
    class Method1 {

        // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
        // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

        private List<Department> entityList = null;

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
        }

        @DisplayName("正常終了")
        @Test
        void testOK1() {
            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            DepartmentResult result = converter.convertToResult(entityList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            List<DepartmentResultData> resultList = result.getList();
            assertThat(resultList).hasSize(3);
            DepartmentResultData result1 = resultList.get(0);
            assertThat(result1.getId()).isEqualTo("01");
            assertThat(result1.getName()).isEqualTo("部署1");
            DepartmentResultData result2 = resultList.get(1);
            assertThat(result2.getId()).isEqualTo("02");
            assertThat(result2.getName()).isEqualTo("部署2");
            DepartmentResultData result3 = resultList.get(2);
            assertThat(result3.getId()).isEqualTo("03");
            assertThat(result3.getName()).isEqualTo("部署3");
        }

    }

}
