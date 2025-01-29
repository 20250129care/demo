package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    // NOTE: カバレッジを100%にするためのテスト

    @Test
    void testMain() {
        String[] args = {};
        DemoApplication.main(args);
    }

}
