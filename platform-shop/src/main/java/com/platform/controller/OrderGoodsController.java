package com.platform.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.platform.Global;
import com.platform.entity.SysUserEntity;
import com.platform.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.platform.entity.OrderGoodsEntity;
import com.platform.service.OrderGoodsService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-13 10:41:09
 */
@RestController
@RequestMapping("ordergoods")
public class OrderGoodsController {
	@Autowired
	private OrderGoodsService orderGoodsService;

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("ordergoods:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<OrderGoodsEntity> orderGoodsList = orderGoodsService.queryList(query);
		int total = orderGoodsService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(orderGoodsList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}


	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("ordergoods:info")
	public R info(@PathVariable("id") Integer id){
		OrderGoodsEntity orderGoods = orderGoodsService.queryObject(id);

		return R.ok().put("orderGoods", orderGoods);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("ordergoods:save")
	public R save(@RequestBody OrderGoodsEntity orderGoods){
		orderGoodsService.save(orderGoods);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("ordergoods:update")
	public R update(@RequestBody OrderGoodsEntity orderGoods){
		orderGoodsService.update(orderGoods);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("ordergoods:delete")
	public R delete(@RequestBody Integer[] ids){
		orderGoodsService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 查看所有列表
	 */
	@RequestMapping("/queryAll")
	public R queryAll(@RequestParam Map<String, Object> params) {
		List<OrderGoodsEntity> list = orderGoodsService.queryList(params);

		return R.ok().put("list", list);
	}

	/**
	 * 打印某一日各品种订单总量 Jian Shen 20180801
	 */
	@RequestMapping("/exportByVariety")
	@GetMapping
	public void exportByVariety(@RequestParam String orderDate, HttpServletResponse response) {
		orderGoodsService.exportByVariety(orderDate, response);
	}

	/**
	 * 打印某一日各网点各品种订单总量 Jian Shen 20180804
	 */
	@RequestMapping("/exportByNetwork")
	@GetMapping
	public void exportByNetwork(@RequestParam String orderDate, HttpServletResponse response) {
		orderGoodsService.exportNetworkByOrderDate(orderDate, response);
	}

	/**
	 *  获取我的佣金相关信息 Jian Shen 20180906
	 */
	@RequestMapping("/income")
	@GetMapping
	public R income(@RequestParam String orderDate, @RequestParam int page, HttpServletRequest request) {
		SysUserEntity sysUserEntity = (SysUserEntity) SecurityUtils.getSubject().getSession().getAttribute(Global.CURRENT_USER);
		if (null == sysUserEntity) {
			return R.error("会话已过期，请重新登录");
		}

		Map<String, Object> params = new HashMap<>();
		params.put("username", sysUserEntity.getUsername());

		List<SysUserEntity> sysUserEntities = sysUserService.queryList(params);

		if (sysUserEntities != null) {
			String[] orderDates = orderDate.split(",");
			orderDate = orderDates[orderDates.length - 1];
			List<OrderGoodsEntity> list = orderGoodsService.listMyIncome(orderDate, sysUserEntities.get(0).getDeptName());

			PageUtils pageUtil = new PageUtils(list, list.size(), 10, page);

			return R.ok().put("page", pageUtil);
		}
		return null;
	}



	
}
