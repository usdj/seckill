package com.usdj.seckill.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gerrydeng
 * @date 2019-07-28 10:08
 * @Description:
 */
public class ValidationResult {

	private boolean hasErrors = false;

	private Map<String, String> errorMsgMap = new HashMap<>();

	public boolean isHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	public Map<String, String> getErrorMsgMap() {
		return errorMsgMap;
	}

	public void setErrorMsgMap(Map<String, String> errorMsgMap) {
		this.errorMsgMap = errorMsgMap;
	}

	public String getErrMsg(){
		return StringUtils.join(errorMsgMap.values().toArray(), ",");
	}
}
