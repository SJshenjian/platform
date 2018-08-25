package com.platform.service;

import com.platform.entity.OrderEntity;

import java.util.List;
import java.util.Map;

/**
 * @author Jian Shen
 * @email SJshenjian@outlook.com
 * @date 2017-08-13 10:41:09
 */
public interface OrderService {

    OrderEntity queryObject(Integer id);

    List<OrderEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    int save(OrderEntity order);

    int update(OrderEntity order);

    int delete(Integer id);

    int deleteBatch(Integer[] ids);

    int confirm(Integer id);

    int batchConfirm();

    int sendGoods(OrderEntity order);

    int batchSendGoods();

    List<Object> listOrder(String date);
}
