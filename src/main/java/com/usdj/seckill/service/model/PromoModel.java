package com.usdj.seckill.service.model;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @author gerrydeng
 * @date 2019-07-28 09:35
 * @Description:
 */
@Getter
@Setter
public class PromoModel {

	private Integer id;

	private Integer status;

	private String promoName;

	private DateTime startDate;

	private DateTime endDate;

	private Integer itemId;

	private BigDecimal promoItemPrice;

}
