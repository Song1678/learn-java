package com.learn.web.w04_exception.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * w04 练习 4.4：@Status 的检查逻辑
 */
public class StatusValidator implements ConstraintValidator<Status, Integer> {

    /**
     * 返回 true 表示校验通过。规则：null 通过（“必填”交给 @NotNull 管）；0 和 1 通过；其他都不通过。
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // @answer
        return value == null || value == 0 || value == 1;
        // @end
    }
}
