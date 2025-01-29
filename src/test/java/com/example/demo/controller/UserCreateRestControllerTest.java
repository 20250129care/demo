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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.common.exception.ApplicationException;
import com.example.demo.common.response.ErrorResponse;
import com.example.demo.common.response.SuccessResponse;
import com.example.demo.service.UserCreateParam;
import com.example.demo.service.UserCreateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

// NOTE: Controllerのテストはバリデーションとレスポンスが想定通りに動くかだけに観点を置く
// NOTE: Serviceの処理はServiceのテストに切り出すことでテスト観点を明確化する
// NOTE: パラメータの変換が正しく行われるかどうかは各メソッドのテストに切り出すことでテスト観点を明確化する

// NOTE: テスト対象のControllerを@WebMvcTestで設定
@WebMvcTest(UserCreateRestController.class)
class UserCreateRestControllerTest {

    private static final String URL = "/users/create";

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
    private UserCreateRestControllerConverter converter;

    @MockitoBean
    private UserCreateService service;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("createApi")
    @Nested
    class Method1 {

        @DisplayName("業務処理")
        @Nested
        class Application {

            // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
            // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

            private static final String BASE_FAMILY_NAME = "苗字";
            private static final String BASE_FIRST_NAME = "名前";
            private static final String DEPT_ID = "01";

            UserCreateRequest request = null;
            private UserCreateParam param = null;

            @BeforeEach
            void setUp() {
                // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

                request = new UserCreateRequest(
                        BASE_FAMILY_NAME + "1",
                        BASE_FIRST_NAME + "1",
                        DEPT_ID);

                param = new UserCreateParam(
                        BASE_FAMILY_NAME + "1",
                        BASE_FIRST_NAME + "1",
                        DEPT_ID);
            }

            @DisplayName("正常終了")
            @Test
            void testOK1() throws Exception {
                String requestJson = objectMapper.writeValueAsString(request);
                String responseJson = objectMapper.writeValueAsString(new SuccessResponse());

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doReturn(param)
                        .when(converter)
                        .convertToParam(any());

                doNothing()
                        .when(service)
                        .create(anyString(), any());

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(content().json(responseJson));

                // NOTE: 呼び出されるメソッドが想定通りであること
                verify(converter, times(1)).convertToParam(eq(request));
                verify(service, times(1)).create(eq(OPERATOR_VALUE), eq(param));
            }

            @DisplayName("異常終了：RuntimeException")
            @Test
            void testNG1() throws Exception {
                String requestJson = objectMapper.writeValueAsString(request);
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        "999",
                        "想定外の例外が発生しました"));

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doReturn(param)
                        .when(converter)
                        .convertToParam(any());

                doThrow(RuntimeException.class)
                        .when(service)
                        .create(anyString(), any());

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isInternalServerError())
                        .andExpect(content().json(responseJson));

