package com.hongan.template.base.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: PasswordValidation 
*/

@Documented
// 指定真正实现校验规则的类
@Constraint(validatedBy = PasswordValidation.PasswordValid.class)
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER})
public @interface PasswordValidation {

    public String message() default "PasswordError";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        PasswordValidation[] value();
    }

    class PasswordValid implements ConstraintValidator<PasswordValidation, String> {
        private static final String PasswordPattern = "^[0-9A-Za-z]{6,20}";

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value.matches(PasswordPattern);
        }
    }
}
