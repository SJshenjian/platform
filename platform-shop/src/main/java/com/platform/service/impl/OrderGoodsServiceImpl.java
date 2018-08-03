package com.platform.service.impl;

import com.platform.utils.excel.ExcelExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.platform.dao.OrderGoodsDao;
import com.platform.entity.OrderGoodsEntity;
import com.platform.service.OrderGoodsService;

import javax.servlet.http.HttpServletResponse;


@Service("orderGoodsService")
public class OrderGoodsServiceImpl implements OrderGoodsService {
	@Autowired
	private OrderGoodsDao orderGoodsDao;
	
	@Override
	public OrderGoodsEntity queryObject(Integer id){
		return orderGoodsDao.queryObject(id);
	}
	
	@Override
	public List<OrderGoodsEntity> queryList(Map<String, Object> map){
		return orderGoodsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return orderGoodsDao.queryTotal(map);
	}
	
	@Override
	public void save(OrderGoodsEntity orderGoods){
		orderGoodsDao.save(orderGoods);
	}
	
	@Override
	public void update(OrderGoodsEntity orderGoods){
		orderGoodsDao.update(orderGoods);
	}
	
	@Override
	public void delete(Integer id){
		orderGoodsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		orderGoodsDao.deleteBatch(ids);
	}

	@Override
	public void exportByVariety(String date, HttpServletResponse response) {
		String[] header = new String[]{"品种", "数量"};

		List<OrderGoodsEntity> orderGoods = orderGoodsDao.listByVariety(date);
		List<Object[]> body = new ArrayList<>();

		for (OrderGoodsEntity orderGood : orderGoods) {
			Object[] obj = new Object[]{orderGood.getGoodsName(), orderGood.getNumber()};
			body.add(obj);
		}

		ExcelExport excelExport = new ExcelExport("采购清单");
		excelExport.addSheetByArray("采购列表", body, header);
		excelExport.export(response);
	}
}
