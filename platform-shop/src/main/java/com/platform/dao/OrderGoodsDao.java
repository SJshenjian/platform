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

    List<OrderGoodsEntity> listByVariety(@Param("date") String date);
}
