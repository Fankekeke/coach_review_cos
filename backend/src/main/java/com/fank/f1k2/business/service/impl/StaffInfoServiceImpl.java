package com.fank.f1k2.business.service.impl;

import cn.hutool.core.date.DateUtil;
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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private UserAbilityScoreMapper userAbilityScoreMapper;

    @Resource
    private QuestionBankMapper questionBankMapper;



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
     * 获取教练看板信息
     *
     * @param userId 用户ID
     * @return 教练信息
     */
    @Override
    public LinkedHashMap<String, Object> queryStaffBoard(Integer userId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        StaffInfo staffInfo = this.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, userId));

        if (staffInfo == null) {
            return result;
        }

        Integer staffTagId = staffInfo.getTagId();

        List<QuestionBank> questionBankList = questionBankMapper.selectList(Wrappers.<QuestionBank>lambdaQuery());
        List<AssessmentTask> assessmentTaskList = assessmentTaskMapper.selectList(Wrappers.<AssessmentTask>lambdaQuery().eq(AssessmentTask::getStatus, 1));

        List<AnswerRecord> answerRecordList = answerRecordMapper.selectList(Wrappers.<AnswerRecord>lambdaQuery().eq(AnswerRecord::getUserId, staffInfo.getId()));
        List<AssessmentSubmission> assessmentSubmissionList = assessmentSubmissionMapper.selectList(Wrappers.<AssessmentSubmission>lambdaQuery().eq(AssessmentSubmission::getUserId, staffInfo.getId()));

        List<QuestionBank> matchedQuestionBanks = questionBankList.stream()
                .filter(qb -> qb.getTagId() == null || qb.getTagId().equals(staffTagId))
                .collect(Collectors.toList());

        List<AssessmentTask> matchedTasks = assessmentTaskList.stream()
                .filter(task -> task.getTagId() == null || task.getTagId().equals(staffTagId))
                .collect(Collectors.toList());

        List<AnswerRecord> completedAnswerRecords = answerRecordList.stream()
                .filter(record -> matchedQuestionBanks.stream().anyMatch(qb -> qb.getId().equals(record.getBankId())))
                .collect(Collectors.toList());

        List<AssessmentSubmission> completedSubmissions = assessmentSubmissionList.stream()
                .filter(submission -> matchedTasks.stream().anyMatch(task -> task.getId().equals(submission.getTaskId())))
                .collect(Collectors.toList());

        int totalQuestionBanks = matchedQuestionBanks.size();
        int totalTasks = matchedTasks.size();

        int completedQuestionBankCount = (int) matchedQuestionBanks.stream()
                .filter(qb -> completedAnswerRecords.stream().anyMatch(record -> record.getBankId().equals(qb.getId())))
                .count();

        int completedTaskCount = (int) matchedTasks.stream()
                .filter(task -> completedSubmissions.stream()
                        .anyMatch(submission -> submission.getTaskId().equals(task.getId())
                                && submission.getSubmissionStatus() != null
                                && submission.getSubmissionStatus() >= 1))
                .count();

        int pendingQuestionBankCount = totalQuestionBanks - completedQuestionBankCount;
        int pendingTaskCount = totalTasks - completedTaskCount;

        List<BigDecimal> allScores = new java.util.ArrayList<>();

        completedAnswerRecords.stream()
                .filter(record -> record.getTotalScore() != null)
                .forEach(record -> allScores.add(new BigDecimal(record.getTotalScore())));

        completedSubmissions.stream()
                .filter(submission -> submission.getFinalScore() != null)
                .forEach(submission -> allScores.add(submission.getFinalScore()));

        BigDecimal taskAverageScore = BigDecimal.ZERO;
        if (!allScores.isEmpty()) {
            BigDecimal totalScore = allScores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            taskAverageScore = totalScore.divide(new BigDecimal(allScores.size()), 2, RoundingMode.HALF_UP);
        }

        result.put("pendingQuestionBankCount", pendingQuestionBankCount);
        result.put("completedQuestionBankCount", completedQuestionBankCount);
        result.put("totalQuestionBanks", totalQuestionBanks);

        result.put("pendingTaskCount", pendingTaskCount);
        result.put("completedTaskCount", completedTaskCount);
        result.put("totalTasks", totalTasks);

        result.put("taskAverageScore", taskAverageScore);

        List<LinkedHashMap<String, Object>> questionBankListResult = new java.util.ArrayList<>();

        matchedQuestionBanks.forEach(qb -> {
            LinkedHashMap<String, Object> taskInfo = new LinkedHashMap<>();
            taskInfo.put("id", qb.getId());
            taskInfo.put("taskName", qb.getName());
            taskInfo.put("taskType", 1);
            taskInfo.put("tagId", qb.getTagId());
            taskInfo.put("dimension", qb.getDimension());

            boolean isCompleted = completedAnswerRecords.stream().anyMatch(record -> record.getBankId().equals(qb.getId()));
            taskInfo.put("status", isCompleted ? "已完成" : "待完成");

            if (isCompleted) {
                AnswerRecord record = completedAnswerRecords.stream()
                        .filter(r -> r.getBankId().equals(qb.getId()))
                        .findFirst()
                        .orElse(null);
                if (record != null && record.getTotalScore() != null) {
                    taskInfo.put("score", record.getTotalScore());
                }
            }

            questionBankListResult.add(taskInfo);
        });

        List<LinkedHashMap<String, Object>> assessmentTaskListFix = new java.util.ArrayList<>();

        matchedTasks.forEach(task -> {
            LinkedHashMap<String, Object> taskInfo = new LinkedHashMap<>();
            taskInfo.put("id", task.getId());
            taskInfo.put("taskName", task.getTaskName());
            taskInfo.put("taskType", task.getTaskType());
            taskInfo.put("tagId", task.getTagId());
            taskInfo.put("taskDimension", task.getTaskDimension());

            AssessmentSubmission submission = completedSubmissions.stream()
                    .filter(s -> s.getTaskId().equals(task.getId()))
                    .findFirst()
                    .orElse(null);

            if (submission != null && submission.getSubmissionStatus() != null && submission.getSubmissionStatus() >= 1) {
                taskInfo.put("status", "已完成");
                if (submission.getFinalScore() != null) {
                    taskInfo.put("score", submission.getFinalScore());
                }
            } else {
                taskInfo.put("status", "待完成");
            }

            assessmentTaskListFix.add(taskInfo);
        });

        result.put("questionBanks", questionBankListResult);
        result.put("assessmentTasks", assessmentTaskListFix);

        return result;
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

        dimensionScores.put("专业技能", calculateDimensionScoreByDimension(assessmentSubmissionList, answerRecordList, discussionScoreList, "专业技能"));
        dimensionScores.put("沟通能力", calculateDimensionScoreByDimension(assessmentSubmissionList, answerRecordList, discussionScoreList, "沟通能力"));
        dimensionScores.put("团队协作", calculateDimensionScoreByDimension(assessmentSubmissionList, answerRecordList, discussionScoreList, "团队协作"));
        dimensionScores.put("应急处理", calculateDimensionScoreByDimension(assessmentSubmissionList, answerRecordList, discussionScoreList, "应急处理"));
        dimensionScores.put("学习能力", calculateDimensionScoreByDimension(assessmentSubmissionList, answerRecordList, discussionScoreList, "学习能力"));

        return dimensionScores;
    }

    private BigDecimal calculateDimensionScoreByDimension(List<AssessmentSubmission> assessmentSubmissionList,
                                                          List<AnswerRecord> answerRecordList,
                                                          List<DiscussionScore> discussionScoreList,
                                                          String dimension) {
        List<BigDecimal> allScores = new java.util.ArrayList<>();

        if (discussionScoreList != null && !discussionScoreList.isEmpty()) {
            List<DiscussionScore> filteredDiscussionScores = discussionScoreList.stream()
                    .filter(score -> {
                        BigDecimal scoreValue = getDiscussionScoreByDimension(score, dimension);
                        return scoreValue != null;
                    })
                    .collect(Collectors.toList());

            if (!filteredDiscussionScores.isEmpty()) {
                BigDecimal totalDiscussionScore = filteredDiscussionScores.stream()
                        .map(score -> getDiscussionScoreByDimension(score, dimension))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal avgDiscussionScore = totalDiscussionScore.divide(new BigDecimal(filteredDiscussionScores.size()), 2, RoundingMode.HALF_UP);
                allScores.add(avgDiscussionScore);
            }
        }

        if (answerRecordList != null && !answerRecordList.isEmpty()) {
            List<AnswerRecord> filteredAnswerRecords = answerRecordList.stream()
                    .filter(record -> dimension.equals(record.getDimension()))
                    .collect(Collectors.toList());

            if (!filteredAnswerRecords.isEmpty()) {
                BigDecimal totalAnswerScore = filteredAnswerRecords.stream()
                        .filter(record -> record.getTotalScore() != null)
                        .map(record -> new BigDecimal(record.getTotalScore()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal avgAnswerScore = totalAnswerScore.divide(new BigDecimal(filteredAnswerRecords.size()), 2, RoundingMode.HALF_UP);
                allScores.add(avgAnswerScore);
            }
        }

        if (assessmentSubmissionList != null && !assessmentSubmissionList.isEmpty()) {
            List<Integer> taskIdsForDimension = assessmentTaskMapper.selectList(Wrappers.<AssessmentTask>lambdaQuery()
                            .eq(AssessmentTask::getTaskDimension, dimension))
                    .stream()
                    .map(AssessmentTask::getId)
                    .collect(Collectors.toList());

            if (!taskIdsForDimension.isEmpty()) {
                List<AssessmentSubmission> filteredSubmissions = assessmentSubmissionList.stream()
                        .filter(submission -> submission.getFinalScore() != null
                                && taskIdsForDimension.contains(submission.getTaskId()))
                        .collect(Collectors.toList());

                if (!filteredSubmissions.isEmpty()) {
                    BigDecimal totalAssessmentScore = filteredSubmissions.stream()
                            .map(AssessmentSubmission::getFinalScore)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal avgAssessmentScore = totalAssessmentScore.divide(new BigDecimal(filteredSubmissions.size()), 2, RoundingMode.HALF_UP);
                    allScores.add(avgAssessmentScore);
                }
            }
        }

        if (allScores.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalScore = allScores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalScore.divide(new BigDecimal(allScores.size()), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getDiscussionScoreByDimension(DiscussionScore score, String dimension) {
        switch (dimension) {
            case "沟通能力":
                return score.getCommunicationScore();
            case "团队协作":
                return score.getTeamworkScore();
            case "应急处理":
                return score.getEmergencyScore();
            case "学习能力":
                return score.getLearningScore();
            case "专业技能":
                return score.getProfessionalScore();
            default:
                return null;
        }
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
                .filter(record -> record.getTotalScore() != null)
                .map(record -> new BigDecimal(record.getTotalScore()))
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

            UserAbilityScore newScore = new UserAbilityScore();
            newScore.setUserId(userId);
            newScore.setDimension(dimension);
            newScore.setRawScore(score);
            newScore.setFinalScore(score);
            newScore.setCreateDate(DateUtil.formatDateTime(new Date()));
            userAbilityScoreMapper.insert(newScore);
        }

        updateStaffInfoScores(userId, dimensionScores);
    }

    private void updateStaffInfoScores(Integer userId, Map<String, BigDecimal> dimensionScores) {
        StaffInfo staffInfo = baseMapper.selectOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, userId));
        if (staffInfo == null) {
            return;
        }

        boolean needUpdate = false;

        if (dimensionScores.containsKey("专业技能")) {
            staffInfo.setProfessionalScore(dimensionScores.get("专业技能"));
            needUpdate = true;
        }
        if (dimensionScores.containsKey("沟通能力")) {
            staffInfo.setCommunicationScore(dimensionScores.get("沟通能力"));
            needUpdate = true;
        }
        if (dimensionScores.containsKey("团队协作")) {
            staffInfo.setTeamworkScore(dimensionScores.get("团队协作"));
            needUpdate = true;
        }
        if (dimensionScores.containsKey("应急处理")) {
            staffInfo.setEmergencyScore(dimensionScores.get("应急处理"));
            needUpdate = true;
        }
        if (dimensionScores.containsKey("学习能力")) {
            staffInfo.setLearningScore(dimensionScores.get("学习能力"));
            needUpdate = true;
        }

        if (needUpdate) {
            baseMapper.updateById(staffInfo);
        }
    }
}
