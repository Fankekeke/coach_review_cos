package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fank.f1k2.business.entity.StaffInfo;
import com.fank.f1k2.business.service.IStaffInfoService;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.DiscussionScore;
import com.fank.f1k2.business.service.IDiscussionScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * 讨论评分表 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/discussion-score")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscussionScoreController {

    private final IDiscussionScoreService bulletinInfoService;

    private final IStaffInfoService staffInfoService;

    /**
     * 分页获取讨论评分表
     *
     * @param page      分页对象
     * @param queryFrom 讨论评分表
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DiscussionScore> page, DiscussionScore queryFrom) {
        return R.ok(bulletinInfoService.queryPage(page, queryFrom));
    }

    /**
     * 查询讨论评分表详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
     * 查询讨论评分表列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 新增讨论评分表
     *
     * @param addFrom 讨论评分表对象
     * @return 结果
     */
    @PostMapping
    public R save(DiscussionScore addFrom) {
        BigDecimal professionalScore = addFrom.getProfessionalScore() != null ? addFrom.getProfessionalScore() : BigDecimal.ZERO;
        BigDecimal communicationScore = addFrom.getCommunicationScore() != null ? addFrom.getCommunicationScore() : BigDecimal.ZERO;
        BigDecimal teamworkScore = addFrom.getTeamworkScore() != null ? addFrom.getTeamworkScore() : BigDecimal.ZERO;
        BigDecimal emergencyScore = addFrom.getEmergencyScore() != null ? addFrom.getEmergencyScore() : BigDecimal.ZERO;
        BigDecimal learningScore = addFrom.getLearningScore() != null ? addFrom.getLearningScore() : BigDecimal.ZERO;

        BigDecimal totalScore = professionalScore
                .add(communicationScore)
                .add(teamworkScore)
                .add(emergencyScore)
                .add(learningScore)
                .divide(new BigDecimal(5), 2, RoundingMode.HALF_UP);

        addFrom.setTotalScore(totalScore);
        addFrom.setScoredTime(DateUtil.formatDateTime(new Date()));
        bulletinInfoService.save(addFrom);
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getId, addFrom.getUserId()));
        staffInfoService.calculateStaffScore(staffInfo.getUserId());
        return R.ok(true);
    }

    /**
     * 修改讨论评分表
     *
     * @param editFrom 讨论评分表对象
     * @return 结果
     */
    @PutMapping
    public R edit(DiscussionScore editFrom) {
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
     * 删除讨论评分表
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
