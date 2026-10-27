package com.learn.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.learn.web.support.WebTestBase;
import com.learn.web.w04_exception.EmployeeService;
import com.learn.web.w04_exception.validate.StatusValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("w04 统一异常处理与参数校验")
class W04ExceptionTest extends WebTestBase {

    @Autowired
    EmployeeService employeeService;

    @BeforeEach
    void reset() {
        employeeService.reset();
    }

    private static final String VALID = """
            {"username": "zhangxc", "name": "张小厨", "password": "123456", "phone": "13900000001"}
            """;

    @Test
    @DisplayName("4.1 ServiceException → R.fail(code, msg)")
    void serviceException() {
        assertEquals(2, assertOk(postJson("/w04/employees", VALID)).asInt());
        assertFail(500, "账号已存在", postJson("/w04/employees", VALID));
        assertFail(404, "员工不存在", getJson("/w04/employees/99"));
    }

    @Test
    @DisplayName("4.1 缺少参数 / JSON 格式错误 / 未知异常")
    void otherExceptions() {
        assertEquals("搜索：张", assertOk(getJson("/w04/employees/search?keyword=张")).asText());
        assertFail(400, "缺少必需的请求参数: keyword", getJson("/w04/employees/search"));
        assertFail(400, "请求体格式错误", postJson("/w04/employees", "{\"username\": "));
        assertFail(500, "系统繁忙，请稍后再试", getJson("/w04/employees/boom"));
    }

    @Test
    @DisplayName("4.2 + 4.3 新增时的校验")
    void addValidation() {
        assertFail(400, "账号不能为空", postJson("/w04/employees", """
                {"name": "张小厨", "password": "123456"}
                """));
        assertFail(400, "账号长度必须在4到20之间", postJson("/w04/employees", """
                {"username": "abc", "name": "张小厨", "password": "123456"}
                """));
        assertFail(400, "姓名不能为空", postJson("/w04/employees", """
                {"username": "zhangxc", "password": "123456"}
                """));
        assertFail(400, "密码不能为空", postJson("/w04/employees", """
                {"username": "zhangxc", "name": "张小厨"}
                """));
        assertFail(400, "手机号格式不正确", postJson("/w04/employees", """
                {"username": "zhangxc", "name": "张小厨", "password": "123456", "phone": "12345678901"}
                """));
        assertFail(400, "手机号格式不正确", postJson("/w04/employees", """
                {"username": "zhangxc", "name": "张小厨", "password": "123456", "phone": "1390000000"}
                """));
        assertFail(400, "状态只能是0或1", postJson("/w04/employees", """
                {"username": "zhangxc", "name": "张小厨", "password": "123456", "status": 2}
                """));

        JsonNode two = postJson("/w04/employees", """
                {"username": "zhangxc", "password": "123456", "phone": "123"}
                """);
        assertEquals(400, two.get("code").asInt());
        String msg = two.get("msg").asText();
        assertTrue(msg.contains("姓名不能为空") && msg.contains("手机号格式不正确") && msg.contains(", "),
                "多个错误要用 \", \" 连接起来，实际：" + msg);

        assertEquals(2, assertOk(postJson("/w04/employees", """
                {"username": "zhangxc", "name": "张小厨", "password": "123456"}
                """)).asInt(), "手机号、状态不传是可以的");
    }

    @Test
    @DisplayName("4.2 + 4.3 修改时的校验（分组）")
    void editValidation() {
        assertFail(400, "id不能为空", putJson("/w04/employees", """
                {"username": "admin", "name": "管理员"}
                """));
        assertFail(400, "账号不能为空", putJson("/w04/employees", """
                {"id": 1, "name": "管理员"}
                """));
        assertOk(putJson("/w04/employees", """
                {"id": 1, "username": "admin", "name": "超级管理员"}
                """));
        assertEquals("超级管理员", assertOk(getJson("/w04/employees/1")).get("name").asText(),
                "修改时不传密码是允许的");
    }

    @Test
    @DisplayName("4.3 方法参数校验 @Min")
    void pathVariableValidation() {
        assertEquals("admin", assertOk(getJson("/w04/employees/1")).get("username").asText());
        assertFail(400, "id必须大于0", getJson("/w04/employees/0"));
        assertFail(400, "id必须大于0", getJson("/w04/employees/-1"));
    }

    @Test
    @DisplayName("4.4 自定义校验注解 StatusValidator")
    void statusValidator() {
        StatusValidator v = new StatusValidator();
        assertTrue(v.isValid(null, null));
        assertTrue(v.isValid(0, null));
        assertTrue(v.isValid(1, null));
        assertFalse(v.isValid(2, null));
        assertFalse(v.isValid(-1, null));
    }
}
