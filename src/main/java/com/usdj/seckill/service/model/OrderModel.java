package com.usdj.seckill.service.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author gerrydeng
 * @date 2019-07-28 09:40
 * @Description:
 */
@Getter
@Setter
public class OrderModel {

	private String id;

	private Integer userId;

	private Integer itemId;

	private Integer promoId;

	private BigDecimal itemPrice;

	private Integer amount;

	private BigDecimal orderPrice;
}
