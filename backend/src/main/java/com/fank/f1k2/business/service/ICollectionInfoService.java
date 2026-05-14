package com.fank.f1k2.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.CollectionInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
public interface ICollectionInfoService extends IService<CollectionInfo> {

    /**
     * 分页获取题目表
     *
     * @param page       分页对象
     * @param queryFrom 题目表
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<CollectionInfo> page, CollectionInfo queryFrom);

    /**
     * 查询题目表列表（根据题库ID）
     *
     * @param bankId 题库ID
     * @return 列表
     */
    List<LinkedHashMap<String, Object>> queryCollectionListByBank(Integer bankId);
}
