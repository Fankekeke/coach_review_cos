package com.fank.f1k2.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.CollectionInfo;
import com.fank.f1k2.business.dao.CollectionInfoMapper;
import com.fank.f1k2.business.entity.CollectionOptions;
import com.fank.f1k2.business.service.ICollectionInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fank.f1k2.business.service.ICollectionOptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectionInfoServiceImpl extends ServiceImpl<CollectionInfoMapper, CollectionInfo> implements ICollectionInfoService {

    private final ICollectionOptionsService collectionOptionsService;

    /**
     * 分页获取题目表
     *
     * @param page       分页对象
     * @param queryFrom 题目表
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<CollectionInfo> page, CollectionInfo queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }

    /**
     * 查询题目表列表（根据题库ID）
     *
     * @param bankId 题库ID
     * @return 列表
     */
    @Override
    public List<LinkedHashMap<String, Object>> queryCollectionListByBank(Integer bankId) {
        List<CollectionInfo> list = this.list(Wrappers.<CollectionInfo>lambdaQuery().eq(CollectionInfo::getCollectionId, bankId));
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<String> collectionCodeList = list.stream().map(CollectionInfo::getCode).collect(Collectors.toList());
        List<CollectionOptions> collectionOptionsList = collectionOptionsService.list(Wrappers.<CollectionOptions>lambdaQuery().in(CollectionOptions::getQuestionCode, collectionCodeList));
        Map<String, List<CollectionOptions>> collectionOptionsMap = collectionOptionsList.stream().collect(Collectors.groupingBy(CollectionOptions::getQuestionCode));

        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        for (CollectionInfo collectionInfo : list) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("collectionInfo", collectionInfo);
            map.put("collectionOptionsList", collectionOptionsMap.get(collectionInfo.getCode()));
            result.add(map);
        }
        return result;
    }
}
