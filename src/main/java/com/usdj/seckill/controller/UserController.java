package com.usdj.seckill.controller;

import com.usdj.seckill.controller.viewobject.UserVO;
import com.usdj.seckill.error.BusinessException;
import com.usdj.seckill.error.EmBusinessError;
import com.usdj.seckill.response.CommonReturnType;
import com.usdj.seckill.service.UserService;
import com.usdj.seckill.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author gerrydeng
 * @date 2019-07-28 13:20
 * @Description:
 */
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController{

	@Autowired
	private UserService userService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
	@ResponseBody
	public CommonReturnType login(@RequestParam(name = "telephone") String telephone,
	                              @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
		if (StringUtils.isEmpty(telephone) || StringUtils.isEmpty(password)){
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		UserModel userModel = userService.validateLogin(telephone, this.enCodeByMD5(password));
		this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
		this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
		return CommonReturnType.create(null);
	}

	@RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
	@ResponseBody
	public CommonReturnType register(@RequestParam(name = "telephone") String telephone,
	                                 @RequestParam(name = "optCode") String optCode,
	                                 @RequestParam(name = "name") String name,
	                                 @RequestParam(name = "gender") Byte gender,
	                                 @RequestParam(name = "age") Integer age,
	                                 @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
		String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telephone);
		if (!StringUtils.equals(optCode, inSessionOtpCode)){
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,EmBusinessError.PARAMETER_VALIDATION_ERROR.getErrMsg());
		}
		UserModel userModel = new UserModel();
		userModel.setName(name);
		userModel.setGender(gender);
		userModel.setAge(age);
		userModel.setTelephone(telephone);
		userModel.setRegisterMode("byPhone");
		userModel.setEncryptPassword(this.enCodeByMD5(password));
		userService.register(userModel);
		return CommonReturnType.create(null);
	}

	@RequestMapping(value = "/getotp",method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
	@ResponseBody
	public CommonReturnType getOtp(@RequestParam(name = "telephone") String telephone){
		int randomInt = new Random().nextInt(899999) + 100000;
		String otpCode = String.valueOf(randomInt);
		httpServletRequest.getSession().setAttribute(telephone, otpCode);
		System.out.println("telephone + " + telephone + " & otpCode = " + otpCode);
		return CommonReturnType.create(null);
	}

	@RequestMapping("/get")
	@ResponseBody
	public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
		UserModel userModel = userService.getUserById(id);
		if (userModel == null){
			throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
		}

		UserVO userVO = convertFromModel(userModel);

		return CommonReturnType.create(userVO);
	}

	private UserVO convertFromModel(UserModel userModel){
		if (userModel == null){
			return null;
		}
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(userModel, userVO);
		return userVO;
	}

	private String enCodeByMD5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
	}
}
