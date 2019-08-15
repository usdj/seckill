package com.usdj.seckill.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author gerrydeng
 * @date 2019-07-28 09:31
 * @Description:
 */
@Getter
@Setter
public class UserModel {

	private Integer id;

	@NotBlank(message = "用户名不能为空")
	private String name;

	@NotNull(message = "性别不能为空")
	private Byte gender;

	@NotNull(message = "年龄不能为空")
	@Min(value = 0, message = "年龄必须大于0")
	@Max(value = 150, message = "年龄必须小于150")
	private Integer age;

	@NotBlank(message = "手机号不能为空")
	private String telephone;

	private String registerMode;

	private String thirdPartyId;

	@NotBlank(message = "密码不能为空")
	private String encryptPassword;

}
