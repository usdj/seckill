package com.usdj.seckill.error;

/**
 * @author gerrydeng
 * @date 2019-07-27 21:27
 * @Description:
 */
public interface CommonError {

	int getErrCode();

	String getErrMsg();

	CommonError setErrMsg(String errMsg);
}
