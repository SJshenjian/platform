package com.platform.controller;

import com.platform.entity.OrderEntity;
import com.platform.service.OrderService;
import com.platform.service.SysUserService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author Jian Shen
 * @email SJshenjian@outlook.com
 * @date 2017-08-13 10:41:09
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:list")
    public R list(@RequestParam Map<String, Object> params) {
        String address = sysUserService.queryObject(ShiroUtils.getUserId()).getDeptName();// 新增按网点查询订单
        if (!"总部".equals(address)) {
            params.put("address", address);
        }

        // 查询列表数据
        Query query = new Query(params);
        List<OrderEntity> orderList = orderService.queryList(query);
        int total = orderService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(orderList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    @RequestMapping("/listOrder")
    @GetMapping
    public R list(@RequestParam String orderDate) {
        List<Object> orders = orderService.listOrder(orderDate);

        return R.ok().put("orders", orders);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("order:info")
    public R info(@PathVariable("id") Integer id) {
        OrderEntity orders = orderService.queryObject(id);

        return R.ok().put("orders", orders);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("order:save")
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:update")
    public R update(@RequestBody OrderEntity order) {
        orderService.update(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("order:delete")
    public R delete(@RequestBody Integer[] ids) {
        orderService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<OrderEntity> list = orderService.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 总计
     */
    @RequestMapping("/queryTotal")
    public R queryTotal(@RequestParam Map<String, Object> params) {
        int sum = orderService.queryTotal(params);

        return R.ok().put("sum", sum);
    }

    /**
     * 一键确定收货(针对当日订单)
     */
    @RequestMapping("/batchConfirm")
    @RequiresPermissions("order:batchConfirm")
    public R batchConfirm() {
        orderService.batchConfirm();

        return R.ok();
    }

    /**
     * 一键发货(针对当日订单)
     */
    @RequestMapping("/batchSendGoods")
    @RequiresPermissions("order:batchSendGoods")
    public R batchSendGoods() {
        orderService.batchSendGoods();

        return R.ok();
    }
}
