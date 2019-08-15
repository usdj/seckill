package com.usdj.seckill.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author gerrydeng
 * @date 2019-07-28 10:11
 * @Description:
 */
@Component
public class ValidatorImpl implements InitializingBean {

	private Validator validator;

	public ValidationResult validate(Object bean){
		ValidationResult result = new ValidationResult();
		Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
		if (constraintViolationSet.size() > 0){
			result.setHasErrors(true);
			constraintViolationSet.forEach(constrainViolation->{
				String errMsg = constrainViolation.getMessage();
				String propertyName = constrainViolation.getPropertyPath().toString();
				result.getErrorMsgMap().put(propertyName, errMsg);
			});
		}
		return result;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
}
