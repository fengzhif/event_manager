package com.fengzhi.event_manager.anno;

import com.fengzhi.event_manager.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented //元注解，是否包含在Javadoc中
@Constraint(
        validatedBy = {StateValidation.class}
)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface State {
    String message() default "{事件状态只能为 完成|未完成 }";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
