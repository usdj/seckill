package com.usdj.seckill.service;

import com.usdj.seckill.service.model.PromoModel;

/**
 * @author gerrydeng
 * @date 2019-07-28 15:08
 * @Description:
 */
public interface PromoService {

	PromoModel getPromoByItemId(Integer itemId);
}
