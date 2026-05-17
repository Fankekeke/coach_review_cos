package com.fank.f1k2.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.dao.*;
import com.fank.f1k2.business.entity.*;
import com.fank.f1k2.business.service.IStaffInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
public class StaffInfoServiceImpl extends ServiceImpl<StaffInfoMapper, StaffInfo> implements IStaffInfoService {

    @Resource
    private AssessmentTaskMapper assessmentTaskMapper;

    @Resource
    private AssessmentSubmissionMapper assessmentSubmissionMapper;

    @Resource
    private AnswerRecordMapper answerRecordMapper;

    @Resource
    private DiscussionScoreMapper discussionScoreMapper;

    /**
     * 分页获取教练管理
     *
     * @param page      分页对象
     * @param queryFrom 教练管理
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<StaffInfo> page, StaffInfo queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }

    /**
     * 查询教练列表
     *
     * @param queryFrom 教练管理
     * @return 列表
     */
    @Override
    public List<LinkedHashMap<String, Object>> queryStaffList(StaffInfo queryFrom) {
        return baseMapper.queryStaffList(queryFrom);
    }

    /**
     * 计算教练积分
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateStaffScore(Integer userId) {
        StaffInfo staffInfo = baseMapper.selectOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, userId));
        if (staffInfo == null) {
            return;
        }

        List<AnswerRecord> answerRecordList = answerRecordMapper.selectList(Wrappers.<AnswerRecord>lambdaQuery().eq(AnswerRecord::getUserId, staffInfo.getId()));
        List<AssessmentSubmission> assessmentSubmissionList = assessmentSubmissionMapper.selectList(Wrappers.<AssessmentSubmission>lambdaQuery().eq(AssessmentSubmission::getUserId, staffInfo.getId()));
        List<DiscussionScore> discussionScoreList = discussionScoreMapper.selectList(Wrappers.<DiscussionScore>lambdaQuery().eq(DiscussionScore::getUserId, staffInfo.getId()));

        Map<String, BigDecimal> dimensionScores = calculateDimensionScores(answerRecordList, assessmentSubmissionList, discussionScoreList);

        saveOrUpdateDimensionScores(staffInfo.getUserId(), dimensionScores);
    }

    private Map<String, BigDecimal> calculateDimensionScores(List<AnswerRecord> answerRecordList,
                                                             List<AssessmentSubmission> assessmentSubmissionList,
                                                             List<DiscussionScore> discussionScoreList) {
        Map<String, BigDecimal> dimensionScores = new LinkedHashMap<>();

        dimensionScores.put("专业技能", calculateProfessionalScore(assessmentSubmissionList, answerRecordList));
        dimensionScores.put("沟通能力", calculateCommunicationScore(discussionScoreList, answerRecordList));
        dimensionScores.put("团队协作", calculateTeamworkScore(discussionScoreList, answerRecordList));
        dimensionScores.put("应急处理", calculateEmergencyScore(discussionScoreList, answerRecordList));
        dimensionScores.put("学习能力", calculateLearningScore(discussionScoreList, answerRecordList));

        return dimensionScores;
    }

    private BigDecimal calculateProfessionalScore(List<AssessmentSubmission> assessmentSubmissionList, List<AnswerRecord> answerRecordList) {
        BigDecimal assessmentScore = BigDecimal.ZERO;
        if (assessmentSubmissionList != null && !assessmentSubmissionList.isEmpty()) {
            BigDecimal totalScore = assessmentSubmissionList.stream()
                    .filter(submission -> submission.getFinalScore() != null)
                    .map(AssessmentSubmission::getFinalScore)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            assessmentScore = totalScore.divide(new BigDecimal(assessmentSubmissionList.size()), 2, RoundingMode.HALF_UP);
        }

        BigDecimal answerScore = calculateAnswerRecordScoreByDimension(answerRecordList, "专业技能");

        return mergeScores(assessmentScore, answerScore);
    }

    private BigDecimal calculateCommunicationScore(List<DiscussionScore> discussionScoreList, List<AnswerRecord> answerRecordList) {
        BigDecimal discussionScore = BigDecimal.ZERO;
        if (discussionScoreList != null && !discussionScoreList.isEmpty()) {
            BigDecimal totalScore = discussionScoreList.stream()
                    .filter(score -> score.getCommunicationScore() != null)
                    .map(DiscussionScore::getCommunicationScore)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            discussionScore = totalScore.divide(new BigDecimal(discussionScoreList.size()), 2, RoundingMode.HALF_UP);
        }

        BigDecimal answerScore = calculateAnswerRecordScoreByDimension(answerRecordList, "沟通能力");

        return mergeScores(discussionScore, answerScore);
    }

    private BigDecimal calculateTeamworkScore(List<DiscussionScore> discussionScoreList, List<AnswerRecord> answerRecordList) {
        BigDecimal discussionScore = BigDecimal.ZERO;
        if (discussionScoreList != null && !discussionScoreList.isEmpty()) {
            BigDecimal totalScore = discussionScoreList.stream()
                    .filter(score -> score.getTeamworkScore() != null)
                    .map(DiscussionScore::getTeamworkScore)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            discussionScore = totalScore.divide(new BigDecimal(discussionScoreList.size()), 2, RoundingMode.HALF_UP);
        }

        BigDecimal answerScore = calculateAnswerRecordScoreByDimension(answerRecordList, "团队协作");

        return mergeScores(discussionScore, answerScore);
    }

    private BigDecimal calculateEmergencyScore(List<DiscussionScore> discussionScoreList, List<AnswerRecord> answerRecordList) {
        BigDecimal discussionScore = BigDecimal.ZERO;
        if (discussionScoreList != null && !discussionScoreList.isEmpty()) {
            BigDecimal totalScore = discussionScoreList.stream()
                    .filter(score -> score.getEmergencyScore() != null)
                    .map(DiscussionScore::getEmergencyScore)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            discussionScore = totalScore.divide(new BigDecimal(discussionScoreList.size()), 2, RoundingMode.HALF_UP);
        }

        BigDecimal answerScore = calculateAnswerRecordScoreByDimension(answerRecordList, "应急处理");

        return mergeScores(discussionScore, answerScore);
    }

    private BigDecimal calculateLearningScore(List<DiscussionScore> discussionScoreList, List<AnswerRecord> answerRecordList) {
        BigDecimal discussionScore = BigDecimal.ZERO;
        if (discussionScoreList != null && !discussionScoreList.isEmpty()) {
            BigDecimal totalScore = discussionScoreList.stream()
                    .filter(score -> score.getLearningScore() != null)
                    .map(DiscussionScore::getLearningScore)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            discussionScore = totalScore.divide(new BigDecimal(discussionScoreList.size()), 2, RoundingMode.HALF_UP);
        }

        BigDecimal answerScore = calculateAnswerRecordScoreByDimension(answerRecordList, "学习能力");

        return mergeScores(discussionScore, answerScore);
    }

    private BigDecimal calculateAnswerRecordScoreByDimension(List<AnswerRecord> answerRecordList, String dimension) {
        if (answerRecordList == null || answerRecordList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        List<AnswerRecord> filteredRecords = answerRecordList.stream()
                .filter(record -> dimension.equals(record.getDimension()))
                .collect(Collectors.toList());

        if (filteredRecords.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalScore = filteredRecords.stream()
                .filter(record -> record.getScore() != null)
                .map(record -> new BigDecimal(record.getScore()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalScore.divide(new BigDecimal(filteredRecords.size()), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal mergeScores(BigDecimal score1, BigDecimal score2) {
        if (score1.compareTo(BigDecimal.ZERO) > 0 && score2.compareTo(BigDecimal.ZERO) > 0) {
            return score1.add(score2).divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
        } else if (score1.compareTo(BigDecimal.ZERO) > 0) {
            return score1;
        } else {
            return score2;
        }
    }

    private void saveOrUpdateDimensionScores(Integer userId, Map<String, BigDecimal> dimensionScores) {
        for (Map.Entry<String, BigDecimal> entry : dimensionScores.entrySet()) {
            String dimension = entry.getKey();
            BigDecimal score = entry.getValue();

            UserAbilityScore existingScore = userAbilityScoreMapper.selectOne(
                    Wrappers.<UserAbilityScore>lambdaQuery()
                            .eq(UserAbilityScore::getUserId, userId)
                            .eq(UserAbilityScore::getDimension, dimension)
            );

            if (existingScore != null) {
                existingScore.setRawScore(score);
                existingScore.setFinalScore(score);
                userAbilityScoreMapper.updateById(existingScore);
            } else {
                UserAbilityScore newScore = new UserAbilityScore();
                newScore.setUserId(userId);
                newScore.setDimension(dimension);
                newScore.setRawScore(score);
                newScore.setFinalScore(score);
                userAbilityScoreMapper.insert(newScore);
            }
        }
    }
}
