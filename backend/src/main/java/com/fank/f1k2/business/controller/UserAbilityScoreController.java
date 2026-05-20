package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.UserAbilityScore;
import com.fank.f1k2.business.service.IUserAbilityScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * 用户能力维度得分表 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/user-ability-score")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAbilityScoreController {

    private final IUserAbilityScoreService bulletinInfoService;

    /**
     * 分页获取用户能力维度得分表
     *
     * @param page      分页对象
     * @param queryFrom 用户能力维度得分表
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<UserAbilityScore> page, UserAbilityScore queryFrom) {
        return R.ok(bulletinInfoService.queryPage(page, queryFrom));
    }

    /**
     * 根据用户ID查询用户能力得分趋势
     *
     * @param userId 用户ID
     * @return 讨论发言记录表列表
     */
    @GetMapping("/queryScoreTrendByUser")
    public R queryScoreTrendByUser(Integer userId) {
        return R.ok(bulletinInfoService.queryScoreTrendByUser(userId));
    }

    /**
     * 查询用户能力维度得分表详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
     * 查询用户能力维度得分表列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 新增用户能力维度得分表
     *
     * @param addFrom 用户能力维度得分表对象
     * @return 结果
     */
    @PostMapping
    public R save(UserAbilityScore addFrom) {
        return R.ok(bulletinInfoService.save(addFrom));
    }

    /**
     * 修改用户能力维度得分表
     *
     * @param editFrom 用户能力维度得分表对象
     * @return 结果
     */
    @PutMapping
    public R edit(UserAbilityScore editFrom) {
        return R.ok(bulletinInfoService.updateById(editFrom));
    }

    /**
     * 删除用户能力维度得分表
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
