package com.example.demo.mapper;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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

@Sql(scripts = "/test-data/UserSummaryMapper.sql")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserSummaryMapperTest {

    private static final String BASE_NAME = "苗字名前";
    private static final String SEARCHABLE_ID1 = "20250101120055111_01";
    private static final String SEARCHABLE_ID2 = "20250701192423499_01";
    private static final String SEARCHABLE_ID3 = "20251231063011222_01";
    private static final String INSERTABLE_BASE_ID = "20250701094512000";
    private static final String NON_INSERTABLE_BASE_ID = "99999999999999999";
    private static final String UPDATABLE_BASE_ID = "20250701183006000";
    private static final String NON_UPDATABLE_BASE_ID = "99999999999999999";
    private static final String BASE_DEPT_NAME = "部署";
    private static final Integer VERSION = 0;
    private static final LocalDate LAST_UPDATED_AT1 = LocalDate.of(2025, 1, 1);
    private static final LocalDate LAST_UPDATED_AT2 = LocalDate.of(2025, 7, 1);
    private static final LocalDate LAST_UPDATED_AT3 = LocalDate.of(2025, 12, 31);

    // NOTE: テスト対象とモックを定義する
    // NOTE: テストに使用するパラメータとは関係ない定義を外だしすることで見やすくする

    // NOTE: テスト対象のクラスを@Autowired
    @Autowired
    private UserSummaryMapper userSummaryMapper;

    // NOTE: テーブルの内容を確認するために使う
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<UserSummary> rowMapper = (rs, rowNum) -> {
        String name = rs.getString("name");
        String deptId = rs.getString("dept_id");
        String deptName = rs.getString("dept_name");
        LocalDate lastUpdatedAt = rs.getDate("last_updated_at").toLocalDate();
        String userId = rs.getString("user_id");
        Integer userVersion = rs.getInt("user_version");
        return new UserSummary(name, deptId, deptName, lastUpdatedAt, userId, userVersion);
    };

    // NOTE: テストにおいて定義しているクラス名（Method1）やメソッド名（testOK1）は意味を持たないので考える時間をかけないように簡単なものにする
    // NOTE: @DisplayNameでテスト対象のメソッドやテスト内容を書く

    @DisplayName("find")
    @Nested
    class Method1 {

        @DisplayName("name")
        @Test
        void testOK1() {
            UserSummaryCondition condition = new UserSummaryCondition(
                    BASE_NAME + "0",
                    null,
                    null,
                    null);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            List<UserSummary> actualList = userSummaryMapper.find(condition);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(actualList).hasSize(3);
            UserSummary actual1 = actualList.get(0);
            assertThat(actual1.getName()).isEqualTo(BASE_NAME + "0");
            assertThat(actual1.getDeptId()).isEqualTo("01");
            assertThat(actual1.getDeptName()).isEqualTo(BASE_DEPT_NAME + "1");
            assertThat(actual1.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT1);
            assertThat(actual1.getUserId()).isEqualTo(SEARCHABLE_ID1);
            assertThat(actual1.getUserVersion()).isEqualTo(VERSION);
            UserSummary actual2 = actualList.get(1);
            assertThat(actual2.getName()).isEqualTo(BASE_NAME + "0");
            assertThat(actual2.getDeptId()).isEqualTo("01");
            assertThat(actual2.getDeptName()).isEqualTo(BASE_DEPT_NAME + "1");
            assertThat(actual2.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT2);
            assertThat(actual2.getUserId()).isEqualTo(SEARCHABLE_ID2);
            assertThat(actual2.getUserVersion()).isEqualTo(VERSION);
            UserSummary actual3 = actualList.get(2);
            assertThat(actual3.getName()).isEqualTo(BASE_NAME + "0");
            assertThat(actual3.getDeptId()).isEqualTo("01");
            assertThat(actual3.getDeptName()).isEqualTo(BASE_DEPT_NAME + "1");
            assertThat(actual3.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT3);
            assertThat(actual3.getUserId()).isEqualTo(SEARCHABLE_ID3);
            assertThat(actual3.getUserVersion()).isEqualTo(VERSION);
        }

        @DisplayName("beginUpdatedAt")
        @Test
        void testOK2() {
            UserSummaryCondition condition = new UserSummaryCondition(
                    null,
                    null,
                    LAST_UPDATED_AT3,
                    null);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            List<UserSummary> actualList = userSummaryMapper.find(condition);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(actualList).hasSize(1);
            UserSummary actual1 = actualList.get(0);
            assertThat(actual1.getName()).isEqualTo(BASE_NAME + "0");
            assertThat(actual1.getDeptId()).isEqualTo("01");
            assertThat(actual1.getDeptName()).isEqualTo(BASE_DEPT_NAME + "1");
            assertThat(actual1.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT3);
            assertThat(actual1.getUserId()).isEqualTo(SEARCHABLE_ID3);
            assertThat(actual1.getUserVersion()).isEqualTo(VERSION);
        }

        @DisplayName("endUpdatedAt")
        @Test
        void testOK3() {
            UserSummaryCondition condition = new UserSummaryCondition(
                    null,
                    null,
                    null,
                    LAST_UPDATED_AT1);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            List<UserSummary> actualList = userSummaryMapper.find(condition);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(actualList).hasSize(1);
            UserSummary actual1 = actualList.get(0);
            assertThat(actual1.getName()).isEqualTo(BASE_NAME + "0");
            assertThat(actual1.getDeptId()).isEqualTo("01");
            assertThat(actual1.getDeptName()).isEqualTo(BASE_DEPT_NAME + "1");
            assertThat(actual1.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT1);
            assertThat(actual1.getUserId()).isEqualTo(SEARCHABLE_ID1);
            assertThat(actual1.getUserVersion()).isEqualTo(VERSION);
        }

        @DisplayName("all")
        @Test
        void testOK4() {
            UserSummaryCondition condition = new UserSummaryCondition(
                    BASE_NAME + "0",
                    "01",
                    LAST_UPDATED_AT2,
                    LAST_UPDATED_AT2);

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            List<UserSummary> actualList = userSummaryMapper.find(condition);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(actualList).hasSize(1);
            UserSummary actual1 = actualList.get(0);
            assertThat(actual1.getName()).isEqualTo(BASE_NAME + "0");
            assertThat(actual1.getDeptId()).isEqualTo("01");
            assertThat(actual1.getDeptName()).isEqualTo(BASE_DEPT_NAME + "1");
            assertThat(actual1.getLastUpdatedAt()).isEqualTo(LAST_UPDATED_AT2);
            assertThat(actual1.getUserId()).isEqualTo(SEARCHABLE_ID2);
            assertThat(actual1.getUserVersion()).isEqualTo(VERSION);
        }

    }

    @DisplayName("copyFromUser")
    @Nested
    class Method2 {

        private LocalDate now = null;

        @BeforeEach
        void setUp() {
            now = LocalDate.now();
        }

        @DisplayName("コピーが成功する場合")
        @Test
        void testOK1() {
            String userId = INSERTABLE_BASE_ID + "_01";

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userSummaryMapper.copyFromUser(userId);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(1);

            UserSummary actual = selectUserSummary(INSERTABLE_BASE_ID + "_01");
            assertThat(actual).isNotNull();
            assertThat(actual.getName()).isEqualTo(BASE_NAME + "1");
            assertThat(actual.getDeptId()).isEqualTo("02");
            assertThat(actual.getDeptName()).isEqualTo(BASE_DEPT_NAME + "2");
            assertThat(actual.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual.getUserId()).isEqualTo(INSERTABLE_BASE_ID + "_01");
            assertThat(actual.getUserVersion()).isEqualTo(VERSION);
        }

        @DisplayName("コピーが失敗する場合")
        @Test
        void testOK2() {
            String userId = NON_INSERTABLE_BASE_ID + "_01";

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userSummaryMapper.copyFromUser(userId);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(0);

            UserSummary actual = selectUserSummary(NON_INSERTABLE_BASE_ID + "_01");

            assertThat(actual).isNull();
        }

    }

    @DisplayName("copyListFromUser")
    @Nested
    class Method3 {

        private LocalDate now = null;

        @BeforeEach
        void setUp() {
            now = LocalDate.now();
        }

        @DisplayName("すべてのコピーが成功する場合")
        @Test
        void testOK1() {
            List<String> userIdList = List.of(INSERTABLE_BASE_ID + "_01", INSERTABLE_BASE_ID + "_02",
                    INSERTABLE_BASE_ID + "_03");

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userSummaryMapper.copyListFromUser(userIdList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(3);

            List<UserSummary> actualList = selectUserSummaryList(INSERTABLE_BASE_ID + "_01", INSERTABLE_BASE_ID + "_02",
                    INSERTABLE_BASE_ID + "_03");

            assertThat(actualList).hasSize(3);
            UserSummary actual1 = actualList.get(0);
            assertThat(actual1.getName()).isEqualTo(BASE_NAME + "1");
            assertThat(actual1.getDeptId()).isEqualTo("02");
            assertThat(actual1.getDeptName()).isEqualTo(BASE_DEPT_NAME + "2");
            assertThat(actual1.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual1.getUserId()).isEqualTo(INSERTABLE_BASE_ID + "_01");
            assertThat(actual1.getUserVersion()).isEqualTo(VERSION);
            UserSummary actual2 = actualList.get(1);
            assertThat(actual2.getName()).isEqualTo(BASE_NAME + "2");
            assertThat(actual2.getDeptId()).isEqualTo("02");
            assertThat(actual2.getDeptName()).isEqualTo(BASE_DEPT_NAME + "2");
            assertThat(actual2.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual2.getUserId()).isEqualTo(INSERTABLE_BASE_ID + "_02");
            assertThat(actual2.getUserVersion()).isEqualTo(VERSION);
            UserSummary actual3 = actualList.get(2);
            assertThat(actual3.getName()).isEqualTo(BASE_NAME + "3");
            assertThat(actual3.getDeptId()).isEqualTo("02");
            assertThat(actual3.getDeptName()).isEqualTo(BASE_DEPT_NAME + "2");
            assertThat(actual3.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual3.getUserId()).isEqualTo(INSERTABLE_BASE_ID + "_03");
            assertThat(actual3.getUserVersion()).isEqualTo(VERSION);
        }

        @DisplayName("すべてのコピーが失敗する場合")
        @Test
        void testOK2() {
            List<String> userIdList = List.of(NON_INSERTABLE_BASE_ID + "_01", NON_INSERTABLE_BASE_ID + "_02",
                    NON_INSERTABLE_BASE_ID + "_03");

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userSummaryMapper.copyListFromUser(userIdList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(0);

            List<UserSummary> actualList = selectUserSummaryList(NON_INSERTABLE_BASE_ID + "_01",
                    NON_INSERTABLE_BASE_ID + "_02", NON_INSERTABLE_BASE_ID + "_03");

            assertThat(actualList).hasSize(0);
        }

        @DisplayName("1つでもコピーが失敗する場合")
        @Test
        void testOK3() {
            List<String> userIdList = List.of(INSERTABLE_BASE_ID + "_01", INSERTABLE_BASE_ID + "_02",
                    NON_INSERTABLE_BASE_ID + "_01");

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userSummaryMapper.copyListFromUser(userIdList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(2);

            List<UserSummary> actualList = selectUserSummaryList(INSERTABLE_BASE_ID + "_01", INSERTABLE_BASE_ID + "_02",
                    NON_INSERTABLE_BASE_ID + "_01");

            assertThat(actualList).hasSize(2);
            UserSummary actual1 = actualList.get(0);
            assertThat(actual1.getName()).isEqualTo(BASE_NAME + "1");
            assertThat(actual1.getDeptId()).isEqualTo("02");
            assertThat(actual1.getDeptName()).isEqualTo(BASE_DEPT_NAME + "2");
            assertThat(actual1.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual1.getUserId()).isEqualTo(INSERTABLE_BASE_ID + "_01");
            assertThat(actual1.getUserVersion()).isEqualTo(VERSION);
            UserSummary actual2 = actualList.get(1);
            assertThat(actual2.getName()).isEqualTo(BASE_NAME + "2");
            assertThat(actual2.getDeptId()).isEqualTo("02");
            assertThat(actual2.getDeptName()).isEqualTo(BASE_DEPT_NAME + "2");
            assertThat(actual2.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual2.getUserId()).isEqualTo(INSERTABLE_BASE_ID + "_02");
            assertThat(actual2.getUserVersion()).isEqualTo(VERSION);
        }

    }

    @DisplayName("modifyFromUser")
    @Nested
    class Method4 {

        private LocalDate now = null;

        @BeforeEach
        void setUp() {
            now = LocalDate.now();
        }

        @DisplayName("更新が成功する場合")
        @Test
        void testOK1() {
            String userId = UPDATABLE_BASE_ID + "_01";

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userSummaryMapper.modifyFromUser(userId);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(1);

            UserSummary actual = selectUserSummary(UPDATABLE_BASE_ID + "_01");

            assertThat(actual).isNotNull();
            assertThat(actual.getName()).isEqualTo(BASE_NAME + "101");
            assertThat(actual.getDeptId()).isEqualTo("03");
            assertThat(actual.getDeptName()).isEqualTo(BASE_DEPT_NAME + "3");
            assertThat(actual.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual.getUserId()).isEqualTo(UPDATABLE_BASE_ID + "_01");
            assertThat(actual.getUserVersion()).isEqualTo(VERSION + 1);
        }

        @DisplayName("更新が失敗する場合")
        @Test
        void testOK2() {
            String userId = NON_UPDATABLE_BASE_ID + "_01";

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userSummaryMapper.modifyFromUser(userId);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(0);

            UserSummary actual = selectUserSummary(NON_UPDATABLE_BASE_ID + "_01");

            assertThat(actual).isNull();
        }

    }

    @DisplayName("modifyListFromUser")
    @Nested
    class Method5 {

        private LocalDate now = null;

        @BeforeEach
        void setUp() {
            now = LocalDate.now();
        }

        @DisplayName("すべての更新が成功する場合")
        @Test
        void testOK1() {
            List<String> userIdList = List.of(UPDATABLE_BASE_ID + "_01", UPDATABLE_BASE_ID + "_02",
                    UPDATABLE_BASE_ID + "_03");

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userSummaryMapper.modifyListFromUser(userIdList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(3);

            List<UserSummary> actualList = selectUserSummaryList(UPDATABLE_BASE_ID + "_01", UPDATABLE_BASE_ID + "_02",
                    UPDATABLE_BASE_ID + "_03");

            assertThat(actualList).hasSize(3);
            UserSummary actual1 = actualList.get(0);
            assertThat(actual1.getName()).isEqualTo(BASE_NAME + "101");
            assertThat(actual1.getDeptId()).isEqualTo("03");
            assertThat(actual1.getDeptName()).isEqualTo(BASE_DEPT_NAME + "3");
            assertThat(actual1.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual1.getUserId()).isEqualTo(UPDATABLE_BASE_ID + "_01");
            assertThat(actual1.getUserVersion()).isEqualTo(VERSION + 1);
            UserSummary actual2 = actualList.get(1);
            assertThat(actual2.getName()).isEqualTo(BASE_NAME + "102");
            assertThat(actual2.getDeptId()).isEqualTo("03");
            assertThat(actual2.getDeptName()).isEqualTo(BASE_DEPT_NAME + "3");
            assertThat(actual2.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual2.getUserId()).isEqualTo(UPDATABLE_BASE_ID + "_02");
            assertThat(actual2.getUserVersion()).isEqualTo(VERSION + 1);
            UserSummary actual3 = actualList.get(2);
            assertThat(actual3.getName()).isEqualTo(BASE_NAME + "103");
            assertThat(actual3.getDeptId()).isEqualTo("03");
            assertThat(actual3.getDeptName()).isEqualTo(BASE_DEPT_NAME + "3");
            assertThat(actual3.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual3.getUserId()).isEqualTo(UPDATABLE_BASE_ID + "_03");
            assertThat(actual3.getUserVersion()).isEqualTo(VERSION + 1);
        }

        @DisplayName("すべての更新が失敗する場合")
        @Test
        void testOK2() {
            List<String> userIdList = List.of(NON_UPDATABLE_BASE_ID + "_01", NON_UPDATABLE_BASE_ID + "_02",
                    NON_UPDATABLE_BASE_ID + "_03");

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userSummaryMapper.modifyListFromUser(userIdList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(0);

            List<UserSummary> actualList = selectUserSummaryList(NON_UPDATABLE_BASE_ID + "_01",
                    NON_UPDATABLE_BASE_ID + "_02", NON_UPDATABLE_BASE_ID + "_03");

            assertThat(actualList).hasSize(0);
        }

        @DisplayName("1つでも更新が失敗する場合")
        @Test
        void testOK3() {
            List<String> userIdList = List.of(UPDATABLE_BASE_ID + "_01", UPDATABLE_BASE_ID + "_02",
                    NON_UPDATABLE_BASE_ID + "_01");

            // -----------------------------------------------------------------
            // テスト実行
            // -----------------------------------------------------------------

            int count = userSummaryMapper.modifyListFromUser(userIdList);

            // -----------------------------------------------------------------
            // 実行結果確認
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(2);

            List<UserSummary> actualList = selectUserSummaryList(UPDATABLE_BASE_ID + "_01", UPDATABLE_BASE_ID + "_02",
                    NON_UPDATABLE_BASE_ID + "_01");

            assertThat(actualList).hasSize(2);
            UserSummary actual1 = actualList.get(0);
            assertThat(actual1.getName()).isEqualTo(BASE_NAME + "101");
            assertThat(actual1.getDeptId()).isEqualTo("03");
            assertThat(actual1.getDeptName()).isEqualTo(BASE_DEPT_NAME + "3");
            assertThat(actual1.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual1.getUserId()).isEqualTo(UPDATABLE_BASE_ID + "_01");
            assertThat(actual1.getUserVersion()).isEqualTo(VERSION + 1);
            UserSummary actual2 = actualList.get(1);
            assertThat(actual2.getName()).isEqualTo(BASE_NAME + "102");
            assertThat(actual2.getDeptId()).isEqualTo("03");
            assertThat(actual2.getDeptName()).isEqualTo(BASE_DEPT_NAME + "3");
            assertThat(actual2.getLastUpdatedAt()).isEqualTo(now);
            assertThat(actual2.getUserId()).isEqualTo(UPDATABLE_BASE_ID + "_02");
            assertThat(actual2.getUserVersion()).isEqualTo(VERSION + 1);
        }

    }

    private UserSummary selectUserSummary(String userId) {
        List<UserSummary> userSummaryList = jdbcTemplate.query(
                "SELECT * FROM user_summary WHERE user_id = ?",
                rowMapper,
                userId);
        return userSummaryList.size() == 1 ? userSummaryList.get(0) : null;
    }

    private List<UserSummary> selectUserSummaryList(String userId1, String userId2, String userId3) {
        List<UserSummary> userSummaryList = jdbcTemplate.query(
                "SELECT * FROM user_summary WHERE user_id IN (?, ?, ?) ORDER BY user_id ASC",
                rowMapper,
                userId1, userId2, userId3);
        return userSummaryList;
    }

}
