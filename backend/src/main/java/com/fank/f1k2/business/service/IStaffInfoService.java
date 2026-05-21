package com.fank.f1k2.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.StaffInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
public interface IStaffInfoService extends IService<StaffInfo> {

    /**
     * 分页获取教练管理
     *
     * @param page      分页对象
     * @param queryFrom 教练管理
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<StaffInfo> page, StaffInfo queryFrom);

    /**
     * 查询教练列表
     *
     * @param queryFrom 教练管理
     * @return 列表
     */
    List<LinkedHashMap<String, Object>> queryStaffList(StaffInfo queryFrom);

    /**
     * 获取教练看板信息
     *
     * @param userId 用户ID
     * @return 教练信息
     */
    LinkedHashMap<String, Object> queryStaffBoard(Integer userId);

    /**
     * 计算教练积分
     */
    void calculateStaffScore(Integer userId);
}
