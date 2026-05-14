package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.CollectionInfo;
import com.fank.f1k2.business.service.ICollectionInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * 题目表 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/collection-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectionInfoController {

    private final ICollectionInfoService bulletinInfoService;

    /**
     * 分页获取题目表
     *
     * @param page      分页对象
     * @param queryFrom 题目表
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<CollectionInfo> page, CollectionInfo queryFrom) {
        return R.ok(bulletinInfoService.queryPage(page, queryFrom));
    }

    /**
     * 查询题目表列表（根据题库ID）
     *
     * @param bankId 题库ID
     * @return 列表
     */
    @GetMapping("/queryCollectionListByBank")
    public R queryCollectionListByBank(Integer bankId) {
        return R.ok(bulletinInfoService.queryCollectionListByBank(bankId));
    }

    /**
     * 查询题目表详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
     * 查询题目表列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 新增题目表
     *
     * @param addFrom 题目表对象
     * @return 结果
     */
    @PostMapping
    public R save(CollectionInfo addFrom) {
        return R.ok(bulletinInfoService.save(addFrom));
    }

    /**
     * 修改题目表
     *
     * @param editFrom 题目表对象
     * @return 结果
     */
    @PutMapping
    public R edit(CollectionInfo editFrom) {
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
     * 删除题目表
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
