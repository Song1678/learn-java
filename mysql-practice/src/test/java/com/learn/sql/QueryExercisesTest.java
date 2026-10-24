package com.learn.sql;

import com.learn.sql.support.SqlTestSupport;
import com.learn.sql.support.SqlTestSupport.Expected;
import com.learn.sql.support.SqlTestSupport.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 查询类练习（m02 / m03 / m04 / m07）的判题：
 * 每道题对应 expected/ 目录下的一个 .tsv 期望结果文件。
 *
 * <p>只跑某一章：mvn test -Dtest='QueryExercisesTest#m02*'
 */
@DisplayName("查询练习")
class QueryExercisesTest {

    @TestFactory
    @DisplayName("m02 基础查询")
    List<DynamicTest> m02() {
        return chapter("m02_select");
    }

    @TestFactory
    @DisplayName("m03 函数与聚合")
    List<DynamicTest> m03() {
        return chapter("m03_aggregate");
    }

    @TestFactory
    @DisplayName("m04 多表查询")
    List<DynamicTest> m04() {
        return chapter("m04_join");
    }

    @TestFactory
    @DisplayName("m07 综合实战")
    List<DynamicTest> m07() {
        return chapter("m07_report");
    }

    private List<DynamicTest> chapter(String chapter) {
        SqlTestSupport.resetDatabase();
        return SqlTestSupport.expectedFiles(chapter).stream()
                .map(tsv -> DynamicTest.dynamicTest(name(tsv), () -> check(chapter, tsv)))
                .toList();
    }

    private static String name(Path tsv) {
        return tsv.getFileName().toString().replace(".tsv", "");
    }

    private void check(String chapter, Path tsv) throws SQLException {
        Expected expected = SqlTestSupport.readExpected(tsv);
        // 每道题都在全新的数据上执行，互不影响
        SqlTestSupport.resetDatabase();
        try (Connection conn = SqlTestSupport.connection()) {
            Result actual = SqlTestSupport.runAnswer(conn, chapter + "/" + name(tsv) + ".sql");
            SqlTestSupport.assertResult(expected.result(), actual, expected.ordered());
        }
    }
}
