package com.bloomberg.fx_analyzer.annotation;

import com.bloomberg.fx_analyzer.validator.CurrencyCodeValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyCodeValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyCode {
	String message() default "Invalid currency code";

}
