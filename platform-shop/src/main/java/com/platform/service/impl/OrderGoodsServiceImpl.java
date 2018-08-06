package com.platform.service.impl;

import com.platform.utils.excel.ExcelExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

	@Override
	public void exportNetworkByOrderDate(String orderDate, HttpServletResponse response) {
		List<String> networks = orderGoodsDao.listNetworkByOrderDate(orderDate);

		String[] header = new String[]{"网点", "品种", "数量", "付款金额", "佣金"};
		List<Object[]> body = new ArrayList<>();

		BigDecimal totalPrice = new BigDecimal(0);

		for (String network : networks) {
			List<OrderGoodsEntity> orderGoods = orderGoodsDao.listByNetwork(orderDate, network);
			BigDecimal networkTotalPrice = new BigDecimal(0);

			for (int i = 0; i < orderGoods.size(); i++) {
				List<Object> list = new ArrayList<>();
				if (i != 0) {
					list.add(" ");
				} else {
					list.add(network);
				}

				networkTotalPrice = networkTotalPrice.add(orderGoods.get(i).getRetailPrice());

				list.add(orderGoods.get(i).getGoodsName());
				list.add(orderGoods.get(i).getNumber());
				list.add(orderGoods.get(i).getRetailPrice());// 价格
				list.add(orderGoods.get(i).getMarketPrice());// 佣金（目前10%）

				body.add(list.toArray());
			}

			totalPrice = totalPrice.add(networkTotalPrice);

			Object[] networkTotal = new Object[]{"小计", " ", " ", networkTotalPrice, networkTotalPrice.divide(new BigDecimal(10))};
			body.add(networkTotal);
		}

		Object[] total = new Object[]{"总计", " ", " ", totalPrice, totalPrice.divide(new BigDecimal(10))};
		body.add(total);

		ExcelExport excelExport = new ExcelExport("各网点供货清单");
		excelExport.addSheetByArray("供货清单", body, header);
		excelExport.export(response);
	}

	@Override
	public List<OrderGoodsEntity> listMyIncome(String orderDate, String network) {
		List<OrderGoodsEntity> orderGoods= orderGoodsDao.listByNetwork(orderDate, network);
		BigDecimal totalPrice = BigDecimal.ZERO;
		BigDecimal totalBrokerage = BigDecimal.ZERO;

		for (OrderGoodsEntity orderGood : orderGoods) {
			totalPrice = totalPrice.add(orderGood.getRetailPrice());
			totalBrokerage = totalBrokerage.add(orderGood.getMarketPrice());
		}

		OrderGoodsEntity orderGood = new OrderGoodsEntity();

		orderGood.setGoodsName("总计");
		orderGood.setRetailPrice(totalPrice);
		orderGood.setMarketPrice(totalBrokerage);

		orderGoods.add(orderGood);

		return orderGoods;
	}
}
