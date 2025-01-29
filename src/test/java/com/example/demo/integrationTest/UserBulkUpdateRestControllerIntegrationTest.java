package com.example.demo.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "/test-data/Integration_bulkUpdate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class UserBulkUpdateRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("正常終了")
    @Test
    void testOK1() throws Exception {
        mockMvc.perform(
                post("/users/bulk/update")
                        .header("X-Operator", "OPERATOR")
                        .content("""
                                {
                                  "list": [
                                    {
                                      "id": "20250101120055111_01",
                                      "family_name": "苗字101",
                                      "first_name": "名前101",
                                      "dept_id": "01",
                                      "version": 0
                                    },
                                    {
                                      "id": "20250101120055111_02",
                                      "family_name": "苗字102",
                                      "first_name": "名前102",
                                      "dept_id": "01",
                                      "version": 0
                                    },
                                    {
                                      "id": "20250101120055111_03",
                                      "family_name": "苗字103",
                                      "first_name": "名前103",
                                      "dept_id": "01",
                                      "version": 0
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

    @DisplayName("異常終了：402")
    @Test
    void testNG1() throws Exception {
        mockMvc.perform(
                post("/users/bulk/update")
                        .header("X-Operator", "OPERATOR")
                        .content("""
                                {
                                  "list": [
                                    {
                                      "id": "20250101120055111_01",
                                      "family_name": "苗字101",
                                      "first_name": "名前101",
                                      "dept_id": "97",
                                      "version": 0
                                    },
                                    {
                                      "id": "20250101120055111_02",
                                      "family_name": "苗字102",
                                      "first_name": "名前102",
                                      "dept_id": "98",
                                      "version": 0
                                    },
                                    {
                                      "id": "20250101120055111_03",
                                      "family_name": "苗字103",
                                      "first_name": "名前103",
                                      "dept_id": "99",
                                      "version": 0
                                    }
                                  ]
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("""
                        {
                          "status": "error",
                          "code": "402",
                          "message": "更新に使用する部署が存在しません：97,98,99",
                          "data": null
                        }
                        """));
    }

    @DisplayName("異常終了：102")
    @Test
    void testNG2() throws Exception {
        mockMvc.perform(
                post("/users/bulk/update")
                        .header("X-Operator", "OPERATOR")
                        .content("""
                                {
                                  "list": [
                                    {
                                      "id": "99999999999999999_97",
                                      "family_name": "苗字101",
                                      "first_name": "名前101",
                                      "dept_id": "01",
                                      "version": 0
                                    },
                                    {
                                      "id": "99999999999999999_98",
                                      "family_name": "苗字102",
                                      "first_name": "名前102",
                                      "dept_id": "01",
                                      "version": 0
                                    },
                                    {
                                      "id": "99999999999999999_99",
                                      "family_name": "苗字103",
                                      "first_name": "名前103",
                                      "dept_id": "01",
                                      "version": 0
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
                                  "code": "102",
                                  "message": "更新対象のデータが登録されていません：99999999999999999_97,99999999999999999_98,99999999999999999_99",
                                  "data": null
                                }
                                """));
    }

    @DisplayName("異常終了：202")
    @Test
    void testNG3() throws Exception {
        mockMvc.perform(
                post("/users/bulk/update")
                        .header("X-Operator", "OPERATOR")
                        .content("""
                                {
                                  "list": [
                                    {
                                      "id": "20250101120055111_01",
                                      "family_name": "苗字101",
                                      "first_name": "名前101",
                                      "dept_id": "01",
                                      "version": 999
                                    },
                                    {
                                      "id": "20250101120055111_02",
                                      "family_name": "苗字102",
                                      "first_name": "名前102",
                                      "dept_id": "01",
                                      "version": 999
                                    },
                                    {
                                      "id": "20250101120055111_03",
                                      "family_name": "苗字103",
                                      "first_name": "名前103",
                                      "dept_id": "01",
                                      "version": 999
                                    }
                                  ]
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("""
                        {
                          "status": "error",
                          "code": "202",
                          "message": "更新に失敗しました：20250101120055111_01,20250101120055111_02,20250101120055111_03",
                          "data": null
                        }
                        """));
    }

}
