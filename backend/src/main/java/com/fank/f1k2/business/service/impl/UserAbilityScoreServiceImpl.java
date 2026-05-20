package com.fank.f1k2.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.StaffInfo;
import com.fank.f1k2.business.entity.UserAbilityScore;
import com.fank.f1k2.business.dao.UserAbilityScoreMapper;
import com.fank.f1k2.business.service.IStaffInfoService;
import com.fank.f1k2.business.service.IUserAbilityScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
public class UserAbilityScoreServiceImpl extends ServiceImpl<UserAbilityScoreMapper, UserAbilityScore> implements IUserAbilityScoreService {

    @Resource
    private IStaffInfoService staffInfoService;

    /**
     * 分页获取用户能力维度得分表
     *
     * @param page      分页对象
     * @param queryFrom 用户能力维度得分表
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<UserAbilityScore> page, UserAbilityScore queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }

    /**
     * 根据用户ID查询用户能力得分趋势
     *
     * @param userId 用户ID
     * @return 讨论发言记录表列表
     */
    @Override
    public List<LinkedHashMap<String, Object>> queryScoreTrendByUser(Integer userId) {
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, userId));
        if (staffInfo == null) {
            return null;
        }

        List<UserAbilityScore> userAbilityScoreList = list(Wrappers.<UserAbilityScore>lambdaQuery()
                .eq(UserAbilityScore::getUserId, staffInfo.getId())
                .orderByAsc(UserAbilityScore::getCreateDate));

        if (userAbilityScoreList == null || userAbilityScoreList.isEmpty()) {
            return null;
        }

        Map<String, List<UserAbilityScore>> dimensionGroupMap = userAbilityScoreList.stream()
                .collect(Collectors.groupingBy(UserAbilityScore::getDimension, LinkedHashMap::new, Collectors.toList()));

        List<LinkedHashMap<String, Object>> trendList = new java.util.ArrayList<>();

        for (Map.Entry<String, List<UserAbilityScore>> entry : dimensionGroupMap.entrySet()) {
            String dimension = entry.getKey();
            List<UserAbilityScore> scores = entry.getValue();

            LinkedHashMap<String, Object> dimensionData = new LinkedHashMap<>();
            dimensionData.put("dimension", dimension);

            List<LinkedHashMap<String, Object>> scorePoints = new java.util.ArrayList<>();
            for (UserAbilityScore score : scores) {
                LinkedHashMap<String, Object> point = new LinkedHashMap<>();
                point.put("createDate", score.getCreateDate());
                point.put("rawScore", score.getRawScore());
                point.put("finalScore", score.getFinalScore());
                scorePoints.add(point);
            }

            dimensionData.put("scores", scorePoints);
            trendList.add(dimensionData);
        }

        return trendList;
    }
}
