package com.example.demo.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class DepartmentRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("正常終了")
    @Test
    void testOK1() throws Exception {
        mockMvc.perform(
                get("/departments")
                        .header("X-Operator", "OPERATOR"))
        .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "status": "success",
                          "code": null,
                          "message": null,
                          "data": {
                            "list": [
                              {
                                "id": "01",
                                "name": "部署1",
                                "is_deleted": false
                              },
                              {
                                "id": "02",
                                "name": "部署2",
                                "is_deleted": false
                              },
                              {
                                "id": "03",
                                "name": "部署3",
                                "is_deleted": false
                              },
                              {
                                "id": "04",
                                "name": "部署4",
                                "is_deleted": false
                              },
                              {
                                "id": "05",
                                "name": "部署5",
                                "is_deleted": false
                              },
                              {
                                "id": "06",
                                "name": "部署6",
                                "is_deleted": false
                              },
                              {
                                "id": "07",
                                "name": "部署7",
                                "is_deleted": false
                              },
                              {
                                "id": "08",
                                "name": "部署8",
                                "is_deleted": false
                              },
                              {
                                "id": "09",
                                "name": "部署9",
                                "is_deleted": false
                              },
                              {
                                "id": "10",
                                "name": "部署10",
                                "is_deleted": false
                              }
                            ]
                          }
                        }
                        """));

    }

}
