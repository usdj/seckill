package com.usdj.seckill.service;

import com.usdj.seckill.error.BusinessException;
import com.usdj.seckill.service.model.OrderModel;

/**
 * @author gerrydeng
 * @date 2019-07-28 15:16
 * @Description:
 */
public interface OrderService {

	OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
}
