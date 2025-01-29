package com.example.demo.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "/test-data/Integration_bulkCreate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class UserBulkCreateRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("正常終了")
    @Test
    void testOK1() throws Exception {
        mockMvc.perform(
                post("/users/bulk/create")
                        .header("X-Operator", "OPERATOR")
                        .content("""
                                {
                                  "list": [
                                    {
                                      "family_name": "苗字1",
                                      "first_name": "名前1",
                                      "dept_id": "01"
                                    },
                                    {
                                      "family_name": "苗字2",
                                      "first_name": "名前2",
                                      "dept_id": "01"
                                    },
                                    {
                                      "family_name": "苗字3",
                                      "first_name": "名前3",
                                      "dept_id": "01"
                                    }
                                  ]
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "status": "success",
                          "code": null,
                          "message": null,
                          "data": null
                        }
                        """));
    }

    @DisplayName("異常終了：401")
    @Test
    void testNG1() throws Exception {
        mockMvc.perform(
                post("/users/bulk/create")
                        .header("X-Operator", "OPERATOR")
                        .content("""
                                {
                                  "list": [
                                    {
                                      "family_name": "苗字1",
                                      "first_name": "名前1",
                                      "dept_id": "97"
                                    },
                                    {
                                      "family_name": "苗字2",
                                      "first_name": "名前2",
                                      "dept_id": "98"
                                    },
                                    {
                                      "family_name": "苗字3",
                                      "first_name": "名前3",
                                      "dept_id": "99"
                                    }
                                  ]
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("""
                        {
                          "status": "error",
                          "code": "401",
                          "message": "登録に使用する部署が存在しません：97,98,99",
                          "data": null
                        }
                        """));
    }

    @DisplayName("異常終了：101")
    @Test
    void testNG2() throws Exception {
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 12, 0, 55, 111000000);

        try (MockedStatic<LocalDateTime> mock = Mockito.mockStatic(LocalDateTime.class)) {

            mock.when(LocalDateTime::now).thenReturn(now);

            mockMvc.perform(
                    post("/users/bulk/create")
                            .header("X-Operator", "OPERATOR")
                            .content("""
                                    {
                                      "list": [
                                        {
                                          "family_name": "苗字1",
                                          "first_name": "名前1",
                                          "dept_id": "01"
                                        },
                                        {
                                          "family_name": "苗字2",
                                          "first_name": "名前2",
                                          "dept_id": "01"
                                        },
                                        {
                                          "family_name": "苗字3",
                                          "first_name": "名前3",
                                          "dept_id": "01"
                                        }
                                      ]
                                    }
                                    """)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content()
                            .json("""
                                    {
                                      "status": "error",
                                      "code": "101",
                                      "message": "登録対象のデータが既に登録されています：20250101120055111_01,20250101120055111_02,20250101120055111_03",
                                      "data": null
                                    }
                                    """));
        }
    }

}
