package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fank.f1k2.business.entity.StaffInfo;
import com.fank.f1k2.business.service.IStaffInfoService;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.PartyQuestions;
import com.fank.f1k2.business.service.IPartyQuestionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * 教练问题表 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/party-questions")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PartyQuestionsController {

    private final IPartyQuestionsService bulletinInfoService;

    private final IStaffInfoService staffInfoService;

    /**
     * 分页获取教练问题表
     *
     * @param page      分页对象
     * @param queryFrom 教练问题表
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PartyQuestions> page, PartyQuestions queryFrom) {
        return R.ok(bulletinInfoService.queryPage(page, queryFrom));
    }

    /**
     * 查询所有问题
     *
     * @return 列表
     */
    @GetMapping("/queryAllQuestions")
    public R queryAllQuestions(){
        return R.ok(bulletinInfoService.queryAllQuestions());
    }

    /**
     * 查询教练问题表详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
     * 查询教练问题表列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 新增教练问题表
     *
     * @param addFrom 教练问题表对象
     * @return 结果
     */
    @PostMapping
    public R save(PartyQuestions addFrom) {
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, addFrom.getUserId()));
        addFrom.setUserId(staffInfo.getId());
        addFrom.setCreatedAt(DateUtil.formatDateTime(new Date()));
        addFrom.setStatus("进行中");
        return R.ok(bulletinInfoService.save(addFrom));
    }

    /**
     * 接受回答
     *
     * @param questionId 问题ID
     * @return 结果
     */
    @GetMapping("/acceptAnswer")
    public R acceptAnswer(Integer questionId) {
        return R.ok(bulletinInfoService.update(Wrappers.<PartyQuestions>lambdaUpdate().set(PartyQuestions::getStatus, "已采纳").eq(PartyQuestions::getId, questionId)));
    }

    /**
     * 修改教练问题表
     *
     * @param editFrom 教练问题表对象
     * @return 结果
     */
    @PutMapping
    public R edit(PartyQuestions editFrom) {
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
     * 删除教练问题表
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
