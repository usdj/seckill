package com.usdj.seckill.service;

import com.usdj.seckill.error.BusinessException;
import com.usdj.seckill.service.model.ItemModel;

import java.util.List;

/**
 * @author gerrydeng
 * @date 2019-07-28 14:38
 * @Description:
 */
public interface ItemService {

	ItemModel createItem(ItemModel itemModel) throws BusinessException;

	List<ItemModel> listItem();

	ItemModel getItemById(Integer id);

	boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

	void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}
