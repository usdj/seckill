package com.usdj.seckill.controller;

import com.usdj.seckill.error.BusinessException;
import com.usdj.seckill.error.EmBusinessError;
import com.usdj.seckill.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gerrydeng
 * @date 2019-07-27 21:24
 * @Description:
 */
public class BaseController {

	public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object handlerException(HttpServletRequest request, Exception ex) {
		Map<String, Object> responseData = new HashMap<>(2);
		if (ex instanceof BusinessException) {
			BusinessException businessException = (BusinessException) ex;
			responseData.put("errCode", businessException.getErrCode());
			responseData.put("errMsg", businessException.getErrMsg());
		} else {
			responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
			responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
		}
		return CommonReturnType.create(responseData, "fail");
	}
}
