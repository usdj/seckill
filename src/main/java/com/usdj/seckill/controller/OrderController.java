package com.usdj.seckill.controller;

import com.usdj.seckill.error.BusinessException;
import com.usdj.seckill.error.EmBusinessError;
import com.usdj.seckill.response.CommonReturnType;
import com.usdj.seckill.service.OrderService;
import com.usdj.seckill.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gerrydeng
 * @date 2019-07-28 15:43
 * @Description:
 */
@Controller("order")
@RequestMapping("/order")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class OrderController extends BaseController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	/**
	 * 封装下单请求
	 *
	 * @param itemId
	 * @param amount
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
	@ResponseBody
	public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
	                                    @RequestParam(name = "promoId", required = false) Integer promoId,
	                                    @RequestParam(name = "amount") Integer amount) throws BusinessException {
		// 获取用户的登录信息
		Boolean isLogin = (Boolean) this.httpServletRequest.getSession().getAttribute("IS_LOGIN");
		if (isLogin == null || !isLogin.booleanValue()) {
			throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户还未登录，不能下单");
		}
		UserModel userModel = (UserModel) this.httpServletRequest.getSession().getAttribute("LOGIN_USER");

		this.orderService.createOrder(userModel.getId(), itemId, promoId,amount);

		return CommonReturnType.create(null);
	}

}
