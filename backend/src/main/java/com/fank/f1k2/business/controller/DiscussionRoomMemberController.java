package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.DiscussionRoomMember;
import com.fank.f1k2.business.service.IDiscussionRoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
