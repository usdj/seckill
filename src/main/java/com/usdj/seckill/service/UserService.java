package com.usdj.seckill.service;

import com.usdj.seckill.error.BusinessException;
import com.usdj.seckill.service.model.UserModel;

/**
 * @author gerrydeng
 * @date 2019-07-28 09:29
 * @Description:
 */
public interface UserService {

	UserModel getUserById(Integer id);

	void register(UserModel userModel) throws BusinessException;

	UserModel validateLogin(String telephone, String encryptPassword) throws BusinessException;
}
