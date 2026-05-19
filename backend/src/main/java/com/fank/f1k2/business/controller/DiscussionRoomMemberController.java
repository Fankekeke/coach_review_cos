package com.fank.f1k2.business.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fank.f1k2.business.entity.StaffInfo;
import com.fank.f1k2.business.service.IStaffInfoService;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.DiscussionRoomMember;
import com.fank.f1k2.business.service.IDiscussionRoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;

/**
 * 讨论房间成员表 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/discussion-room-member")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscussionRoomMemberController {

    private final IDiscussionRoomMemberService bulletinInfoService;

    private final IStaffInfoService staffInfoService;

    /**
     * 分页获取讨论房间成员表
     *
     * @param page      分页对象
     * @param queryFrom 讨论房间成员表
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DiscussionRoomMember> page, DiscussionRoomMember queryFrom) {
        return R.ok(bulletinInfoService.queryPage(page, queryFrom));
    }

    /**
     * 根据房间ID查询讨论房间成员表
     *
     * @param roomId 房间ID
     * @return 列表
     */
    @GetMapping("/queryRoomMember")
    public R queryRoomMember(Integer roomId) {
        List<DiscussionRoomMember> list = bulletinInfoService.list(Wrappers.<DiscussionRoomMember>lambdaQuery().eq(DiscussionRoomMember::getRoomId, roomId).eq(DiscussionRoomMember::getMemberStatus, 1));
        if (CollectionUtil.isEmpty(list)) {
            return R.ok(Collections.emptyList());
        }
        List<StaffInfo> staffInfoList = staffInfoService.list(Wrappers.<StaffInfo>lambdaQuery().in(StaffInfo::getId, list.stream().map(DiscussionRoomMember::getUserId).collect(Collectors.toList())));
        return R.ok(staffInfoList);
    }

    /**
     * 添加讨论房间成员表
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     * @return 添加结果
     */
    @GetMapping("/addRoomMember")
    public R addRoomMember(Integer roomId, Integer userId) {
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, userId));

        DiscussionRoomMember discussionRoomMember = bulletinInfoService.getOne(Wrappers.<DiscussionRoomMember>lambdaQuery().eq(DiscussionRoomMember::getRoomId, roomId).eq(DiscussionRoomMember::getUserId, staffInfo.getId()));
        if (discussionRoomMember != null && discussionRoomMember.getMemberStatus() == 2) {
            discussionRoomMember.setMemberStatus(1);
            discussionRoomMember.setLeaveTime(null);
            discussionRoomMember.setJoinTime(DateUtil.formatDateTime(new Date()));
            return R.ok(bulletinInfoService.updateById(discussionRoomMember));
        } else {
            discussionRoomMember = new DiscussionRoomMember();
            discussionRoomMember.setUserId(staffInfo.getId());
            discussionRoomMember.setJoinTime(DateUtil.formatDateTime(new Date()));
            discussionRoomMember.setMemberStatus(1);
            return R.ok(bulletinInfoService.save(discussionRoomMember));
        }
    }

    /**
     * 退出讨论房间
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     * @return 退出结果
     */
    @GetMapping("/exitRoom")
    public R exitRoom(Integer roomId, Integer userId) {
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, userId));
        DiscussionRoomMember discussionRoomMember = bulletinInfoService.getOne(Wrappers.<DiscussionRoomMember>lambdaQuery().eq(DiscussionRoomMember::getRoomId, roomId).eq(DiscussionRoomMember::getUserId, staffInfo.getId()));
        if (discussionRoomMember != null) {
            discussionRoomMember.setLeaveTime(DateUtil.formatDateTime(new Date()));
            discussionRoomMember.setMemberStatus(2);
            return R.ok(bulletinInfoService.updateById(discussionRoomMember));
        }
        return R.ok(false);
    }

    /**
     * 查询讨论房间成员表详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
     * 查询讨论房间成员表列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 新增讨论房间成员表
     *
     * @param addFrom 讨论房间成员表对象
     * @return 结果
     */
    @PostMapping
    public R save(DiscussionRoomMember addFrom) {
        return R.ok(bulletinInfoService.save(addFrom));
    }

    /**
     * 修改讨论房间成员表
     *
     * @param editFrom 讨论房间成员表对象
     * @return 结果
     */
    @PutMapping
    public R edit(DiscussionRoomMember editFrom) {
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
     * 删除讨论房间成员表
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
