package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fank.f1k2.business.entity.StaffInfo;
import com.fank.f1k2.business.service.IStaffInfoService;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.AssessmentTask;
import com.fank.f1k2.business.service.IAssessmentTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * 自我评估任务表 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/assessment-task")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssessmentTaskController {

    private final IAssessmentTaskService bulletinInfoService;

    private final IStaffInfoService staffInfoService;

    /**
     * 分页获取自我评估任务表
     *
     * @param page      分页对象
     * @param queryFrom 自我评估任务表
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<AssessmentTask> page, AssessmentTask queryFrom) {
        return R.ok(bulletinInfoService.queryPage(page, queryFrom));
    }

    /**
     * 查询自我评估任务表详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
     * 查询自我评估任务表列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 新增自我评估任务表
     *
     * @param addFrom 自我评估任务表对象
     * @return 结果
     */
    @PostMapping
    public R save(AssessmentTask addFrom) {
        addFrom.setCreateTime(DateUtil.formatDateTime(new Date()));
        return R.ok(bulletinInfoService.save(addFrom));
    }

    /**
     * 修改自我评估任务表
     *
     * @param editFrom 自我评估任务表对象
     * @return 结果
     */
    @PutMapping
    public R edit(AssessmentTask editFrom) {
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
     * 删除自我评估任务表
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
