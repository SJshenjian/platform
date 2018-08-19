package com.platform.service.impl;

import com.platform.dao.OrderDao;
import com.platform.dao.OrderGoodsDao;
import com.platform.dao.ShippingDao;
import com.platform.entity.OrderEntity;
import com.platform.entity.OrderGoodsEntity;
import com.platform.entity.ShippingEntity;
import com.platform.service.OrderService;
import com.platform.utils.RRException;
import com.platform.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ShippingDao shippingDao;
    @Autowired
    private OrderGoodsDao orderGoodsDao;

    @Override
    public OrderEntity queryObject(Integer id) {
        return orderDao.queryObject(id);
    }

    @Override
    public List<OrderEntity> queryList(Map<String, Object> map) {
        return orderDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return orderDao.queryTotal(map);
    }

    @Override
    public int save(OrderEntity order) {
        return orderDao.save(order);
    }

    @Override
    public int update(OrderEntity order) {
        return orderDao.update(order);
    }

    @Override
    public int delete(Integer id) {
        return orderDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return orderDao.deleteBatch(ids);
    }

    @Override
    public int confirm(Integer id) {
        OrderEntity order = queryObject(id);
        Integer shippingStatus = order.getShippingStatus();//发货状态
        Integer payStatus = order.getPayStatus();//付款状态
        if (2 != payStatus) {
            throw new RRException("此订单未付款，不能确认收货！");
        }
        if (4 == shippingStatus) {
            throw new RRException("此订单处于退货状态，不能确认收货！");
        }
        if (0 == shippingStatus) {
            throw new RRException("此订单未发货，不能确认收货！");
        }
        order.setOrderStatus(301);//订单已收货
        order.setShippingStatus(2);//已收货
        return orderDao.update(order);
    }

    @Override
    public int sendGoods(OrderEntity order) {
        Integer payStatus = order.getPayStatus();//付款状态
        if (2 != payStatus) {
            throw new RRException("此订单未付款！");
        }

        ShippingEntity shippingEntity = shippingDao.queryObject(order.getShippingId());
        if (null != shippingEntity) {
            order.setShippingName(shippingEntity.getName());
        }
        order.setOrderStatus(300);//订单已发货
        order.setShippingStatus(1);//已发货
        return orderDao.update(order);
    }

    @Override
    public List<Object> listOrder(String date) {
        List<Object> results = new ArrayList<>();

        int  id = 0;
        List<String> networks = orderGoodsDao.listNetworkByOrderDate(date);
        for (String network : networks) {
            BigDecimal totalPrice = BigDecimal.ZERO;
            int serialNumber = 0;
            Map<String, Object> result = new HashMap<>();

            List<OrderEntity> orders = orderDao.listOrder(network, date);
            List<OrderEntity> resultOrders = new ArrayList<>();
            for (OrderEntity order : orders) {
                order.setId(++serialNumber);// 序号
                BigDecimal customerTotalPrice = BigDecimal.ZERO;
                if (order.getOrderGoods() != null) {
                    for (int i = 0; i < order.getOrderGoods().size(); i++) {
                        OrderGoodsEntity orderGood = order.getOrderGoods().get(i);
                        if (i != 0) {
                            OrderEntity orderEntity = new OrderEntity();
                            orderEntity.setGoodsName(orderGood.getGoodsName());
                            orderEntity.setNumber(orderGood.getNumber());
                            orderEntity.setRetailPrice(orderGood.getRetailPrice());
                            orderEntity.setMarketPrice(orderGood.getMarketPrice()); // 此市场价实际代表零售价格乘以数量之积
                            resultOrders.add(orderEntity);
                        } else {
                            order.setGoodsName(orderGood.getGoodsName());
                            order.setNumber(orderGood.getNumber());
                            order.setRetailPrice(orderGood.getRetailPrice());
                            order.setMarketPrice(orderGood.getMarketPrice()); // 此市场价实际代表零售价格乘以数量之积
                            resultOrders.add(order);
                        }
                        customerTotalPrice = customerTotalPrice.add(orderGood.getMarketPrice()); // 计算单个客户的总金额
                    }
                }
                /*OrderEntity orderEntity = new OrderEntity(); // 每个客户购买商品小计功能
                orderEntity.setGoodsName("小计");
                orderEntity.setMarketPrice(customerTotalPrice);
                resultOrders.add(orderEntity);*/
                totalPrice = totalPrice.add(customerTotalPrice); // 计算网点的订单总金额
            }

            result.put("id", id++); // 打印分页使用
            result.put("network", network);
            result.put("orders", resultOrders);
            result.put("totalPrice", totalPrice);

            results.add(result);
        }
        return results;
    }
}
