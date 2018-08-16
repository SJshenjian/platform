package com.platform.service;

import com.platform.entity.AdPositionEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author Jian Shen
 * @email SJshenjian@outlook.com
 * @date 2017-08-19 12:02:42
 */
public interface AdPositionService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    AdPositionEntity queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<AdPositionEntity> queryList(Map<String, Object> map);

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存实体
     *
     * @param adPosition 实体
     * @return 保存条数
     */
    int save(AdPositionEntity adPosition);

    /**
     * 根据主键更新实体
     *
     * @param adPosition 实体
     * @return 更新条数
     */
    int update(AdPositionEntity adPosition);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Integer id);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    int deleteBatch(Integer[] ids);
}
