package com.fank.f1k2.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.UserAbilityScore;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
public interface IUserAbilityScoreService extends IService<UserAbilityScore> {

    /**
     * 分页获取用户能力维度得分表
     *
     * @param page      分页对象
     * @param queryFrom 用户能力维度得分表
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<UserAbilityScore> page, UserAbilityScore queryFrom);

    /**
     * 根据用户ID查询用户能力得分趋势
     *
     * @param userId 用户ID
     * @return 讨论发言记录表列表
     */
    List<LinkedHashMap<String, Object>> queryScoreTrendByUser(Integer userId);
}
