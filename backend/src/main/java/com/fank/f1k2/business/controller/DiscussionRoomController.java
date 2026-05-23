package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fank.f1k2.business.entity.DiscussionRoomMember;
import com.fank.f1k2.business.entity.StaffInfo;
import com.fank.f1k2.business.service.IDiscussionRoomMemberService;
import com.fank.f1k2.business.service.IStaffInfoService;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.DiscussionRoom;
import com.fank.f1k2.business.service.IDiscussionRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;

/**
 * 讨论房间表 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/discussion-room")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscussionRoomController {

    private final IDiscussionRoomService bulletinInfoService;

    private final IDiscussionRoomMemberService discussionRoomMemberService;

    private final IStaffInfoService staffInfoService;

    /**
     * 分页获取讨论房间表
     *
     * @param page      分页对象
     * @param queryFrom 讨论房间表
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DiscussionRoom> page, DiscussionRoom queryFrom) {
        return R.ok(bulletinInfoService.queryPage(page, queryFrom));
    }

    /**
     * 查询讨论房间表详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
     * 查询讨论房间表列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list(Integer userId) {
        List<DiscussionRoom> roomList = bulletinInfoService.list(Wrappers.<DiscussionRoom>lambdaQuery().ne(DiscussionRoom::getRoomStatus, 2));

        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, userId));
        List<DiscussionRoomMember> memberList = discussionRoomMemberService.list(Wrappers.<DiscussionRoomMember>lambdaQuery().eq(DiscussionRoomMember::getUserId, staffInfo.getId()));
        Map<Integer, DiscussionRoomMember> memberMap = memberList.stream().collect(Collectors.toMap(DiscussionRoomMember::getRoomId, v -> v));

        for (DiscussionRoom room : roomList) {
            int currentCount = discussionRoomMemberService.count(Wrappers.<DiscussionRoomMember>lambdaQuery()
                    .eq(DiscussionRoomMember::getRoomId, room.getId())
                    .eq(DiscussionRoomMember::getMemberStatus, 1));
            DiscussionRoomMember currentUserMember = memberMap.get(room.getId());
            room.setCurrentUserMemberFlag(currentUserMember == null ? "0" : "1");
            room.setCurrentMemberCount(currentCount);
        }

        return R.ok(roomList);
    }

    /**
     * 讨论房间开始
     *
     * @param id 讨论房间ID
     * @return 讨论房间对象
     */
    @GetMapping("/start")
    public R startRoomMeeting(Integer id) {
        DiscussionRoom room = bulletinInfoService.getById(id);
        room.setStartTime(DateUtil.formatDateTime(new Date()));
        room.setRoomStatus(1);
        return R.ok(bulletinInfoService.updateById(room));
    }

    /**
     * 新增讨论房间表
     *
     * @param addFrom 讨论房间表对象
     * @return 结果
     */
    @PostMapping
    public R save(DiscussionRoom addFrom) {
        return R.ok(bulletinInfoService.save(addFrom));
    }

    /**
     * 修改讨论房间表
     *
     * @param editFrom 讨论房间表对象
     * @return 结果
     */
    @PutMapping
    public R edit(DiscussionRoom editFrom) {
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
     * 删除讨论房间表
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
