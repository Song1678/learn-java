package com.learn.ch04_reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch04 反射")
class ReflectionTest {

    private SysUser newUser() {
        SysUser u = new SysUser(1L, "zhangsan", "张三", 18, "123456");
        u.setCreateBy("admin");
        u.setCreateTime(LocalDateTime.of(2024, 1, 1, 12, 0));
        return u;
    }

    @Test
    @DisplayName("1.1 getFieldNames（跳过 static）")
    void getFieldNames() {
        assertEquals(Set.of("userId", "userName", "nickName", "age", "password"),
                new HashSet<>(ReflectionUtils.getFieldNames(SysUser.class)));
    }

    @Test
    @DisplayName("1.2 getAllFields（包括父类）")
    void getAllFields() {
        Set<String> names = new HashSet<>();
        for (Field f : ReflectionUtils.getAllFields(SysUser.class)) {
            names.add(f.getName());
        }
        assertEquals(Set.of("userId", "userName", "nickName", "age", "password", "createBy", "createTime"), names);
    }

    @Test
    @DisplayName("1.3 findField")
    void findField() {
        assertEquals("createBy", ReflectionUtils.findField(SysUser.class, "createBy").getName());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> ReflectionUtils.findField(SysUser.class, "nope"));
        assertEquals("字段不存在: nope", e.getMessage());
    }

    @Test
    @DisplayName("1.4 / 1.5 读写字段（包括 private 和父类字段）")
    void getAndSet() {
        SysUser u = newUser();
        assertEquals("123456", ReflectionUtils.getFieldValue(u, "password"));
        assertEquals(18, ReflectionUtils.getFieldValue(u, "age"));
        assertEquals("admin", ReflectionUtils.getFieldValue(u, "createBy"));

        ReflectionUtils.setFieldValue(u, "nickName", "老张");
        ReflectionUtils.setFieldValue(u, "createBy", "root");
        assertEquals("老张", u.getNickName());
        assertEquals("root", u.getCreateBy());
        assertThrows(IllegalArgumentException.class, () -> ReflectionUtils.setFieldValue(u, "nope", 1));
    }

    @Test
    @DisplayName("1.6 invoke")
    void invoke() {
        SysUser u = newUser();
        assertEquals(3, ReflectionUtils.invoke(u, "add", 1, 2));
        assertEquals("张三 向 李四 打招呼", ReflectionUtils.invoke(u, "greet", "李四"));
        assertEquals("密码是 123456", ReflectionUtils.invoke(u, "secret"));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ReflectionUtils.invoke(u, "fly"));
        assertEquals("方法不存在: fly", e.getMessage());
    }

    @Test
    @DisplayName("1.7 newInstance")
    void newInstance() {
        SysUser u = ReflectionUtils.newInstance(SysUser.class);
        assertNotNull(u);
        assertNull(u.getUserName());
    }

    @Test
    @DisplayName("2.1 / 2.2 copy：同名同类型才复制")
    void copy() {
        SysUserVo vo = BeanCopier.copy(newUser(), SysUserVo.class);
        assertEquals(1L, vo.getUserId());
        assertEquals("zhangsan", vo.getUserName());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0), vo.getCreateTime(), "父类字段也要复制");
        assertNull(vo.getAge(), "int 和 Integer 类型不同，应该跳过");
        assertNull(vo.getRemark());
    }

    @Test
    @DisplayName("2.3 toMap")
    void toMap() {
        Map<String, Object> map = BeanCopier.toMap(newUser());
        assertEquals(7, map.size());
        assertEquals("zhangsan", map.get("userName"));
        assertEquals(18, map.get("age"));
        assertEquals("admin", map.get("createBy"));
        assertFalse(map.containsKey("TABLE_NAME"));
    }
}
