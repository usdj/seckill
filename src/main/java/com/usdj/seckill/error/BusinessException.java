package com.usdj.seckill.error;

/**
 * @author gerrydeng
 * @date 2019-07-27 21:28
 * @Description:
 */
public class BusinessException extends Exception implements CommonError {

	private CommonError commonError;

	public BusinessException(CommonError commonError) {
		super();
		this.commonError = commonError;
	}

	public BusinessException(CommonError commonError, String errMsg) {
		super();
		this.commonError = commonError;
		this.commonError.setErrMsg(errMsg);
	}

	@Override
	public int getErrCode() {
		return this.commonError.getErrCode();
	}

	@Override
	public String getErrMsg() {
		return this.commonError.getErrMsg();
	}

	@Override
	public CommonError setErrMsg(String errMsg) {
		this.commonError.setErrMsg(errMsg);
		return this;
	}
}
