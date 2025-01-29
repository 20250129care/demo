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

@Sql(scripts = "/test-data/Integration_search.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class UserSearchRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("正常終了")
    @Test
    void testOK1() throws Exception {
        mockMvc.perform(
                post("/users/search")
                        .header("X-Operator", "OPERATOR")
                        .content("""
                                {
                                  "name": "苗字名前",
                                  "dept_id": "01",
                                  "begin_updated_at": "2025-01-01",
                                  "end_updated_at": "2025-01-01"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "status": "success",
                          "code": null,
                          "message": null,
                          "data": {
                            "list": [
                              {
                                "name": "苗字名前",
                                "dept_id": "01",
                                "dept_name": "部署1",
                                "last_updated_at": "2025-01-01",
                                "user_id": "20250101120055111_01",
                                "user_version": 0
                              },
                              {
                                "name": "苗字名前",
                                "dept_id": "01",
                                "dept_name": "部署1",
                                "last_updated_at": "2025-01-01",
                                "user_id": "20250101120055111_02",
                                "user_version": 0
                              },
                              {
                                "name": "苗字名前",
                                "dept_id": "01",
                                "dept_name": "部署1",
                                "last_updated_at": "2025-01-01",
                                "user_id": "20250101120055111_03",
                                "user_version": 0
                              }
                            ]
                          }
                        }
                        """));
    }

}
