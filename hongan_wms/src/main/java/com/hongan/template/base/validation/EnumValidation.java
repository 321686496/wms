package com.hongan.template.base.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: EnumValidation
 */

@Documented
// 指定真正实现校验规则的类
@Constraint(validatedBy = EnumValidation.EnumValid.class)
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER})
public @interface EnumValidation {
    String value();
    String message() default "ValueMustMatch\"{regexp}\"";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /**
     * 验证enum类型，值为传进来的字符串，
     * 例如:
     *
     * @EnumValidation("aa|bb") 即表示 允许 aa和bb;
     */
    class EnumValid implements ConstraintValidator<EnumValidation, String> {

        private Pattern pattern;

        @Override
        public void initialize(EnumValidation annotation) {
            try {
                pattern = Pattern.compile("^" + annotation.value() + "$");
            } catch (PatternSyntaxException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }
            Matcher m = pattern.matcher(value);
            return m.matches();
        }
    }
}
