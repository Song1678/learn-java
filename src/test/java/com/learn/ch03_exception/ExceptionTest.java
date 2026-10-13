package com.learn.ch03_exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch03 异常处理")
class ExceptionTest {

    private static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }

    @Nested
    @DisplayName("练习1：ServiceException")
    class ServiceExceptionTest {
        @Test
        void constructors() {
            ServiceException e1 = new ServiceException("出错了");
            assertEquals("出错了", e1.getMessage());
            assertEquals(500, e1.getCode());
            assertInstanceOf(RuntimeException.class, e1);

            ServiceException e2 = new ServiceException("未登录", 401);
            assertEquals(401, e2.getCode());

            Exception cause = new IllegalStateException("底层错误");
            ServiceException e3 = new ServiceException("包装", 400, cause);
            assertSame(cause, e3.getCause());
            assertEquals(400, e3.getCode());
        }
    }

    @Nested
    @DisplayName("练习2：AgeParser")
    class AgeParserTest {
        @Test
        void valid() {
            assertEquals(18, AgeParser.parseAge("18"));
            assertEquals(0, AgeParser.parseAge(" 0 "));
            assertEquals(150, AgeParser.parseAge("150"));
        }

        @Test
        void invalid() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> AgeParser.parseAge(" "));
            assertEquals("年龄不能为空", e.getMessage());
            assertThrows(IllegalArgumentException.class, () -> AgeParser.parseAge(null));

            e = assertThrows(IllegalArgumentException.class, () -> AgeParser.parseAge(" abc "));
            assertEquals("年龄格式不正确: abc", e.getMessage());
            assertInstanceOf(NumberFormatException.class, e.getCause(), "要把 NumberFormatException 作为 cause");

            e = assertThrows(IllegalArgumentException.class, () -> AgeParser.parseAge("151"));
            assertEquals("年龄超出范围: 151", e.getMessage());
            assertThrows(IllegalArgumentException.class, () -> AgeParser.parseAge("-1"));
        }

        @Test
        void orDefault() {
            assertEquals(18, AgeParser.parseAgeOrDefault("18", 0));
            assertEquals(-1, AgeParser.parseAgeOrDefault("abc", -1));
            assertEquals(-1, AgeParser.parseAgeOrDefault(null, -1));
        }
    }

    @Nested
    @DisplayName("练习3：受检异常与异常转换")
    class AccountTest {
        @Test
        void depositAndWithdraw() throws InsufficientBalanceException {
            Account a = new Account("张三", bd("100"));
            a.deposit(bd("50"));
            a.withdraw(bd("30"));
            assertEquals(0, bd("120").compareTo(a.getBalance()));
            assertThrows(IllegalArgumentException.class, () -> a.deposit(bd("0")));
            assertThrows(IllegalArgumentException.class, () -> a.withdraw(bd("-1")));
        }

        @Test
        void insufficient() {
            Account a = new Account("张三", bd("100"));
            InsufficientBalanceException e = assertThrows(InsufficientBalanceException.class, () -> a.withdraw(bd("200")));
            assertEquals("余额不足，需要 200，可用 100", e.getMessage());
            assertEquals(bd("200"), e.getRequired());
            assertEquals(bd("100"), e.getAvailable());
            assertEquals(0, bd("100").compareTo(a.getBalance()), "失败时余额不能变");
        }

        @Test
        void transfer() {
            Account from = new Account("张三", bd("100"));
            Account to = new Account("李四", bd("0"));
            TransferService service = new TransferService();

            service.transfer(from, to, bd("60"));
            assertEquals(0, bd("40").compareTo(from.getBalance()));
            assertEquals(0, bd("60").compareTo(to.getBalance()));

            ServiceException e = assertThrows(ServiceException.class, () -> service.transfer(from, to, bd("50")));
            assertEquals(400, e.getCode());
            assertEquals("余额不足，需要 50，可用 40", e.getMessage());
            assertInstanceOf(InsufficientBalanceException.class, e.getCause());
            assertEquals(0, bd("40").compareTo(from.getBalance()));
            assertEquals(0, bd("60").compareTo(to.getBalance()));
        }
    }

    @Nested
    @DisplayName("练习4：try-with-resources 执行顺序")
    class ResourceTest {
        @Test
        void success() {
            List<String> log = new ArrayList<>();
            ResourceDemo.run(log, false);
            assertEquals(List.of("open A", "open B", "work B", "close B", "close A", "finally"), log);
        }

        @Test
        void failure() {
            List<String> log = new ArrayList<>();
            ResourceDemo.run(log, true);
            assertEquals(List.of("open A", "open B", "work B", "close B", "close A", "catch: B failed", "finally"), log);
        }
    }

    @Nested
    @DisplayName("练习5：注册校验")
    class RegisterTest {
        private void assertFail(UserRegisterService s, String u, String p, String msg, int code) {
            ServiceException e = assertThrows(ServiceException.class, () -> s.register(u, p));
            assertEquals(msg, e.getMessage());
            assertEquals(code, e.getCode());
        }

        @Test
        void register() {
            UserRegisterService s = new UserRegisterService();
            assertFail(s, null, "abc123", "用户名不能为空", 400);
            assertFail(s, "  ", "abc123", "用户名不能为空", 400);
            assertFail(s, "abc", "abc123", "用户名长度必须在4到16之间", 400);
            assertFail(s, "a".repeat(17), "abc123", "用户名长度必须在4到16之间", 400);
            assertFail(s, "zhangsan", "ab12", "密码长度不能小于6", 400);
            assertFail(s, "zhangsan", null, "密码长度不能小于6", 400);
            assertFail(s, "zhangsan", "abcdef", "密码必须同时包含字母和数字", 400);
            assertFail(s, "zhangsan", "123456", "密码必须同时包含字母和数字", 400);
            assertFail(s, "admin", "abc123", "用户名已存在", 409);

            assertEquals("注册成功", s.register("zhangsan", "abc123"));
            assertFail(s, "zhangsan", "abc123", "用户名已存在", 409);
        }
    }
}
