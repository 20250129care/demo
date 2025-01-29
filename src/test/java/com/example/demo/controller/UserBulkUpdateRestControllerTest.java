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
import com.example.demo.service.UserBulkUpdateParam;
import com.example.demo.service.UserBulkUpdateService;
import com.example.demo.service.UserUpdateParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

// NOTE: Controllerのテストはバリデーションとレスポンスが想定通りに動くかだけに観点を置く
// NOTE: Serviceの処理はServiceのテストに切り出すことでテスト観点を明確化する
// NOTE: パラメータの変換が正しく行われるかどうかは各メソッドのテストに切り出すことでテスト観点を明確化する

// NOTE: テスト対象のControllerを@WebMvcTestで設定
@WebMvcTest(UserBulkUpdateRestController.class)
class UserBulkUpdateRestControllerTest {

    private static final String URL = "/users/bulk/update";

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
    private UserBulkUpdateRestControllerConverter converter;

    @MockitoBean
    private UserBulkUpdateService service;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("updateApi")
    @Nested
    class Method1 {

        @DisplayName("業務処理")
        @Nested
        class Application {

            // NOTE: 対象のメソッドをテストする際に使用するパラメータを定義する
            // NOTE: メソッドごとに必要なパラメータが異なるので、このメソッドに必要なパラメータが明確化される

            private static final String BASE_ID = "20250101120055111";
            private static final String BASE_FAMILY_NAME = "苗字";
            private static final String BASE_FIRST_NAME = "名前";
            private static final String DEPT_ID = "01";
            private static final Integer VERSION = 0;

            private UserBulkUpdateRequest bulkRequest = null;
            private UserBulkUpdateParam bulkParam = null;

            @BeforeEach
            void setUp() {
                // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

                bulkRequest = new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                VERSION)));

                bulkParam = new UserBulkUpdateParam(List.of(
                        new UserUpdateParam(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateParam(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateParam(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                VERSION)));
            }

            @DisplayName("正常終了")
            @Test
            void testOK1() throws Exception {
                String requestJson = objectMapper.writeValueAsString(bulkRequest);
                String responseJson = objectMapper.writeValueAsString(new SuccessResponse());

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doReturn(bulkParam)
                        .when(converter)
                        .convertToParam(any());

                doNothing()
                        .when(service)
                        .bulkUpdate(anyString(), any());

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
                verify(converter, times(1)).convertToParam(eq(bulkRequest));
                verify(service, times(1)).bulkUpdate(eq(OPERATOR_VALUE), eq(bulkParam));
            }

            @DisplayName("異常終了：RuntimeException")
            @Test
            void testNG1() throws Exception {
                String requestJson = objectMapper.writeValueAsString(bulkRequest);
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        "999",
                        "想定外の例外が発生しました"));

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doReturn(bulkParam)
                        .when(converter)
                        .convertToParam(any());

