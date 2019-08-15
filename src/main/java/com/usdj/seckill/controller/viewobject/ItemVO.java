package com.usdj.seckill.controller.viewobject;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author gerrydeng
 * @date 2019-07-28 15:36
 * @Description:
 */
@Getter
@Setter
public class ItemVO {

	private Integer id;

	private String title;

	private BigDecimal price;

	private Integer stock;

	private String description;

	private Integer sales;

	private String imgUrl;

	private Integer promoStatus;

	private BigDecimal promoPrice;

	private Integer promoId;

	private String promoStartDate;
}