                // NOTE: 呼び出されるメソッドが想定通りであること
                verify(converter, times(1)).convertToParam(eq(request));
                verify(service, times(1)).create(eq(OPERATOR_VALUE), eq(param));
            }

            @DisplayName("異常終了：DataAccessException")
            @Test
            void testNG2() throws Exception {
                String requestJson = objectMapper.writeValueAsString(request);
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        "902",
                        "DBエラーが発生しました"));

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doReturn(param)
                        .when(converter)
                        .convertToParam(any());

                doThrow(new DataAccessException("") {
                })
                        .when(service)
                        .create(anyString(), any());

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isInternalServerError())
                        .andExpect(content().json(responseJson));

                // NOTE: 呼び出されるメソッドが想定通りであること
                verify(converter, times(1)).convertToParam(eq(request));
                verify(service, times(1)).create(eq(OPERATOR_VALUE), eq(param));
            }

            @DisplayName("異常終了：ApplicationException")
            @Test
            void testNG3() throws Exception {
                String requestJson = objectMapper.writeValueAsString(request);
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        "100",
                        "テストメッセージ"));

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doReturn(param)
                        .when(converter)
                        .convertToParam(any());

                doThrow(new ApplicationException("100", "テストメッセージ"))
                        .when(service)
                        .create(anyString(), any());

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isInternalServerError())
                        .andExpect(content().json(responseJson));

                // NOTE: 呼び出されるメソッドが想定通りであること
                verify(converter, times(1)).convertToParam(eq(request));
                verify(service, times(1)).create(eq(OPERATOR_VALUE), eq(param));

            }

        }

        @DisplayName("バリデーション")
        @Nested
        class Validation {

            private static final String FAMILY_NAME = "苗字1";
            private static final String FIRST_NAME = "名前1";
            private static final String DEPT_ID = "01";

            private static final String VALIDATION_ERROR_CODE = "901";
            private static final String VALIDATION_ERROR_MESSAGE = "バリデーションエラーが発生しました";

            @DisplayName("family_name：null")
            @Test
            void testNG1() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserCreateRequest(
                        null,
                        FIRST_NAME,
                        DEPT_ID));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("familyName:値は必須です。")));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

            @DisplayName("family_name：0")
            @Test
            void testNG2() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserCreateRequest(
                        "",
                        FIRST_NAME,
                        DEPT_ID));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("familyName:1以上、50以下の桁数です。")));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

            @DisplayName("family_name：51")
            @Test
            void testNG3() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserCreateRequest(
                        "123456789012345678901234567890123456789012345678901",
                        FIRST_NAME,
                        DEPT_ID));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("familyName:1以上、50以下の桁数です。")));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

            @DisplayName("first_name：null")
            @Test
            void testNG4() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserCreateRequest(
                        FAMILY_NAME,
                        null,
                        DEPT_ID));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("firstName:値は必須です。")));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

            @DisplayName("first_name：0")
            @Test
            void testNG5() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserCreateRequest(
                        FAMILY_NAME,
                        "",
                        DEPT_ID));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("firstName:1以上、50以下の桁数です。")));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

            @DisplayName("first_name：51")
            @Test
            void testNG6() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserCreateRequest(
                        FAMILY_NAME,
                        "123456789012345678901234567890123456789012345678901",
                        DEPT_ID));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("firstName:1以上、50以下の桁数です。")));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

            @DisplayName("request:null")
            @Test
            void testNG7() throws Exception {
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        "903",
                        "リクエスト構造エラーが発生しました",
                        null));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

            @DisplayName("header:null")
            @Test
            void testNG8() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserCreateRequest(
                        FAMILY_NAME,
                        FIRST_NAME,
                        DEPT_ID));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        "904",
                        "ヘッダのエラーが発生しました",
                        null));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

            @DisplayName("dept_id：null")
            @Test
            void testNG9() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserCreateRequest(
                        FAMILY_NAME,
                        FIRST_NAME,
                        null));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("deptId:値は必須です。")));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

            @DisplayName("dept_id：1")
            @Test
            void testNG10() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserCreateRequest(
                        FAMILY_NAME,
                        FIRST_NAME,
                        "1"));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("deptId:部署IDは「2桁の数字」形式です。")));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

            @DisplayName("dept_id：3")
            @Test
            void testNG11() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserCreateRequest(
                        FAMILY_NAME,
                        FIRST_NAME,
                        "123"));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("deptId:部署IDは「2桁の数字」形式です。")));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

            @DisplayName("all")
            @Test
            void testNG12() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserCreateRequest(
                        null,
                        null,
                        null));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("familyName:値は必須です。",
                                "firstName:値は必須です。",
                                "deptId:値は必須です。")));

                // -------------------------------------------------------------
                // テスト実行・実行結果確認
                // -------------------------------------------------------------

                mockMvc.perform(
                        post(URL)
                                .header(OPERATOR_KEY, OPERATOR_VALUE)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseJson));
            }

        }

    }

}