                doThrow(RuntimeException.class)
                        .when(service)
                        .bulkUpdate(anyString(), any());

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
                verify(converter, times(1)).convertToParam(eq(bulkRequest));
                verify(service, times(1)).bulkUpdate(eq(OPERATOR_VALUE), eq(bulkParam));
            }

            @DisplayName("異常終了：DataAccessException")
            @Test
            void testNG2() throws Exception {
                String requestJson = objectMapper.writeValueAsString(bulkRequest);
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        "902",
                        "DBエラーが発生しました"));

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doReturn(bulkParam)
                        .when(converter)
                        .convertToParam(any());

                doThrow(new DataAccessException("") {
                })
                        .when(service)
                        .bulkUpdate(anyString(), any());

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
                verify(converter, times(1)).convertToParam(eq(bulkRequest));
                verify(service, times(1)).bulkUpdate(eq(OPERATOR_VALUE), eq(bulkParam));
            }

            @DisplayName("異常終了：ApplicationException")
            @Test
            void testNG3() throws Exception {
                String requestJson = objectMapper.writeValueAsString(bulkRequest);
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        "100",
                        "テストメッセージ"));

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doReturn(bulkParam)
                        .when(converter)
                        .convertToParam(any());

                doThrow(new ApplicationException("100", "テストメッセージ"))
                        .when(service)
                        .bulkUpdate(anyString(), any());

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
                verify(converter, times(1)).convertToParam(eq(bulkRequest));
                verify(service, times(1)).bulkUpdate(eq(OPERATOR_VALUE), eq(bulkParam));
            }

        }

        @DisplayName("バリデーション")
        @Nested
        class Validation {

            private static final String BASE_ID = "20250101120055111";
            private static final String BASE_FAMILY_NAME = "苗字";
            private static final String BASE_FIRST_NAME = "名前";
            private static final String DEPT_ID = "01";
            private static final Integer VERSION = 0;

            private static final String VALIDATION_ERROR_CODE = "901";
            private static final String VALIDATION_ERROR_MESSAGE = "バリデーションエラーが発生しました";

            @DisplayName("id：null")
            @Test
            void testNG1() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                null,
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                null,
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                null,
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].id:値は必須です。",
                                "list[1].id:値は必須です。",
                                "list[2].id:値は必須です。")));

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

            @DisplayName("id：19")
            @Test
            void testNG2() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                "1234567890123456789",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                "1234567890123456789",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                "1234567890123456789",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].id:ユーザIDは「17桁の数字_2桁の数字」形式です。",
                                "list[1].id:ユーザIDは「17桁の数字_2桁の数字」形式です。",
                                "list[2].id:ユーザIDは「17桁の数字_2桁の数字」形式です。")));

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

            @DisplayName("id：21")
            @Test
            void testNG3() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                "123456789012345678901",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                "123456789012345678901",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                "123456789012345678901",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].id:ユーザIDは「17桁の数字_2桁の数字」形式です。",
                                "list[1].id:ユーザIDは「17桁の数字_2桁の数字」形式です。",
                                "list[2].id:ユーザIDは「17桁の数字_2桁の数字」形式です。")));

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

            @DisplayName("family_name：null")
            @Test
            void testNG4() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                null,
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                null,
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                null,
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].familyName:値は必須です。",
                                "list[1].familyName:値は必須です。",
                                "list[2].familyName:値は必須です。")));

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
            void testNG5() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                "",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                "",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                "",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].familyName:1以上、50以下の桁数です。",
                                "list[1].familyName:1以上、50以下の桁数です。",
                                "list[2].familyName:1以上、50以下の桁数です。")));

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
            void testNG6() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                "123456789012345678901234567890123456789012345678901",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                "123456789012345678901234567890123456789012345678901",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                "123456789012345678901234567890123456789012345678901",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].familyName:1以上、50以下の桁数です。",
                                "list[1].familyName:1以上、50以下の桁数です。",
                                "list[2].familyName:1以上、50以下の桁数です。")));

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
            void testNG7() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                null,
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                null,
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                null,
                                DEPT_ID,
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].firstName:値は必須です。",
                                "list[1].firstName:値は必須です。",
                                "list[2].firstName:値は必須です。")));

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
            void testNG8() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                "",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                "",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                "",
                                DEPT_ID,
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].firstName:1以上、50以下の桁数です。",
                                "list[1].firstName:1以上、50以下の桁数です。",
                                "list[2].firstName:1以上、50以下の桁数です。")));

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
            void testNG9() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                "123456789012345678901234567890123456789012345678901",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                "123456789012345678901234567890123456789012345678901",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                "123456789012345678901234567890123456789012345678901",
                                DEPT_ID,
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].firstName:1以上、50以下の桁数です。",
                                "list[1].firstName:1以上、50以下の桁数です。",
                                "list[2].firstName:1以上、50以下の桁数です。")));

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

            @DisplayName("version：null")
            @Test
            void testNG10() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                null),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                null),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                null))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].version:値は必須です。",
                                "list[1].version:値は必須です。",
                                "list[2].version:値は必須です。")));

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

            @DisplayName("version：max")
            @Test
            void testNG11() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                1000),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                1000),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                1000))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].version:999以下の数値です。",
                                "list[1].version:999以下の数値です。",
                                "list[2].version:999以下の数値です。")));

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

            @DisplayName("version：min")
            @Test
            void testNG12() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                -1),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                -1),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                -1))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].version:0以上の数値です。",
                                "list[1].version:0以上の数値です。",
                                "list[2].version:0以上の数値です。")));

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
            void testNG13() throws Exception {
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
            void testNG14() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                VERSION))));
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

            @DisplayName("list：null")
            @Test
            void testNG15() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(null));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list:値は必須です。")));

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

            @DisplayName("list：0")
            @Test
            void testNG16() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of()));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list:1以上、10以下の長さです。")));

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

            @DisplayName("list：11")
            @Test
            void testNG17() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_04",
                                BASE_FAMILY_NAME + "104",
                                BASE_FIRST_NAME + "104",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_05",
                                BASE_FAMILY_NAME + "105",
                                BASE_FIRST_NAME + "105",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_06",
                                BASE_FAMILY_NAME + "106",
                                BASE_FIRST_NAME + "106",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_07",
                                BASE_FAMILY_NAME + "107",
                                BASE_FIRST_NAME + "107",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_08",
                                BASE_FAMILY_NAME + "108",
                                BASE_FIRST_NAME + "108",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_09",
                                BASE_FAMILY_NAME + "109",
                                BASE_FIRST_NAME + "109",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_10",
                                BASE_FAMILY_NAME + "110",
                                BASE_FIRST_NAME + "110",
                                DEPT_ID,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_11",
                                BASE_FAMILY_NAME + "111",
                                BASE_FIRST_NAME + "111",
                                DEPT_ID,
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list:1以上、10以下の長さです。")));

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

            @DisplayName("dept_id：null")
            @Test
            void testNG18() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                null,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                null,
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                null,
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].deptId:値は必須です。",
                                "list[1].deptId:値は必須です。",
                                "list[2].deptId:値は必須です。")));

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
            void testNG19() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                "1",
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                "1",
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                "1",
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].deptId:部署IDは「2桁の数字」形式です。",
                                "list[1].deptId:部署IDは「2桁の数字」形式です。",
                                "list[2].deptId:部署IDは「2桁の数字」形式です。")));

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
            void testNG20() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                BASE_ID + "_01",
                                BASE_FAMILY_NAME + "101",
                                BASE_FIRST_NAME + "101",
                                "123",
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_02",
                                BASE_FAMILY_NAME + "102",
                                BASE_FIRST_NAME + "102",
                                "123",
                                VERSION),
                        new UserUpdateRequest(
                                BASE_ID + "_03",
                                BASE_FAMILY_NAME + "103",
                                BASE_FIRST_NAME + "103",
                                "123",
                                VERSION))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].deptId:部署IDは「2桁の数字」形式です。",
                                "list[1].deptId:部署IDは「2桁の数字」形式です。",
                                "list[2].deptId:部署IDは「2桁の数字」形式です。")));

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
            void testNG21() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserBulkUpdateRequest(List.of(
                        new UserUpdateRequest(
                                null,
                                null,
                                null,
                                null,
                                null),
                        new UserUpdateRequest(
                                null,
                                null,
                                null,
                                null,
                                null),
                        new UserUpdateRequest(
                                null,
                                null,
                                null,
                                null,
                                null))));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("list[0].id:値は必須です。",
                                "list[0].familyName:値は必須です。",
                                "list[0].firstName:値は必須です。",
                                "list[0].deptId:値は必須です。",
                                "list[0].version:値は必須です。",
                                "list[1].id:値は必須です。",
                                "list[1].familyName:値は必須です。",
                                "list[1].firstName:値は必須です。",
                                "list[1].deptId:値は必須です。",
                                "list[1].version:値は必須です。",
                                "list[2].id:値は必須です。",
                                "list[2].familyName:値は必須です。",
                                "list[2].firstName:値は必須です。",
                                "list[2].deptId:値は必須です。",
                                "list[2].version:値は必須です。")));

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
