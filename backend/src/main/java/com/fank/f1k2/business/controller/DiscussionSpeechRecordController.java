package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fank.f1k2.business.entity.StaffInfo;
import com.fank.f1k2.business.service.IStaffInfoService;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.DiscussionSpeechRecord;
import com.fank.f1k2.business.service.IDiscussionSpeechRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * 讨论发言记录表 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/discussion-speech-record")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscussionSpeechRecordController {

    private final IDiscussionSpeechRecordService bulletinInfoService;

    private final IStaffInfoService staffInfoService;

    /**
     * 分页获取讨论发言记录表
     *
     * @param page      分页对象
     * @param queryFrom 讨论发言记录表
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DiscussionSpeechRecord> page, DiscussionSpeechRecord queryFrom) {
        return R.ok(bulletinInfoService.queryPage(page, queryFrom));
    }

    /**
     * 根据房间ID查询讨论发言记录表
     *
     * @param roomId 房间ID
     * @return 讨论发言记录表列表
     */
    @GetMapping("/queryRecordByRoomId")
    public R queryRecordByRoomId(Integer roomId) {
        return R.ok(bulletinInfoService.queryRecordByRoomId(roomId));
    }

    /**
     * 查询讨论发言记录表详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
     * 查询讨论发言记录表列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 新增讨论发言记录表
     *
     * @param addFrom 讨论发言记录表对象
     * @return 结果
     */
    @PostMapping
    public R save(DiscussionSpeechRecord addFrom) {
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, addFrom.getUserId()));
        addFrom.setUserId(staffInfo.getId());
        addFrom.setCreateTime(DateUtil.formatDateTime(new Date()));
        addFrom.setSpeechTime(DateUtil.formatDateTime(new Date()));
        return R.ok(bulletinInfoService.save(addFrom));
    }

    /**
     * 修改讨论发言记录表
     *
     * @param editFrom 讨论发言记录表对象
     * @return 结果
     */
    @PutMapping
    public R edit(DiscussionSpeechRecord editFrom) {
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
     * 删除讨论发言记录表
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
