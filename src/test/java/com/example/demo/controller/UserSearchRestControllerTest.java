package com.example.demo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.example.demo.common.response.ErrorResponse;
import com.example.demo.common.response.SuccessResponse;
import com.example.demo.service.UserSearchParam;
import com.example.demo.service.UserSearchResult;
import com.example.demo.service.UserSearchResultData;
import com.example.demo.service.UserSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

// NOTE: Controllerのテストはバリデーションとレスポンスが想定通りに動くかだけに観点を置く
// NOTE: Serviceの処理はServiceのテストに切り出すことでテスト観点を明確化する
// NOTE: パラメータの変換が正しく行われるかどうかは各メソッドのテストに切り出すことでテスト観点を明確化する

// NOTE: テスト対象のControllerを@WebMvcTestで設定
@WebMvcTest(UserSearchRestController.class)
class UserSearchRestControllerTest {

    private static final String URL = "/users/search";

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
    private UserSearchRestControllerConverter converter;

    @MockitoBean
    private UserSearchService service;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .registerModule(new JavaTimeModule()
                    .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE)));

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("searchApi")
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
            private static final String DEPT_NAME = "部署名";
            private static final LocalDate LAST_UPDATED_AT = LocalDate.of(2025, 1, 1);
            private static final LocalDate BEGIN_UPDATED_AT = LocalDate.of(2025, 1, 1);
            private static final LocalDate END_UPDATED_AT = LocalDate.of(2025, 12, 31);
            private static final String BASE_USER_ID = "20250101120055111";
            private static final Integer VERSION = 0;

            private static final Integer PAGE_NO = 1;
            private static final Integer PAGE_SIZE = 100;
            private static final Integer PREV_PAGE_NO = null;
            private static final Integer NEXT_PAGE_NO = 2;

            private UserSearchRequest request = null;
            private UserSearchParam param = null;
            private UserSearchResult result = null;
            private UserSearchResponse response = null;

            @BeforeEach
            void setUp() {
                // NOTE: 何をテストしたいのか明確化するため、テストしたい処理以外は共通処理に切り出す

                request = new UserSearchRequest(
                        BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1",
                        DEPT_ID,
                        BEGIN_UPDATED_AT,
                        END_UPDATED_AT,
                        PAGE_NO,
                        PAGE_SIZE);

                param = new UserSearchParam(
                        BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1",
                        DEPT_ID,
                        BEGIN_UPDATED_AT,
                        END_UPDATED_AT,
                        PAGE_NO,
                        PAGE_SIZE);

                result = new UserSearchResult(List.of(
                        new UserSearchResultData(
                                BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1",
                                DEPT_ID,
                                DEPT_NAME,
                                LAST_UPDATED_AT,
                                BASE_USER_ID + "_01",
                                VERSION),
                        new UserSearchResultData(
                                BASE_FAMILY_NAME + "2" + BASE_FIRST_NAME + "2",
                                DEPT_ID,
                                DEPT_NAME,
                                LAST_UPDATED_AT,
                                BASE_USER_ID + "_02",
                                VERSION),
                        new UserSearchResultData(
                                BASE_FAMILY_NAME + "3" + BASE_FIRST_NAME + "3",
                                DEPT_ID,
                                DEPT_NAME,
                                LAST_UPDATED_AT,
                                BASE_USER_ID + "_03",
                                VERSION)),
                        PREV_PAGE_NO,
                        NEXT_PAGE_NO,
                        PAGE_SIZE);

                response = new UserSearchResponse(List.of(
                        new UserSearchResponseData(
                                BASE_FAMILY_NAME + "1" + BASE_FIRST_NAME + "1",
                                DEPT_ID,
                                DEPT_NAME,
                                LAST_UPDATED_AT,
                                BASE_USER_ID + "_01",
                                VERSION),
                        new UserSearchResponseData(
                                BASE_FAMILY_NAME + "2" + BASE_FIRST_NAME + "2",
                                DEPT_ID,
                                DEPT_NAME,
                                LAST_UPDATED_AT,
                                BASE_USER_ID + "_02",
                                VERSION),
                        new UserSearchResponseData(
                                BASE_FAMILY_NAME + "3" + BASE_FIRST_NAME + "3",
                                DEPT_ID,
                                DEPT_NAME,
                                LAST_UPDATED_AT,
                                BASE_USER_ID + "_03",
                                VERSION)),
                        PREV_PAGE_NO,
                        NEXT_PAGE_NO,
                        PAGE_SIZE);
            }

            @DisplayName("正常終了")
            @Test
            void testOK1() throws Exception {
                String requestJson = objectMapper.writeValueAsString(request);
                String responseJson = objectMapper.writeValueAsString(new SuccessResponse(response));

                // -------------------------------------------------------------
                // モックの振る舞い設定
                // -------------------------------------------------------------

                doReturn(param)
                        .when(converter)
                        .convertToParam(any());

                doReturn(result)
                        .when(service)
                        .search(anyString(), any());

                doReturn(response)
                        .when(converter)
                        .convertToResponse(any());

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
                verify(service, times(1)).search(eq(OPERATOR_VALUE), eq(param));
                verify(converter, times(1)).convertToResponse(eq(result));
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
                        .search(anyString(), any());

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
                verify(service, times(1)).search(eq(OPERATOR_VALUE), eq(param));
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
                        .search(anyString(), any());

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
                verify(service, times(1)).search(eq(OPERATOR_VALUE), eq(param));
            }

        }

        @DisplayName("バリデーション")
        @Nested
        class Validation {

            private static final String NAME = "苗字1名前1";
            private static final String DEPT_ID = "01";
            private static final LocalDate BEGIN_UPDATE_AT = LocalDate.of(2025, 1, 1);
            private static final LocalDate END_UPDATED_AT = LocalDate.of(2025, 12, 31);

            private static final Integer PAGE_NO = 1;
            private static final Integer PAGE_SIZE = 100;

            private static final String VALIDATION_ERROR_CODE = "901";
            private static final String VALIDATION_ERROR_MESSAGE = "バリデーションエラーが発生しました";

            @DisplayName("name：0")
            @Test
            void testNG1() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserSearchRequest(
                        "",
                        DEPT_ID,
                        BEGIN_UPDATE_AT,
                        END_UPDATED_AT,
                        PAGE_NO,
                        PAGE_SIZE));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("name:1以上、100以下の桁数です。")));

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

            @DisplayName("name：101")
            @Test
            void testNG2() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserSearchRequest(
                        "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901",
                        DEPT_ID,
                        BEGIN_UPDATE_AT,
                        END_UPDATED_AT,
                        PAGE_NO,
                        PAGE_SIZE));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("name:1以上、100以下の桁数です。")));

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

            @DisplayName("request：null")
            @Test
            void testNG3() throws Exception {
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

            @DisplayName("header：null")
            @Test
            void testNG4() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserSearchRequest(
                        NAME,
                        DEPT_ID,
                        BEGIN_UPDATE_AT,
                        END_UPDATED_AT,
                        PAGE_NO,
                        PAGE_SIZE));
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

            @DisplayName("dept_id：1")
            @Test
            void testNG5() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserSearchRequest(
                        NAME,
                        "1",
                        BEGIN_UPDATE_AT,
                        END_UPDATED_AT,
                        PAGE_NO,
                        PAGE_SIZE));
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
            void testNG6() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserSearchRequest(
                        NAME,
                        "123",
                        BEGIN_UPDATE_AT,
                        END_UPDATED_AT,
                        PAGE_NO,
                        PAGE_SIZE));
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
            void testNG7() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserSearchRequest(
                        "",
                        "",
                        null,
                        null,
                        null,
                        null));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("name:1以上、100以下の桁数です。",
                                "deptId:部署IDは「2桁の数字」形式です。",
                                "pageNo:値は必須です。",
                                "pageSize:値は必須です。")));

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

            @DisplayName("page_no:null")
            @Test
            void testNG8() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserSearchRequest(
                        NAME,
                        DEPT_ID,
                        BEGIN_UPDATE_AT,
                        END_UPDATED_AT,
                        null,
                        PAGE_SIZE));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("pageNo:値は必須です。")));

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

            @DisplayName("page_no:0")
            @Test
            void testNG9() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserSearchRequest(
                        NAME,
                        DEPT_ID,
                        BEGIN_UPDATE_AT,
                        END_UPDATED_AT,
                        0,
                        PAGE_SIZE));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("pageNo:1以上の数値です。")));

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

            @DisplayName("page_size:null")
            @Test
            void testNG10() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserSearchRequest(
                        NAME,
                        DEPT_ID,
                        BEGIN_UPDATE_AT,
                        END_UPDATED_AT,
                        PAGE_NO,
                        null));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("pageSize:値は必須です。")));

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

            @DisplayName("page_size:0")
            @Test
            void testNG11() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserSearchRequest(
                        NAME,
                        DEPT_ID,
                        BEGIN_UPDATE_AT,
                        END_UPDATED_AT,
                        PAGE_NO,
                        0));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("pageSize:1以上の数値です。")));

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

            @DisplayName("page_size:101")
            @Test
            void testNG12() throws Exception {
                String requestJson = objectMapper.writeValueAsString(new UserSearchRequest(
                        NAME,
                        DEPT_ID,
                        BEGIN_UPDATE_AT,
                        END_UPDATED_AT,
                        PAGE_NO,
                        101));
                String responseJson = objectMapper.writeValueAsString(new ErrorResponse(
                        VALIDATION_ERROR_CODE,
                        VALIDATION_ERROR_MESSAGE,
                        List.of("pageSize:100以下の数値です。")));

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