package com.example.demo.mapper;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;

// NOTE: @MybatisTestで自動的に@Transactionalを設定してくれるのでテスト後に変更したデータが元に戻る
// NOTE: @AutoConfigureTestDatabaseのreplaceで実際のDBを使ってテストする

@Sql(scripts = "/test-data/UserMapper.sql")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {

    private static final String EXIST_BASE_ID = "20250101120055111";
    private static final String NOT_EXIST_BASE_ID = "20241231223023666";
    private static final String BASE_FAMILY_NAME = "苗字";
    private static final String BASE_FIRST_NAME = "名前";
    private static final String DEPT_ID = "01";
    private static final Integer VERSION = 0;

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@Autowired
    @Autowired
    private UserMapper userMapper;

    // NOTE: テーブルの内容を確認するために使う
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        String id = rs.getString("id");
        String familyName = rs.getString("family_name");
        String firstName = rs.getString("first_name");
        String deptId = rs.getString("dept_id");
        Integer version = rs.getInt("version");
        return new User(id, familyName, firstName, deptId, version);
    };

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("insert")
    @Nested
    class Method1 {

        @DisplayName("登録が成功する場合")
        @Test
        void testOK1() {
            User entity = new User(
                    NOT_EXIST_BASE_ID + "_01",
                    BASE_FAMILY_NAME + "1",
                    BASE_FIRST_NAME + "1",
                    DEPT_ID,
                    VERSION);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userMapper.insert(entity);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(1);

            User actual = selectUser(NOT_EXIST_BASE_ID + "_01");
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(NOT_EXIST_BASE_ID + "_01");
            assertThat(actual.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "1");
            assertThat(actual.getFirstName()).isEqualTo(BASE_FIRST_NAME + "1");
            assertThat(actual.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual.getVersion()).isEqualTo(VERSION);
        }

    }

    @DisplayName("insertList")
    @Nested
    class Method2 {

        @DisplayName("すべての登録が成功する場合")
        @Test
        void testOK1() {
            User entity1 = new User(
                    NOT_EXIST_BASE_ID + "_01",
                    BASE_FAMILY_NAME + "1",
                    BASE_FIRST_NAME + "1",
                    DEPT_ID,
                    VERSION);
            User entity2 = new User(
                    NOT_EXIST_BASE_ID + "_02",
                    BASE_FAMILY_NAME + "2",
                    BASE_FIRST_NAME + "2",
                    DEPT_ID,
                    VERSION);
            User entity3 = new User(
                    NOT_EXIST_BASE_ID + "_03",
                    BASE_FAMILY_NAME + "3",
                    BASE_FIRST_NAME + "3",
                    DEPT_ID,
                    VERSION);

            List<User> entityList = List.of(entity1, entity2, entity3);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userMapper.insertList(entityList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(3);

            List<User> actualList = selectUserList(NOT_EXIST_BASE_ID + "_01", NOT_EXIST_BASE_ID + "_02",
                    NOT_EXIST_BASE_ID + "_03");

            assertThat(actualList).hasSize(3);
            User actual1 = actualList.get(0);
            assertThat(actual1.getId()).isEqualTo(NOT_EXIST_BASE_ID + "_01");
            assertThat(actual1.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "1");
            assertThat(actual1.getFirstName()).isEqualTo(BASE_FIRST_NAME + "1");
            assertThat(actual1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual1.getVersion()).isEqualTo(VERSION);
            User actual2 = actualList.get(1);
            assertThat(actual2.getId()).isEqualTo(NOT_EXIST_BASE_ID + "_02");
            assertThat(actual2.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "2");
            assertThat(actual2.getFirstName()).isEqualTo(BASE_FIRST_NAME + "2");
            assertThat(actual2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual2.getVersion()).isEqualTo(VERSION);
            User actual3 = actualList.get(2);
            assertThat(actual3.getId()).isEqualTo(NOT_EXIST_BASE_ID + "_03");
            assertThat(actual3.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "3");
            assertThat(actual3.getFirstName()).isEqualTo(BASE_FIRST_NAME + "3");
            assertThat(actual3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual3.getVersion()).isEqualTo(VERSION);
        }

    }

    @DisplayName("exists")
    @Nested
    class Method3 {

        @DisplayName("存在する場合")
        @Test
        void testOK1() {
            String userId = EXIST_BASE_ID + "_01";

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            boolean result = userMapper.existsById(userId);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(result).isTrue();
        }

        @DisplayName("存在しない場合")
        @Test
        void testOK2() {
            String userId = NOT_EXIST_BASE_ID + "_01";

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            boolean result = userMapper.existsById(userId);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(result).isFalse();
        }

    }

    @DisplayName("existsList")
    @Nested
    class Method4 {

        @DisplayName("すべて存在する場合")
        @Test
        void testOK1() {
            List<String> userIdList = List.of(EXIST_BASE_ID + "_01", EXIST_BASE_ID + "_02", EXIST_BASE_ID + "_03");

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            boolean result = userMapper.existsByIdList(userIdList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(result).isTrue();
        }

        @DisplayName("すべて存在しない場合")
        @Test
        void testOK2() {
            List<String> userIdList = List.of(NOT_EXIST_BASE_ID + "_01", NOT_EXIST_BASE_ID + "_02",
                    NOT_EXIST_BASE_ID + "_03");

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            boolean result = userMapper.existsByIdList(userIdList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(result).isFalse();
        }

        @DisplayName("1つでも存在しない場合")
        @Test
        void testOK3() {
            List<String> userIdList = List.of(EXIST_BASE_ID + "_01", EXIST_BASE_ID + "_02", NOT_EXIST_BASE_ID + "_03");

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            boolean result = userMapper.existsByIdList(userIdList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(result).isFalse();
        }

    }

    @DisplayName("update")
    @Nested
    class Method5 {

        @DisplayName("更新が成功する場合")
        @Test
        void testOK1() {
            User entity = new User(
                    EXIST_BASE_ID + "_01",
                    BASE_FAMILY_NAME + "101",
                    BASE_FIRST_NAME + "101",
                    DEPT_ID,
                    VERSION);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userMapper.update(entity);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(1);

            List<User> actualList = selectUserList(EXIST_BASE_ID + "_01", EXIST_BASE_ID + "_02",
                    EXIST_BASE_ID + "_03");
            assertThat(actualList).hasSize(3);
            User actual1 = actualList.get(0);
            assertThat(actual1.getId()).isEqualTo(EXIST_BASE_ID + "_01");
            assertThat(actual1.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "101");
            assertThat(actual1.getFirstName()).isEqualTo(BASE_FIRST_NAME + "101");
            assertThat(actual1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual1.getVersion()).isEqualTo(VERSION + 1);
            User actual2 = actualList.get(1);
            assertThat(actual2.getId()).isEqualTo(EXIST_BASE_ID + "_02");
            assertThat(actual2.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "2");
            assertThat(actual2.getFirstName()).isEqualTo(BASE_FIRST_NAME + "2");
            assertThat(actual2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual2.getVersion()).isEqualTo(VERSION);
            User actual3 = actualList.get(2);
            assertThat(actual3.getId()).isEqualTo(EXIST_BASE_ID + "_03");
            assertThat(actual3.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "3");
            assertThat(actual3.getFirstName()).isEqualTo(BASE_FIRST_NAME + "3");
            assertThat(actual3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual3.getVersion()).isEqualTo(VERSION);
        }

        @DisplayName("更新が失敗する場合(IDが異なっている)")
        @Test
        void testOK2() {
            User entity = new User(
                    NOT_EXIST_BASE_ID + "_01",
                    BASE_FAMILY_NAME + "101",
                    BASE_FIRST_NAME + "101",
                    DEPT_ID,
                    VERSION);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userMapper.update(entity);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(0);

            List<User> actualList = selectUserList(EXIST_BASE_ID + "_01", EXIST_BASE_ID + "_02",
                    EXIST_BASE_ID + "_03");
            assertThat(actualList).hasSize(3);
            User actual1 = actualList.get(0);
            assertThat(actual1.getId()).isEqualTo(EXIST_BASE_ID + "_01");
            assertThat(actual1.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "1");
            assertThat(actual1.getFirstName()).isEqualTo(BASE_FIRST_NAME + "1");
            assertThat(actual1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual1.getVersion()).isEqualTo(VERSION);
            User actual2 = actualList.get(1);
            assertThat(actual2.getId()).isEqualTo(EXIST_BASE_ID + "_02");
            assertThat(actual2.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "2");
            assertThat(actual2.getFirstName()).isEqualTo(BASE_FIRST_NAME + "2");
            assertThat(actual2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual2.getVersion()).isEqualTo(VERSION);
            User actual3 = actualList.get(2);
            assertThat(actual3.getId()).isEqualTo(EXIST_BASE_ID + "_03");
            assertThat(actual3.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "3");
            assertThat(actual3.getFirstName()).isEqualTo(BASE_FIRST_NAME + "3");
            assertThat(actual3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual3.getVersion()).isEqualTo(VERSION);
        }

        @DisplayName("更新が失敗する場合（Versionが異なっている）")
        @Test
        void testOK3() {
            User entity = new User(
                    EXIST_BASE_ID + "_01",
                    BASE_FAMILY_NAME + "101",
                    BASE_FIRST_NAME + "101",
                    DEPT_ID,
                    VERSION + 100);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userMapper.update(entity);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(0);

            List<User> actualList = selectUserList(EXIST_BASE_ID + "_01", EXIST_BASE_ID + "_02",
                    EXIST_BASE_ID + "_03");
            assertThat(actualList).hasSize(3);
            User actual1 = actualList.get(0);
            assertThat(actual1.getId()).isEqualTo(EXIST_BASE_ID + "_01");
            assertThat(actual1.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "1");
            assertThat(actual1.getFirstName()).isEqualTo(BASE_FIRST_NAME + "1");
            assertThat(actual1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual1.getVersion()).isEqualTo(VERSION);
            User actual2 = actualList.get(1);
            assertThat(actual2.getId()).isEqualTo(EXIST_BASE_ID + "_02");
            assertThat(actual2.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "2");
            assertThat(actual2.getFirstName()).isEqualTo(BASE_FIRST_NAME + "2");
            assertThat(actual2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual2.getVersion()).isEqualTo(VERSION);
            User actual3 = actualList.get(2);
            assertThat(actual3.getId()).isEqualTo(EXIST_BASE_ID + "_03");
            assertThat(actual3.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "3");
            assertThat(actual3.getFirstName()).isEqualTo(BASE_FIRST_NAME + "3");
            assertThat(actual3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual3.getVersion()).isEqualTo(VERSION);
        }

    }

    @DisplayName("updateList")
    @Nested
    class Method6 {

        @DisplayName("すべての更新が成功する場合")
        @Test
        void testOK1() {
            User entity1 = new User(
                    EXIST_BASE_ID + "_01",
                    BASE_FAMILY_NAME + "101",
                    BASE_FIRST_NAME + "101",
                    DEPT_ID,
                    VERSION);
            User entity2 = new User(
                    EXIST_BASE_ID + "_02",
                    BASE_FAMILY_NAME + "102",
                    BASE_FIRST_NAME + "102",
                    DEPT_ID,
                    VERSION);
            User entity3 = new User(
                    EXIST_BASE_ID + "_03",
                    BASE_FAMILY_NAME + "103",
                    BASE_FIRST_NAME + "103",
                    DEPT_ID,
                    VERSION);

            List<User> entityList = List.of(entity1, entity2, entity3);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userMapper.updateList(entityList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(3);

            List<User> actualList = selectUserList(EXIST_BASE_ID + "_01", EXIST_BASE_ID + "_02",
                    EXIST_BASE_ID + "_03");
            assertThat(actualList).hasSize(3);
            User actual1 = actualList.get(0);
            assertThat(actual1.getId()).isEqualTo(EXIST_BASE_ID + "_01");
            assertThat(actual1.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "101");
            assertThat(actual1.getFirstName()).isEqualTo(BASE_FIRST_NAME + "101");
            assertThat(actual1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual1.getVersion()).isEqualTo(VERSION + 1);
            User actual2 = actualList.get(1);
            assertThat(actual2.getId()).isEqualTo(EXIST_BASE_ID + "_02");
            assertThat(actual2.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "102");
            assertThat(actual2.getFirstName()).isEqualTo(BASE_FIRST_NAME + "102");
            assertThat(actual2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual2.getVersion()).isEqualTo(VERSION + 1);
            User actual3 = actualList.get(2);
            assertThat(actual3.getId()).isEqualTo(EXIST_BASE_ID + "_03");
            assertThat(actual3.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "103");
            assertThat(actual3.getFirstName()).isEqualTo(BASE_FIRST_NAME + "103");
            assertThat(actual3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual3.getVersion()).isEqualTo(VERSION + 1);
        }

        @DisplayName("すべての更新が失敗する場合")
        @Test
        void testOK2() {
            User entity1 = new User(
                    NOT_EXIST_BASE_ID + "_01",
                    BASE_FAMILY_NAME + "101",
                    BASE_FIRST_NAME + "101",
                    DEPT_ID,
                    VERSION);
            User entity2 = new User(
                    NOT_EXIST_BASE_ID + "_02",
                    BASE_FAMILY_NAME + "102",
                    BASE_FIRST_NAME + "102",
                    DEPT_ID,
                    VERSION);
            User entity3 = new User(
                    NOT_EXIST_BASE_ID + "_03",
                    BASE_FAMILY_NAME + "103",
                    BASE_FIRST_NAME + "103",
                    DEPT_ID,
                    VERSION);

            List<User> entityList = List.of(entity1, entity2, entity3);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userMapper.updateList(entityList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(0);

            List<User> actualList = selectUserList(EXIST_BASE_ID + "_01", EXIST_BASE_ID + "_02",
                    EXIST_BASE_ID + "_03");
            assertThat(actualList).hasSize(3);
            User actual1 = actualList.get(0);
            assertThat(actual1.getId()).isEqualTo(EXIST_BASE_ID + "_01");
            assertThat(actual1.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "1");
            assertThat(actual1.getFirstName()).isEqualTo(BASE_FIRST_NAME + "1");
            assertThat(actual1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual1.getVersion()).isEqualTo(VERSION);
            User actual2 = actualList.get(1);
            assertThat(actual2.getId()).isEqualTo(EXIST_BASE_ID + "_02");
            assertThat(actual2.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "2");
            assertThat(actual2.getFirstName()).isEqualTo(BASE_FIRST_NAME + "2");
            assertThat(actual2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual2.getVersion()).isEqualTo(VERSION);
            User actual3 = actualList.get(2);
            assertThat(actual3.getId()).isEqualTo(EXIST_BASE_ID + "_03");
            assertThat(actual3.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "3");
            assertThat(actual3.getFirstName()).isEqualTo(BASE_FIRST_NAME + "3");
            assertThat(actual3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual3.getVersion()).isEqualTo(VERSION);
        }

        @DisplayName("1つでも更新が失敗する場合")
        @Test
        void testOK3() {
            User entity1 = new User(
                    EXIST_BASE_ID + "_01",
                    BASE_FAMILY_NAME + "101",
                    BASE_FIRST_NAME + "101",
                    DEPT_ID,
                    VERSION);
            User entity2 = new User(
                    EXIST_BASE_ID + "_02",
                    BASE_FAMILY_NAME + "102",
                    BASE_FIRST_NAME + "102",
                    DEPT_ID,
                    VERSION);
            User entity3 = new User(
                    NOT_EXIST_BASE_ID + "_03",
                    BASE_FAMILY_NAME + "103",
                    BASE_FIRST_NAME + "103",
                    DEPT_ID,
                    VERSION);

            List<User> entityList = List.of(entity1, entity2, entity3);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userMapper.updateList(entityList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(2);

            List<User> actualList = selectUserList(EXIST_BASE_ID + "_01", EXIST_BASE_ID + "_02",
                    EXIST_BASE_ID + "_03");
            assertThat(actualList).hasSize(3);
            User actual1 = actualList.get(0);
            assertThat(actual1.getId()).isEqualTo(EXIST_BASE_ID + "_01");
            assertThat(actual1.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "101");
            assertThat(actual1.getFirstName()).isEqualTo(BASE_FIRST_NAME + "101");
            assertThat(actual1.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual1.getVersion()).isEqualTo(VERSION + 1);
            User actual2 = actualList.get(1);
            assertThat(actual2.getId()).isEqualTo(EXIST_BASE_ID + "_02");
            assertThat(actual2.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "102");
            assertThat(actual2.getFirstName()).isEqualTo(BASE_FIRST_NAME + "102");
            assertThat(actual2.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual2.getVersion()).isEqualTo(VERSION + 1);
            User actual3 = actualList.get(2);
            assertThat(actual3.getId()).isEqualTo(EXIST_BASE_ID + "_03");
            assertThat(actual3.getFamilyName()).isEqualTo(BASE_FAMILY_NAME + "3");
            assertThat(actual3.getFirstName()).isEqualTo(BASE_FIRST_NAME + "3");
            assertThat(actual3.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(actual3.getVersion()).isEqualTo(VERSION);
        }

    }

    private User selectUser(String userId) {
        List<User> userList = jdbcTemplate.query(
                "SELECT * FROM user WHERE id = ?",
                rowMapper,
                userId);
        return userList.size() == 1 ? userList.get(0) : null;
    }

    private List<User> selectUserList(String userId1, String userId2, String userId3) {
        List<User> userList = jdbcTemplate.query(
                "SELECT * FROM user WHERE id IN (?, ?, ?) ORDER BY id ASC",
                rowMapper,
                userId1, userId2, userId3);
        return userList;
    }

}
