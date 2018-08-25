package com.platform.dao;

import com.platform.entity.CartEntity;

/**
 * 
 * 
 * @author Jian Shen
 * @email SJshenjian@outlook.com
 * @date 2017-08-13 10:41:06
 */
public interface CartDao extends BaseDao<CartEntity> {

    int updateCartByGoodsID(CartEntity cartEntity);
}
