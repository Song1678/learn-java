package com.learn.ch05_annotation;

import com.learn.ch05_annotation.excel.CsvExporter;
import com.learn.ch05_annotation.excel.DishExportVo;
import com.learn.ch05_annotation.validation.DishForm;
import com.learn.ch05_annotation.validation.Length;
import com.learn.ch05_annotation.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch05 注解")
class AnnotationTest {

    @Test
    @DisplayName("1.1 修好 @Length 的 Retention")
    void lengthRetention() {
        Retention r = Length.class.getAnnotation(Retention.class);
        assertEquals(RetentionPolicy.RUNTIME, r.value());
    }

    @Test
    @DisplayName("1.2 校验通过")
    void valid() {
        DishForm form = new DishForm("宫保鸡丁", new BigDecimal("28.00"), 1L, 1, null);
        assertEquals(List.of(), Validator.validate(form));
        assertDoesNotThrow(() -> Validator.validateOrThrow(form));
    }

    @Test
    @DisplayName("1.2 校验不通过")
    void invalid() {
        DishForm form = new DishForm(" ", null, null, 2, null);
        assertEquals(Set.of("name不能为空", "price不能为空", "categoryId不能为空", "status只能是0或1"),
                new HashSet<>(Validator.validate(form)));

        DishForm form2 = new DishForm("超长的名字".repeat(5), new BigDecimal("10000.5"), 1L, null, null);
        assertEquals(Set.of("name长度不能超过20", "price必须在0到9999之间"),
                new HashSet<>(Validator.validate(form2)));

        DishForm form3 = new DishForm("米饭", new BigDecimal("-0.01"), 1L, 0, null);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Validator.validateOrThrow(form3));
        assertEquals("price必须在0到9999之间", e.getMessage());
    }

    @Test
    @DisplayName("2 根据注解导出 CSV")
    void export() {
        List<DishExportVo> rows = List.of(
                new DishExportVo(1L, "宫保鸡丁", "川菜", new BigDecimal("28.00")),
                new DishExportVo(2L, "米饭", null, new BigDecimal("2")));
        assertEquals("菜品名称,分类,价格\n宫保鸡丁,川菜,28.00\n米饭,,2", CsvExporter.export(rows, DishExportVo.class));
        assertEquals("菜品名称,分类,价格", CsvExporter.export(List.of(), DishExportVo.class));
    }
}
