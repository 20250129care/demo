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

import com.example.demo.service.DepartmentResult;
import com.example.demo.service.DepartmentResultData;

// NOTE: Componentのテストは処理の内容が正しく行われるかだけに観点を置く

@ExtendWith(MockitoExtension.class)
class DepartmentRestControllerConverterTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    @InjectMocks
    private DepartmentRestControllerConverter converter;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("convertToResponse")
    @Nested
    class Method1 {

        // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
        // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

        private DepartmentResult result = null;

        @BeforeEach
        void setUp() {
            // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

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
        void testOK1() {
            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            DepartmentResponse response = converter.convertToResponse(result);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            List<DepartmentResponseData> responseList = response.getList();
            assertThat(responseList).hasSize(3);
            DepartmentResponseData response1 = responseList.get(0);
            assertThat(response1.getId()).isEqualTo("01");
            assertThat(response1.getName()).isEqualTo("部署1");
            assertThat(response1.getIsDeleted()).isFalse();
            DepartmentResponseData response2 = responseList.get(1);
            assertThat(response2.getId()).isEqualTo("02");
            assertThat(response2.getName()).isEqualTo("部署2");
            assertThat(response2.getIsDeleted()).isFalse();
            DepartmentResponseData response3 = responseList.get(2);
            assertThat(response3.getId()).isEqualTo("03");
            assertThat(response3.getName()).isEqualTo("部署3");
            assertThat(response3.getIsDeleted()).isFalse();
        }

    }

}
