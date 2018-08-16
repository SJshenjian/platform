package com.platform.dao;

import com.platform.entity.GoodsEntity;

/**
 * Dao
 *
 * @author Jian Shen
 * @email SJshenjian@outlook.com
 * @date 2017-08-21 21:19:49
 */
public interface GoodsDao extends BaseDao<GoodsEntity> {
    Integer queryMaxId();
}
