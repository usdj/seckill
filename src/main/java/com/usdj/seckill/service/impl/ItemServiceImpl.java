package com.usdj.seckill.service.impl;

import com.usdj.seckill.dao.ItemDOMapper;
import com.usdj.seckill.dao.ItemStockDOMapper;
import com.usdj.seckill.dataobject.ItemDO;
import com.usdj.seckill.dataobject.ItemStockDO;
import com.usdj.seckill.error.BusinessException;
import com.usdj.seckill.error.EmBusinessError;
import com.usdj.seckill.service.ItemService;
import com.usdj.seckill.service.PromoService;
import com.usdj.seckill.service.model.ItemModel;
import com.usdj.seckill.service.model.PromoModel;
import com.usdj.seckill.validator.ValidationResult;
import com.usdj.seckill.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gerrydeng
 * @date 2019-07-28 14:40
 * @Description:
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ValidatorImpl validator;

	@Autowired
	private ItemDOMapper itemDOMapper;

	@Autowired
	private ItemStockDOMapper itemStockDOMapper;

	@Autowired
	private PromoService promoService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ItemModel createItem(ItemModel itemModel) throws BusinessException {
		ValidationResult validationResult = validator.validate(itemModel);
		if (validationResult.isHasErrors()) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		ItemDO itemDO = this.convertItemDOFromItemModel(itemModel);
		this.itemDOMapper.insertSelective(itemDO);
		itemModel.setId(itemDO.getId());
		ItemStockDO itemStockDO = this.convertItemStockDOFromModel(itemModel);
		this.itemStockDOMapper.insertSelective(itemStockDO);
		return this.getItemById(itemModel.getId());
	}

	@Override
	public List<ItemModel> listItem() {
		List<ItemDO> itemDOList = this.itemDOMapper.listItem();
		List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
			ItemStockDO itemStockDO = this.itemStockDOMapper.selectByItemId(itemDO.getId());
			return this.convertModelFromDataObject(itemDO, itemStockDO);
		}).collect(Collectors.toList());
		return itemModelList;
	}

	@Override
	public ItemModel getItemById(Integer id) {
		ItemDO itemDO = this.itemDOMapper.selectByPrimaryKey(id);
		if (itemDO == null){
			return null;
		}

		ItemStockDO itemStockDO = this.itemStockDOMapper.selectByItemId(itemDO.getId());
		ItemModel itemModel = this.convertModelFromDataObject(itemDO, itemStockDO);

		PromoModel promoModel = this.promoService.getPromoByItemId(itemModel.getId());
		if (promoModel != null && promoModel.getStatus().intValue() != 3) {
			itemModel.setPromoModel(promoModel);
		}

		return itemModel;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
		int affectedRow = this.itemStockDOMapper.decreaseStock(itemId, amount);
		return affectedRow > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
		this.itemDOMapper.increaseSales(itemId, amount);

	}

	private ItemModel convertModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO) {
		ItemModel itemModel = new ItemModel();
		BeanUtils.copyProperties(itemDO, itemModel);
		itemModel.setStock(itemStockDO.getStock());
		return itemModel;
	}

	private ItemStockDO convertItemStockDOFromModel(ItemModel itemModel) {
		if (itemModel == null) {
			return null;
		}
		ItemStockDO itemStockDO = new ItemStockDO();
		itemStockDO.setItemId(itemModel.getId());
		itemStockDO.setStock(itemModel.getStock());
		return itemStockDO;
	}

	private ItemDO convertItemDOFromItemModel(ItemModel itemModel) {
		if (itemModel == null) {
			return null;
		}
		ItemDO itemDO = new ItemDO();
		BeanUtils.copyProperties(itemModel, itemDO);
		return itemDO;
	}

}
