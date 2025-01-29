package com.example.demo.mapper;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

// NOTE: @MybatisTestで自動的に@Transactionalを設定してくれるのでテスト後に変更したデータが元に戻る
// NOTE: @AutoConfigureTestDatabaseのreplaceで実際のDBを使ってテストする

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DepartmentMapperTest {

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@Autowired
    @Autowired
    private DepartmentMapper departmentMapper;

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("existsById")
    @Nested
    class Method1 {

        @DisplayName("正常終了:存在する場合")
        @Test
        void testOK1() throws Exception {
            boolean result = departmentMapper.existsById("01");
            assertThat(result).isTrue();
        }

        @DisplayName("正常終了:存在しない場合")
        @Test
        void testOK2() throws Exception {
            boolean result = departmentMapper.existsById("99");
            assertThat(result).isFalse();
        }

    }

    @DisplayName("existsByIdList")
    @Nested
    class Method2 {

        @DisplayName("正常終了:すべて存在する場合")
        @Test
        void testOK1() throws Exception {
            boolean result = departmentMapper.existsByIdList(List.of("01", "02", "03"));
            assertThat(result).isTrue();
        }

        @DisplayName("正常終了:すべて存在しない場合")
        @Test
        void testOK2() throws Exception {
            boolean result = departmentMapper.existsByIdList(List.of("97", "98", "99"));
            assertThat(result).isFalse();
        }

        @DisplayName("正常終了:1つでも存在しない場合")
        @Test
        void testOK3() throws Exception {
            boolean result = departmentMapper.existsByIdList(List.of("01", "02", "99"));
            assertThat(result).isFalse();
        }

    }

    @DisplayName("findAll")
    @Nested
    class Method3 {

        @DisplayName("正常終了")
        @Test
        void testOK1() throws Exception {
            List<Department> result = departmentMapper.findAll();
            assertThat(result).hasSize(10);
        }

    }

}
