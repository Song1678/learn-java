package com.learn.ch04_reflection;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 练习 2：手写一个简易版 BeanUtils.copyProperties
 *
 * <p>可以使用 {@link ReflectionUtils} 里你已经写好的方法。
 */
public class BeanCopier {

    private BeanCopier() {
    }

    /**
     * 2.1 把 source 中的字段值复制到 target 中“同名并且类型相同”的字段（包括父类字段）。
     * 名字相同但类型不同的字段（比如 int 和 Integer）跳过。
     * 提示：sourceField.getType() == targetField.getType()
     */
    public static void copyProperties(Object source, Object target) {
        // @answer
        Map<String, Field> targetFields = new LinkedHashMap<>();
        for (Field f : ReflectionUtils.getAllFields(target.getClass())) {
            targetFields.put(f.getName(), f);
        }
        for (Field sf : ReflectionUtils.getAllFields(source.getClass())) {
            Field tf = targetFields.get(sf.getName());
            if (tf == null || tf.getType() != sf.getType()) {
                continue;
            }
            try {
                sf.setAccessible(true);
                tf.setAccessible(true);
                tf.set(target, sf.get(source));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        // @end
    }

    /**
     * 2.2 创建一个 targetClass 的新对象，并把 source 的字段复制过去。
     * <pre>
     * SysUserVo vo = BeanCopier.copy(user, SysUserVo.class);
     * </pre>
     */
    public static <T> T copy(Object source, Class<T> targetClass) {
        // @answer
        T target = ReflectionUtils.newInstance(targetClass);
        copyProperties(source, target);
        return target;
        // @end
    }

    /**
     * 2.3 把对象转成 Map：key 是字段名，value 是字段值（包括父类字段，跳过 static 字段）。
     */
    public static Map<String, Object> toMap(Object source) {
        // @answer
        Map<String, Object> map = new LinkedHashMap<>();
        for (Field f : ReflectionUtils.getAllFields(source.getClass())) {
            try {
                f.setAccessible(true);
                map.put(f.getName(), f.get(source));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return map;
        // @end
    }
}
