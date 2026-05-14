package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.CollectionOptions;
import com.fank.f1k2.business.service.ICollectionOptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * 题目选项表 控制层
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/collection-options")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectionOptionsController {

    private final ICollectionOptionsService bulletinInfoService;

    /**
    * 分页获取题目选项表
    *
    * @param page       分页对象
    * @param queryFrom 题目选项表
    * @return 结果
    */
    @GetMapping("/page")
    public R page(Page<CollectionOptions> page, CollectionOptions queryFrom) {
        return R.ok();
    }

    /**
    * 查询题目选项表详情
    *
    * @param id 主键ID
    * @return 结果
    */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
    * 查询题目选项表列表
    *
    * @return 结果
    */
    @GetMapping("/list")
    public R list() {
        return R.ok(bulletinInfoService.list());
    }

    /**
    * 新增题目选项表
    *
    * @param addFrom 题目选项表对象
    * @return 结果
    */
    @PostMapping
    public R save(CollectionOptions addFrom) {
        return R.ok(bulletinInfoService.save(addFrom));
    }

    /**
    * 修改题目选项表
    *
    * @param editFrom 题目选项表对象
    * @return 结果
    */
    @PutMapping
    public R edit(CollectionOptions editFrom) {
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
    * 删除题目选项表
    *
    * @param ids 删除的主键ID
    * @return 结果
    */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
