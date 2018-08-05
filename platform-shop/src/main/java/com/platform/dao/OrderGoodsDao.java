package com.platform.dao;

import com.platform.entity.OrderGoodsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jian Shen
 * @email SJshenjian@outlook.com
 * @date 2018-08-01
 */
public interface OrderGoodsDao extends BaseDao<OrderGoodsEntity> {

    // 查询所有网点的品种订单量
    List<OrderGoodsEntity> listByVariety(@Param("date") String date);

    // 查询网点订单品种信息
    List<OrderGoodsEntity> listByNetwork(@Param("date") String date, @Param("address") String address);

    // 查询今日存在订单的网点信息
    List<String> listNetworkByOrderDate(@Param("date") String date);
}
