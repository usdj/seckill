package com.usdj.seckill.service.impl;

import com.usdj.seckill.dao.PromoDOMapper;
import com.usdj.seckill.dataobject.PromoDO;
import com.usdj.seckill.service.PromoService;
import com.usdj.seckill.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gerrydeng
 * @date 2019-07-28 15:08
 * @Description:
 */
@Service
public class PromoServiceImpl implements PromoService {

	@Autowired
	private PromoDOMapper promoDOMapper;

	@Override
	public PromoModel getPromoByItemId(Integer itemId) {
		PromoDO promoDO = this.promoDOMapper.selectByItemId(itemId);
		PromoModel promoModel = this.convertFromDataObject(promoDO);
		if (promoModel == null){
			return null;
		}

		DateTime now = new DateTime();
		if (promoModel.getStartDate().isAfterNow()){
			promoModel.setStatus(1);
		} else if (promoModel.getEndDate().isBeforeNow()){
			promoModel.setStatus(3);
		} else {
			promoModel.setStatus(2);
		}
		return promoModel;
	}

	private PromoModel convertFromDataObject(PromoDO promoDO){
		if (promoDO == null){
			return null;
		}
		PromoModel promoModel = new PromoModel();
		BeanUtils.copyProperties(promoDO, promoModel);
		promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
		promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
		return promoModel;
	}
}
