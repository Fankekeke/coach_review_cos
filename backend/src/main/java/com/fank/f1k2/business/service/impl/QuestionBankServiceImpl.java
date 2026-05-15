package com.fank.f1k2.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.dao.*;
import com.fank.f1k2.business.entity.*;
import com.fank.f1k2.business.entity.vo.CollectionItem;
import com.fank.f1k2.business.service.ICollectionInfoService;
import com.fank.f1k2.business.service.ICollectionOptionsService;
import com.fank.f1k2.business.service.IQuestionBankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fank.f1k2.common.exception.F1k2Exception;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank> implements IQuestionBankService {

    @Resource
    private ICollectionInfoService collectionInfoService;

    @Resource
    private ICollectionOptionsService collectionOptionsService;

    @Resource
    private StaffInfoMapper staffInfoMapper;

    @Resource
    private AssessmentTaskMapper assessmentTaskMapper;

    @Resource
    private AssessmentSubmissionMapper assessmentSubmissionMapper;

    @Resource
    private AnswerRecordMapper answerRecordMapper;

    /**
     * 分页获取题库信息
     *
     * @param page      分页对象
     * @param queryFrom 题库信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<QuestionBank> page, QuestionBank queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }

    /**
     * 新增题库信息
     *
     * @param questionBank 题库信息对象
     * @return 添加结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addQuestionBank(QuestionBank questionBank) throws F1k2Exception {
        if (StrUtil.isEmpty(questionBank.getCollectionItemList())) {
            throw new F1k2Exception("请添加题目选项");
        }
        this.save(questionBank);

        List<CollectionInfo> toAddCollectionList = new ArrayList<>();
        List<CollectionOptions> toAddCollectionOptionList = new ArrayList<>();

        List<CollectionItem> collectionItemList = JSONUtil.toList(questionBank.getCollectionItemList(), CollectionItem.class);
        for (CollectionItem collectionItem : collectionItemList) {
            CollectionInfo collectionInfo = JSONUtil.toBean(collectionItem.getCollection(), CollectionInfo.class);
            collectionInfo.setCollectionId(questionBank.getId());
            collectionInfo.setCreationTime(DateUtil.formatDateTime(new Date()));
            collectionInfo.setEditTime(DateUtil.formatDateTime(new Date()));
            collectionInfo.setCode(UUID.randomUUID().toString());

            toAddCollectionList.add(collectionInfo);

            List<CollectionOptions> collectionOptionsList = JSONUtil.toList(collectionItem.getCollectionOptions(), CollectionOptions.class);
            for (CollectionOptions options : collectionOptionsList) {
                options.setQuestionCode(collectionInfo.getCode());
                toAddCollectionOptionList.add(options);
            }
        }

        if (!toAddCollectionList.isEmpty()) {
            collectionInfoService.saveBatch(toAddCollectionList);
        }

        if (!toAddCollectionOptionList.isEmpty()) {
            collectionOptionsService.saveBatch(toAddCollectionOptionList);
        }
        return true;
    }

    /**
     * 修改题库信息
     *
     * @param questionBank 题库信息对象
     * @return 修改结果
     */
    @Override
    public boolean editQuestionBank(QuestionBank questionBank) throws F1k2Exception {
        if (StrUtil.isEmpty(questionBank.getCollectionItemList())) {
            throw new F1k2Exception("请添加题目选项");
        }
        this.updateById(questionBank);

        List<CollectionInfo> collectionInfoList = collectionInfoService.list(Wrappers.<CollectionInfo>lambdaQuery().eq(CollectionInfo::getCollectionId, questionBank.getId()));
        if (CollectionUtil.isNotEmpty(collectionInfoList)) {
            List<String> collectionCodeList = collectionInfoList.stream().map(CollectionInfo::getCode).collect(Collectors.toList());
            collectionInfoService.remove(Wrappers.<CollectionInfo>lambdaQuery().eq(CollectionInfo::getCollectionId, questionBank.getId()));
            collectionOptionsService.remove(Wrappers.<CollectionOptions>lambdaQuery().in(CollectionOptions::getQuestionCode, collectionCodeList));
        }

        List<CollectionInfo> toAddCollectionList = new ArrayList<>();
        List<CollectionOptions> toAddCollectionOptionList = new ArrayList<>();

        List<CollectionItem> collectionItemList = JSONUtil.toList(questionBank.getCollectionItemList(), CollectionItem.class);
        for (CollectionItem collectionItem : collectionItemList) {
            CollectionInfo collectionInfo = JSONUtil.toBean(collectionItem.getCollection(), CollectionInfo.class);
            collectionInfo.setCollectionId(questionBank.getId());
            collectionInfo.setCreationTime(DateUtil.formatDateTime(new Date()));
            collectionInfo.setEditTime(DateUtil.formatDateTime(new Date()));
            collectionInfo.setCode(UUID.randomUUID().toString());

            toAddCollectionList.add(collectionInfo);

            List<CollectionOptions> collectionOptionsList = JSONUtil.toList(collectionItem.getCollectionOptions(), CollectionOptions.class);
            for (CollectionOptions options : collectionOptionsList) {
                options.setQuestionCode(collectionInfo.getCode());
                toAddCollectionOptionList.add(options);
            }
        }

        if (!toAddCollectionList.isEmpty()) {
            collectionInfoService.saveBatch(toAddCollectionList);
        }

        if (!toAddCollectionOptionList.isEmpty()) {
            collectionOptionsService.saveBatch(toAddCollectionOptionList);
        }
        return true;
    }

    /**
     * 查询题库信息列表
     *
     * @return 列表
     */
    @Override
    public List<LinkedHashMap<String, Object>> queryQuestionBankList() {
        List<QuestionBank> questionBankList = this.list();
        if (CollectionUtil.isEmpty(questionBankList)) {
            return Collections.emptyList();
        }

        List<CollectionInfo> collectionInfoList = collectionInfoService.list();
        Map<Integer, List<CollectionInfo>> collectionInfoMap = collectionInfoList.stream()
                .collect(Collectors.groupingBy(CollectionInfo::getCollectionId));

        List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
        for (QuestionBank questionBank : questionBankList) {
            LinkedHashMap<String, Object> result = new LinkedHashMap<>();
            result.put("id", questionBank.getId());
            result.put("name", questionBank.getName());
            result.put("code", questionBank.getCode());
            result.put("image", questionBank.getImage());

            List<CollectionInfo> currentCollectionInfoList = collectionInfoMap.get(questionBank.getId());
            if (CollectionUtil.isNotEmpty(currentCollectionInfoList)) {
                Map<String, Long> typeCountMap = currentCollectionInfoList.stream()
                        .collect(Collectors.groupingBy(CollectionInfo::getQuestionType, Collectors.counting()));

                LinkedHashMap<String, Object> statistics = new LinkedHashMap<>();
                statistics.put("total", currentCollectionInfoList.size());
                statistics.put("typeCount", typeCountMap);

                result.put("questionCount", statistics);
            } else {
                LinkedHashMap<String, Object> statistics = new LinkedHashMap<>();
                statistics.put("total", 0);
                statistics.put("typeCount", Collections.emptyMap());
                result.put("questionCount", statistics);
            }
            resultList.add(result);
        }
        return resultList;
    }

    /**
     * 查询题库信息详情
     *
     * @param bandId 主键ID
     * @return 详情
     */
    @Override
    public LinkedHashMap<String, Object> queryBankDetail(Integer bandId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("bank", null);
                put("collection", Collections.emptyList());
            }
        };
        QuestionBank questionBank = this.getById(bandId);
        if (questionBank != null) {
            result.put("bank", questionBank);
        } else {
            return result;
        }

        // 获取题目信息
        List<CollectionInfo> collectionInfoList = collectionInfoService.list(Wrappers.<CollectionInfo>lambdaQuery().eq(CollectionInfo::getCollectionId, bandId));
        // 获取题目选项信息
        List<CollectionOptions> collectionOptionsList = collectionOptionsService.list(Wrappers.<CollectionOptions>lambdaQuery().in(CollectionOptions::getQuestionCode, collectionInfoList.stream().map(CollectionInfo::getCode).collect(Collectors.toList())));
        Map<String, List<CollectionOptions>> collectionOptionsMap = collectionOptionsList.stream().collect(Collectors.groupingBy(CollectionOptions::getQuestionCode));
        for (CollectionInfo collectionInfo : collectionInfoList) {
            List<CollectionOptions> optionsList = collectionOptionsMap.get(collectionInfo.getCode());
            collectionInfo.setCollectionOptionsList(optionsList);
        }
        result.put("collection", collectionInfoList);
        return result;
    }

    /**
     * 查询用户考核信息
     *
     * @param userId 用户ID
     * @return 详情
     */
    @Override
    public LinkedHashMap<String, Object> queryAssessmentByUser(Integer userId) {
        StaffInfo staffInfo = staffInfoMapper.selectOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, userId));
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("staff", staffInfo);
            }
        };
        // 获取题目、文档、视频审核内容
        List<QuestionBank> questionBankList = this.list(Wrappers.<QuestionBank>lambdaQuery().eq(staffInfo.getTagId() != null, QuestionBank::getTagId, staffInfo.getTagId()).or().isNull(QuestionBank::getTagId));
        // 答题记录
        List<AnswerRecord> answerRecordList = answerRecordMapper.selectList(Wrappers.<AnswerRecord>lambdaQuery().eq(AnswerRecord::getUserId, staffInfo.getId()));

        List<LinkedHashMap<String, Object>> answerRecordMap = new ArrayList<>();
        for (QuestionBank bank : questionBankList) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("bank", bank);
            map.put("score", answerRecordList.stream().filter(item -> item.getBankId().equals(bank.getId())));
        }
        result.put("answerRecord", answerRecordMap);

        // 获取考核任务信息
        List<AssessmentTask> assessmentTaskList = assessmentTaskMapper.selectList(Wrappers.<AssessmentTask>lambdaQuery().eq(staffInfo.getTagId() != null, AssessmentTask::getTagId, staffInfo.getTagId()).or().isNull(AssessmentTask::getTagId));
        // 考核记录
        List<AssessmentSubmission> assessmentSubmissionList = assessmentSubmissionMapper.selectList(Wrappers.<AssessmentSubmission>lambdaQuery().eq(AssessmentSubmission::getUserId, staffInfo.getId()));
        List<LinkedHashMap<String, Object>> taskRecordMap = new ArrayList<>();
        for (AssessmentTask assessmentTask : assessmentTaskList) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("bank", assessmentTask);
            map.put("score", assessmentSubmissionList.stream().filter(item -> item.getTaskId().equals(assessmentTask.getId())));
        }
        result.put("taskRecord", taskRecordMap);
        return result;
    }
}
