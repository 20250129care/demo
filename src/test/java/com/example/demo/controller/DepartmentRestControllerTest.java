package com.example.demo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.common.response.ErrorResponse;
import com.example.demo.common.response.SuccessResponse;
import com.example.demo.service.DepartmentResult;
import com.example.demo.service.DepartmentResultData;
import com.example.demo.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

// NOTE: Controllerのテストはバリデーションとレスポンスが想定通りに動くかだけに観点を置く
// NOTE: Serviceの処理はServiceのテストに切り出すことでテスト観点を明確化する
// NOTE: パラメータの変換が正しく行われるかどうかは各メソッドのテストに切り出すことでテスト観点を明確化する

// NOTE: テスト対象のControllerを@WebMvcTestで設定
@WebMvcTest(DepartmentRestController.class)
class DepartmentRestControllerTest {

    private static final String URL = "/departments";

    private static final String OPERATOR_KEY = "X-Operator";

    private static final String OPERATOR_VALUE = "OPERATOR";

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テストを実行するのに必要なクラスをインジェクションする
    @Autowired
    private MockMvc mockMvc;

    // NOTE: 呼び出されるクラスは@MockitoBean
    // NOTE: Springの設定を読み込まないため@Mockは使わない
    @MockitoBean
    private DepartmentRestControllerConverter converter;

    @MockitoBean
    private DepartmentService service;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("fetchAllApi")
    @Nested
    class Method1 {

        @DisplayName("業務処理")
        @Nested
        class Application {

            // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
            // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

            private DepartmentResult result = null;
            private DepartmentResponse response = null;

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

                response = new DepartmentResponse(List.of(
                        new DepartmentResponseData(
                                "01",
                                "部署1",
                                false),
                        new DepartmentResponseData(
                                "02",
                                "部署2",
                                false),
                        new DepartmentResponseData(
                                "03",
                                "部署3",
                                false)));
            }

            @DisplayName("正常終了")
            @Test
            void testOK1() throws Exception {
                String responseJson = objectMapper.writeValueAsString(new SuccessResponse(response));

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doReturn(result)
                        .when(service)
                        .fetchAll(anyString());

                doReturn(response)
                        .when(converter)
                        .convertToResponse(any());

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        get(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(content().json(responseJson));

                // NOTE: 呼び出されるメソッドが想定通りであること
                verify(service, times(1)).fetchAll(eq(OPERATOR_VALUE));
                verify(converter, times(1)).convertToResponse(eq(result));
            }

            @DisplayName("異常終了：RuntimeException")
            @Test
            void testNG1() throws Exception {
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse("999", "想定外の例外が発生しました"));

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doThrow(RuntimeException.class)
                        .when(service)
                        .fetchAll(anyString());

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        get(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE))
                        .andExpect(status().isInternalServerError())
                        .andExpect(content().json(responseJson));

                // NOTE: 呼び出されるメソッドが想定通りであること
                verify(service, times(1)).fetchAll(eq(OPERATOR_VALUE));
            }

            @DisplayName("異常終了：DataAccessException")
            @Test
            void testNG2() throws Exception {
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse("902", "DBエラーが発生しました"));

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doThrow(new DataAccessException("") {
                })
                        .when(service)
                        .fetchAll(anyString());

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        get(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE))
                        .andExpect(status().isInternalServerError())
                        .andExpect(content().json(responseJson));

                // NOTE: 呼び出されるメソッドが想定通りであること
                verify(service, times(1)).fetchAll(eq(OPERATOR_VALUE));
            }

        }

        @DisplayName("バリデーション")
        @Nested
        class Validation {

            @DisplayName("header：null")
            @Test
            void testNG4() throws Exception {
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        "904",
                        "ヘッダのエラーが発生しました"));

                mockMvc.perform(
                        get(URL))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

        }

    }

}