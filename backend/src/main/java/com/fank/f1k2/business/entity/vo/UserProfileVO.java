package com.fank.f1k2.business.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户画像统计 VO
 *
 * @author FanK fan1ke2ke@gmail.com
 */
@Data
@Accessors(chain = true)
public class UserProfileVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户群体分析（按年龄段）
     */
    private List<AgeGroupAnalysis> ageGroupAnalysis;

    /**
     * 部门/支部维度分析
     */
    private List<DepartmentAnalysis> departmentAnalysis;

    /**
     * 关注重点差异对比
     */
    private FocusComparison focusComparison;

    /**
     * 群体特征标签
     */
    private List<GroupFeatureTag> groupFeatureTags;

    /**
     * 年龄段分析
     */
    @Data
    @Accessors(chain = true)
    public static class AgeGroupAnalysis implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 年龄段名称（如 "35 岁以下"、"35-50 岁"、"50 岁以上"）
         */
        private String ageGroupName;

        /**
         * 该群体总人数
         */
        private Integer totalUsers;

        /**
         * 该群体累计提问数
         */
        private Integer totalQuestions;

        /**
         * 人均提问数
         */
        private Double avgQuestionsPerUser;

        /**
         * 平均积分
         */
        private BigDecimal avgIntegral;

        /**
         * 活跃率（有提问记录的用户占比）
         */
        private Double activeRate;

        /**
         * 主要关注领域 TOP3
         */
        private List<String> topFocusAreas;

        /**
         * 活跃时段偏好
         */
        private String preferredTimeSlot;
    }

    /**
     * 部门/支部维度分析
     */
    @Data
    @Accessors(chain = true)
    public static class DepartmentAnalysis implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 部门/支部 ID
         */
        private Integer departmentId;

        /**
         * 部门/支部名称
         */
        private String departmentName;

        /**
         * 部门类型（DEPARTMENT-部门，BRANCH-支部）
         */
        private String departmentType;

        /**
         * 部门人数
         */
        private Integer memberCount;

        /**
         * 本月提问数
         */
        private Integer monthlyQuestions;

        /**
         * 本月回答数
         */
        private Integer monthlyAnswers;

        /**
         * 问题解决率
         */
        private Double resolutionRate;

        /**
         * 平均响应时长（小时）
         */
        private Double avgResponseTime;

        /**
         * 部门活跃度排名
         */
        private Integer activityRank;
    }

    /**
     * 关注重点差异对比
     */
    @Data
    @Accessors(chain = true)
    public static class FocusComparison implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 年轻教练（35 岁以下）关注 TOP5
         */
        private List<FocusTopic> youngGroupFocus;

        /**
         * 中年教练（35-50 岁）关注 TOP5
         */
        private List<FocusTopic> middleGroupFocus;

        /**
         * 资深教练（50 岁以上）关注 TOP5
         */
        private List<FocusTopic> seniorGroupFocus;
    }

    /**
     * 关注话题
     */
    @Data
    @Accessors(chain = true)
    public static class FocusTopic implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 话题关键词
         */
        private String keyword;

        /**
         * 提及次数
         */
        private Integer mentionCount;

        /**
         * 占该群体总提问的比例
         */
        private Double percentage;

        /**
         * 热度趋势（+1 上升，-1 下降，0 持平）
         */
        private Integer trend;
    }

    /**
     * 群体特征标签
     */
    @Data
    @Accessors(chain = true)
    public static class GroupFeatureTag implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 群体名称
         */
        private String groupName;

        /**
         * 特征标签列表
         */
        private List<String> featureTags;

        /**
         * 群体描述
         */
        private String groupDescription;
    }
}