package com.usdj.seckill.service.impl;

import com.usdj.seckill.dao.OrderDOMapper;
import com.usdj.seckill.dao.SequenceDOMapper;
import com.usdj.seckill.dataobject.OrderDO;
import com.usdj.seckill.dataobject.SequenceDO;
import com.usdj.seckill.error.BusinessException;
import com.usdj.seckill.error.EmBusinessError;
import com.usdj.seckill.service.ItemService;
import com.usdj.seckill.service.OrderService;
import com.usdj.seckill.service.UserService;
import com.usdj.seckill.service.model.ItemModel;
import com.usdj.seckill.service.model.OrderModel;
import com.usdj.seckill.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author gerrydeng
 * @date 2019-07-28 15:17
 * @Description:
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private ItemService itemService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderDOMapper orderDOMapper;

	@Autowired
	private SequenceDOMapper sequenceDOMapper;

	@Override
	public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
		ItemModel itemModel = this.itemService.getItemById(itemId);
		if (itemModel == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
		}

		UserModel userModel = this.userService.getUserById(userId);
		if (userModel == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
		}

		if (amount <= 0 || amount > 99) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "购买数量不正确");
		}

		// 校验活动信息
		if (promoId != null) {
			// 1. 校验对应活动是否存在这个适用商品
			if (promoId.intValue() != itemModel.getPromoModel().getId()) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
			} else if (itemModel.getPromoModel().getStatus().intValue() != 2) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动还未开始");
			}
		}

		// 2. 落单减库存
		boolean result = this.itemService.decreaseStock(itemId, amount);
		if (!result) {
			throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
		}

		// 3. 订单入库
		OrderModel orderModel = new OrderModel();
		orderModel.setUserId(userId);
		orderModel.setItemId(itemId);
		orderModel.setAmount(amount);
		if (promoId != null) {
			orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
		} else {
			orderModel.setItemPrice(itemModel.getPrice());
		}
		orderModel.setPromoId(promoId);
		orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

		// 生成交易流水号，订单号
		orderModel.setId(this.generateOrderNo());
		OrderDO orderDO = this.convertFromOrderModel(orderModel);
		this.orderDOMapper.insertSelective(orderDO);

		// 加上商品的销量
		this.itemService.increaseSales(itemId, amount);

		// 4. 返回前端
		return orderModel;
	}

	private OrderDO convertFromOrderModel(OrderModel orderModel) {
		if (orderModel == null) {
			return null;
		}
		OrderDO orderDO = new OrderDO();
		BeanUtils.copyProperties(orderModel, orderDO);

		return orderDO;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	String generateOrderNo() {
		// 订单号有16位
		StringBuilder stringBuilder = new StringBuilder();

		// 前8位为时间信息，年月日
		LocalDateTime now = LocalDateTime.now();
		String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
		stringBuilder.append(nowDate);

		// 中间6位为自增序列
		// 获取当前sequence
		int sequence = 0;
		SequenceDO sequenceDO = this.sequenceDOMapper.getSequenceByName("order_info");
		sequence = sequenceDO.getCurrentValue();
		sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
		this.sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

		String sequenceStr = String.valueOf(sequence);
		for (int i = 0; i < 6 - sequenceStr.length(); i++) {
			stringBuilder.append(0);
		}
		stringBuilder.append(sequenceStr);

		// 最后2位为分库分表位，暂时写死
		stringBuilder.append("00");

		return stringBuilder.toString();
	}
}
