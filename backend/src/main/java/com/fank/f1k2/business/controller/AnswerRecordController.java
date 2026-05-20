package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fank.f1k2.business.entity.StaffInfo;
import com.fank.f1k2.business.service.IStaffInfoService;
import com.fank.f1k2.common.exception.F1k2Exception;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.AnswerRecord;
import com.fank.f1k2.business.service.IAnswerRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * 答题记录 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/answer-record")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnswerRecordController {

    private final IAnswerRecordService bulletinInfoService;

    private final IStaffInfoService staffInfoService;

    /**
     * 分页获取答题记录
     *
     * @param page      分页对象
     * @param queryFrom 答题记录
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<AnswerRecord> page, AnswerRecord queryFrom) {
        return R.ok(bulletinInfoService.queryPage(page, queryFrom));
    }

    /**
     * 查询答题记录详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
     * 查询答题记录列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 新增答题记录
     *
     * @param addFrom 答题记录对象
     * @return 结果
     */
    @PostMapping
    public R save(AnswerRecord addFrom) throws F1k2Exception {
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, addFrom.getUserId()));
        addFrom.setUserId(staffInfo.getId());
        addFrom.setCreateDate(DateUtil.formatDateTime(new Date()));
        bulletinInfoService.addAnswerRecord(addFrom);

        staffInfoService.calculateStaffScore(staffInfo.getUserId());
        return R.ok(true);
    }

    /**
     * 修改答题记录
     *
     * @param editFrom 答题记录对象
     * @return 结果
     */
    @PutMapping
    public R edit(AnswerRecord editFrom) {
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
     * 删除答题记录
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
