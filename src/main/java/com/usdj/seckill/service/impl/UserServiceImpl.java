package com.usdj.seckill.service.impl;

import com.usdj.seckill.dao.UserDOMapper;
import com.usdj.seckill.dao.UserPasswordDOMapper;
import com.usdj.seckill.dataobject.UserDO;
import com.usdj.seckill.dataobject.UserPasswordDO;
import com.usdj.seckill.error.BusinessException;
import com.usdj.seckill.error.EmBusinessError;
import com.usdj.seckill.service.UserService;
import com.usdj.seckill.service.model.UserModel;
import com.usdj.seckill.validator.ValidationResult;
import com.usdj.seckill.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gerrydeng
 * @date 2019-07-28 10:00
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDOMapper userDOMapper;

	@Autowired
	private UserPasswordDOMapper userPasswordDOMapper;

	@Autowired
	private ValidatorImpl validator;

	@Override
	public UserModel getUserById(Integer id) {
		UserDO userDO = userDOMapper.selectByPrimaryKey(id);
		if (userDO == null) {
			return null;
		}
		UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);
		return converFromDataObject(userDO, userPasswordDO);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void register(UserModel userModel) throws BusinessException {
		if (userModel == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		ValidationResult result = validator.validate(userModel);
		if (result.isHasErrors()) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}

		UserDO userDO = convertFromModel(userModel);
		try {
			userDOMapper.insertSelective(userDO);
		} catch (Exception e) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		userModel.setId(userDO.getId());
		UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
		userPasswordDOMapper.insertSelective(userPasswordDO);
	}

	@Override
	public UserModel validateLogin(String telephone, String encryptPassword) throws BusinessException {
		UserDO userDO = userDOMapper.selectByTelephone(telephone);
		if (userDO == null) {
			throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
		}
		UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
		UserModel userModel = converFromDataObject(userDO, userPasswordDO);
		if (!StringUtils.equals(encryptPassword, userModel.getEncryptPassword())) {
			throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
		}
		return userModel;
	}

	private UserPasswordDO convertPasswordFromModel(UserModel userModel) {
		if (userModel == null) {
			return null;
		}
		UserPasswordDO userPasswordDO = new UserPasswordDO();
		userPasswordDO.setEncryptPassword(userModel.getEncryptPassword());
		userPasswordDO.setUserId(userModel.getId());
		return userPasswordDO;
	}

	private UserDO convertFromModel(UserModel userModel) {
		if (userModel == null) {
			return null;
		}
		UserDO userDO = new UserDO();
		BeanUtils.copyProperties(userModel, userDO);
		return userDO;
	}

	private UserModel converFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
		if (userDO == null) {
			return null;
		}
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(userDO, userModel);
		if (userPasswordDO != null) {
			userModel.setEncryptPassword(userPasswordDO.getEncryptPassword());
		}
		return userModel;
	}
}
