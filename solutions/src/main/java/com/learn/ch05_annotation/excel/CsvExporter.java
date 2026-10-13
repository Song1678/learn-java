package com.learn.ch05_annotation.excel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 练习 2：根据注解导出 CSV
 */
public class CsvExporter {

    private CsvExporter() {
    }

    /**
     * 把 rows 导出成 CSV 文本：
     * <ul>
     *   <li>只导出带 @ExcelColumn 的字段，按 order 从小到大排列</li>
     *   <li>第一行是表头（各列的 name），之后每个对象一行</li>
     *   <li>列之间用英文逗号分隔，行之间用 '\n' 分隔，最后一行后面没有换行</li>
     *   <li>字段值为 null 时输出空字符串</li>
     * </ul>
     * <pre>
     * 菜品名称,分类,价格
     * 宫保鸡丁,川菜,28.00
     * 米饭,主食,2
     * </pre>
     * 提示：先把带注解的 Field 收集到 List 里再排序：
     * list.sort(Comparator.comparingInt(f -&gt; f.getAnnotation(ExcelColumn.class).order()))
     * 或者写一个匿名内部类 new Comparator&lt;Field&gt;() { ... }
     */
    public static <T> String export(List<T> rows, Class<T> clazz) {
        // @answer
        List<Field> columns = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(ExcelColumn.class)) {
                f.setAccessible(true);
                columns.add(f);
            }
        }
        columns.sort(Comparator.comparingInt(f -> f.getAnnotation(ExcelColumn.class).order()));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(columns.get(i).getAnnotation(ExcelColumn.class).name());
        }
        for (T row : rows) {
            sb.append('\n');
            for (int i = 0; i < columns.size(); i++) {
                if (i > 0) {
                    sb.append(',');
                }
                try {
                    Object v = columns.get(i).get(row);
                    sb.append(v == null ? "" : v.toString());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return sb.toString();
        // @end
    }
}
