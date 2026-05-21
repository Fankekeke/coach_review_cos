package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fank.f1k2.business.service.IMailService;
import com.fank.f1k2.common.service.CacheService;
import com.fank.f1k2.common.service.RedisService;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.StaffInfo;
import com.fank.f1k2.business.service.IStaffInfoService;
import com.fank.f1k2.system.domain.User;
import com.fank.f1k2.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * 教练管理 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Api(tags = "教练管理")
@RestController
@RequestMapping("/business/staff-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StaffInfoController {

    private final IStaffInfoService staffInfoService;

    private final UserService userService;

    private final CacheService cacheService;

    private final TemplateEngine templateEngine;

    private final IMailService iMailService;

    private final RedisService redisService;

    /**
     * 验证码验证
     * @param validateCode
     * @return
     */
    @SneakyThrows
    @GetMapping("/verification/check")
    public R verificationCheck(String validateCode, String email) {
        //1:获取redis里面的验证码信息
        String code = redisService.get(email);
        //2:判断验证码是否正确
        if (!StringUtils.isEmpty(validateCode)) {
            validateCode = validateCode.toLowerCase();
            code = code.toLowerCase();
            if (validateCode.equals(code)) {
                // 删除key
                redisService.del(email);
                return R.ok(true);
            }
        }
        return R.ok(false);
    }

    /**
     * 发送注册邮件
     * @param email
     * @return
     */
    @SneakyThrows
    @GetMapping("/register/email")
    public R sendRegisterEmail(String email) {
        // 判断邮箱是否重复
        Integer count = staffInfoService.count(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getEmail, email));
        if (count > 0) {
            return R.ok(false);
        }
        // 绘制随机字符
        String randomString = "";
        for (int i = 1; i <= 50; i++) {
            randomString = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        }

        Context context = new Context();
        context.setVariable("today", DateUtil.formatDate(new Date()));
        context.setVariable("verifyCode", randomString);
        String emailContent = templateEngine.process("registerEmail", context);
        iMailService.sendHtmlMail(email, "账户验证", emailContent);
        // 将随机生成的验证码放入Redis中
        redisService.set(email, randomString);
        return R.ok(true);
    }

    /**
     * 分页获取教练管理
     *
     * @param page      分页对象
     * @param queryFrom 教练管理
     * @return 结果
     */
    @ApiOperation(value = "分页查询教练", notes = "根据分页和筛选条件获取教练信息")
    @GetMapping("/page")
    public R page(Page<StaffInfo> page, StaffInfo queryFrom) {
        return R.ok(staffInfoService.queryPage(page, queryFrom));
    }

    /**
     * 根据系统用户ID查询教练信息
     *
     * @param sysUserId 系统用户ID
     * @return 教练信息
     */
    @ApiOperation(value = "按用户ID查教练", notes = "通过系统用户ID获取对应的教练信息")
    @GetMapping("/queryStaffBySysUserId")
    public R queryStaffBySysUserId(Integer sysUserId) {
        return R.ok(staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery()
                .eq(StaffInfo::getSysUserId, sysUserId)));
    }

    /**
     * 查询教练列表
     *
     * @param queryFrom 教练管理
     * @return 列表
     */
    @ApiOperation(value = "查询教练列表", notes = "根据条件列出当前所有教练信息")
    @GetMapping("/queryStaffList")
    public R queryStaffList(StaffInfo queryFrom) {
        return R.ok(staffInfoService.queryStaffList(queryFrom));
    }

    /**
     * 查询教练管理详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @ApiOperation(value = "教练详情", notes = "通过ID获取教练详细信息")
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(staffInfoService.getById(id));
    }

    /**
     * 查询教练管理列表
     *
     * @return 结果
     */
    @ApiOperation(value = "获取教练集合", notes = "列出所有教练记录")
    @GetMapping("/list")
    public R list() {
        return R.ok(staffInfoService.list());
    }

    /**
     * 获取教练看板信息
     *
     * @param userId 用户ID
     * @return 教练信息
     */
    @GetMapping("/queryStaffBoard")
    public R queryStaffBoard(Integer userId) {
        return R.ok(staffInfoService.queryStaffBoard(userId));
    }

    /**
     * 新增教练管理
     *
     * @param addFrom 教练管理对象
     * @return 结果
     */
    @ApiOperation(value = "新增教练", notes = "创建一个新的教练并注册关联的系统用户")
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(StaffInfo addFrom) throws Exception {
        addFrom.setCode("SAF-" + System.currentTimeMillis());
        addFrom.setCreateDate(DateUtil.formatDateTime(new Date()));
        staffInfoService.save(addFrom);
        return R.ok(true);
    }

    /**
     * 修改教练管理
     *
     * @param editFrom 教练管理对象
     * @return 结果
     */
    @ApiOperation(value = "修改教练信息", notes = "更新已有的教练信息")
    @PutMapping
    public R edit(StaffInfo editFrom) {
        return R.ok(staffInfoService.updateById(editFrom));
    }

    /**
     * 删除教练管理
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @ApiOperation(value = "删除教练", notes = "根据ID集合批量删除教练记录")
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(staffInfoService.removeByIds(ids));
    }
}
