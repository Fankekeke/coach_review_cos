package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fank.f1k2.business.entity.StaffInfo;
import com.fank.f1k2.business.service.IStaffInfoService;
import com.fank.f1k2.business.service.IUserAbilityScoreService;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.AssessmentSubmission;
import com.fank.f1k2.business.service.IAssessmentSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * 评估任务提交表 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/assessment-submission")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssessmentSubmissionController {

    private final IAssessmentSubmissionService bulletinInfoService;

    private final IStaffInfoService staffInfoService;

    private final IUserAbilityScoreService userAbilityScoreService;

    /**
     * 分页获取评估任务提交表
     *
     * @param page      分页对象
     * @param queryFrom 评估任务提交表
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<AssessmentSubmission> page, AssessmentSubmission queryFrom) {
        return R.ok(bulletinInfoService.queryPage(page, queryFrom));
    }

    /**
     * 查询评估任务提交表详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
     * 查询评估任务提交表列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 新增评估任务提交表
     *
     * @param addFrom 评估任务提交表对象
     * @return 结果
     */
    @PostMapping
    public R save(AssessmentSubmission addFrom) {
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, addFrom.getUserId()));
        addFrom.setUserId(staffInfo.getId());
        addFrom.setCreateTime(DateUtil.formatDateTime(new Date()));
        if (addFrom.getSubmissionStatus() == 1) {
            addFrom.setSubmitTime(DateUtil.formatDateTime(new Date()));
        }
        return R.ok(bulletinInfoService.save(addFrom));
    }

    /**
     * 审核评分
     *
     * @param editFrom 评估任务提交表对象
     * @return 结果
     */
    @PutMapping("/auditScore")
    public R auditScore(AssessmentSubmission editFrom) {
        editFrom.setSubmissionStatus(2);
        editFrom.setReviewedTime(DateUtil.formatDateTime(new Date()));
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
     * 修改评估任务提交表
     *
     * @param editFrom 评估任务提交表对象
     * @return 结果
     */
    @PutMapping
    public R edit(AssessmentSubmission editFrom) {
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
     * 删除评估任务提交表
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
