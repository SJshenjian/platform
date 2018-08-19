package com.platform.dao;

import com.platform.entity.OrderEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Jian Shen
 * @email SJshenjian@outlook.com
 * @date 2017-08-13 10:41:09
 */
public interface OrderDao extends BaseDao<OrderEntity> {

    // 查询网点的出库清单
    List<OrderEntity> listOrder(@Param("address") String address, @Param("date") String date);
}
