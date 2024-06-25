package com.bloomberg.fx_analyzer.validator;

import com.bloomberg.fx_analyzer.annotation.CurrencyCode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

public class CurrencyCodeValidator implements ConstraintValidator<CurrencyCode, String> {

	@Override
	public void initialize(CurrencyCode constraintAnnotation) {
	}

	@Override
	public boolean isValid(String currencyCode, ConstraintValidatorContext context) {
		if (currencyCode == null || currencyCode.isEmpty()) {
			return true;
		}
		try {
			Currency.getInstance(currencyCode);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
