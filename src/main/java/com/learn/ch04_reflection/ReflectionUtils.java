package com.learn.ch04_reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 练习 1：反射基础（对应「反射 - Class类 / 访问字段 / 调用方法 / 调用构造方法 / 获取继承关系」）
 *
 * <p>JS 里 obj[key] 就能动态读写属性；Java 是静态类型语言，想“按名字”操作字段/方法就要用反射。
 * Spring、MyBatis 这类框架的底层大量使用反射。
 *
 * <p>为了简化，所有反射抛出的受检异常（NoSuchFieldException、IllegalAccessException 等）
 * 统一包装成 RuntimeException 抛出（找不到字段/方法时用 IllegalArgumentException）。
 */
public class ReflectionUtils {

    private ReflectionUtils() {
    }

    /**
     * 1.1 获取类自己声明的所有【非 static】字段名（不包括父类）。
     * 例如 SysUser -> [userId, userName, nickName, age, password]
     * 提示：clazz.getDeclaredFields()、Modifier.isStatic(field.getModifiers())
     */
    public static List<String> getFieldNames(Class<?> clazz) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 1.2 获取类及其所有父类（直到 Object 之前）声明的【非 static】字段。
     * 例如 SysUser -> userId, userName, nickName, age, password, createBy, createTime 对应的 Field
     * 提示：clazz.getSuperclass() 循环往上找。
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 1.3 根据字段名找字段（包括父类里的），找不到抛 IllegalArgumentException("字段不存在: xxx")。
     */
    public static Field findField(Class<?> clazz, String fieldName) {
        // TODO 提示：复用 getAllFields
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 1.4 读取对象某个字段的值（包括 private 字段和父类字段）。
     * 提示：field.setAccessible(true) 之后才能访问 private 字段。
     */
    public static Object getFieldValue(Object target, String fieldName) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 1.5 设置对象某个字段的值（包括 private 字段和父类字段）。
     */
    public static void setFieldValue(Object target, String fieldName, Object value) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 1.6 按方法名和参数个数调用方法（包括 private 方法，只找类自己声明的方法），返回方法返回值。
     * 找不到抛 IllegalArgumentException("方法不存在: xxx")。
     * <pre>
     * invoke(user, "add", 1, 2)       -> 3
     * invoke(user, "secret")          -> "密码是 123456"
     * </pre>
     * 提示：遍历 getDeclaredMethods()，比较 getName() 和 getParameterCount()，然后 method.invoke(target, args)。
     * invoke 会抛 InvocationTargetException，它的 getCause() 才是方法内部真正抛出的异常。
     */
    public static Object invoke(Object target, String methodName, Object... args) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 1.7 用无参构造方法创建对象。
     * 提示：clazz.getDeclaredConstructor().newInstance()
     */
    public static <T> T newInstance(Class<T> clazz) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
