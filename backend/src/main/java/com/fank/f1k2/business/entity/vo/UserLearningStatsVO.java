package com.fank.f1k2.business.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用户个人学习档案统计 VO
 *
 * @author FanK fan1ke2ke@gmail.com
 */
@Data
@Accessors(chain = true)
public class UserLearningStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户基本信息
     */
    private UserInfoBasic userBasic;

    /**
     * 学习里程碑
     */
    private LearningMilestone milestone;

    /**
     * 今日推荐（党务规定）
     */
    private List<RecommendationItem> todayRecommendations;

    /**
     * 支部热榜 TOP 3
     */
    private List<HotTopicItem> branchHotTopics;

    /**
     * 用户基本信息
     */
    @Data
    @Accessors(chain = true)
    public static class UserInfoBasic implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 用户 ID
         */
        private Integer userId;

        /**
         * 用户姓名
         */
        private String userName;

        /**
         * 用户头像
         */
        private String avatar;

        /**
         * 所属组织
         */
        private String organization;
    }

    /**
     * 学习里程碑（游戏化成就感）
     */
    @Data
    @Accessors(chain = true)
    public static class LearningMilestone implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 累计提问次数
         */
        private Integer totalQuestions;

        /**
         * 累计学习时长（分钟）
         */
        private Integer totalLearningMinutes;

        /**
         * 当前积分
         */
        private BigDecimal currentIntegral;

        /**
         * 积分等级
         */
        private String integralLevel;

        /**
         * 已采纳问题数
         */
        private Integer adoptedQuestions;

        /**
         * 等级进度（下一级所需积分）
         */
        private Integer levelProgress;
    }

    /**
     * 推荐项
     */
    @Data
    @Accessors(chain = true)
    public static class RecommendationItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 推荐 ID（公告 ID 或问题 ID）
         */
        private Integer id;

        /**
         * 推荐标题
         */
        private String title;

        /**
         * 推荐内容摘要
         */
        private String summary;

        /**
         * 推荐类型（BULLETIN-公告，QUESTION-问题）
         */
        private String type;

        /**
         * 发布时间
         */
        private String publishDate;

        /**
         * 匹配度标签（如：与你相关的"组织建设"）
         */
        private String matchTag;
    }

    /**
     * 热榜项
     */
    @Data
    @Accessors(chain = true)
    public static class HotTopicItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 话题 ID
         */
        private Integer topicId;

        /**
         * 话题标题
         */
        private String title;

        /**
         * 关注/提问次数
         */
        private Integer viewCount;

        /**
         * 热度指数
         */
        private Integer hotScore;

        /**
         * 排名变化（+1 上升，-1 下降，0 持平）
         */
        private Integer rankChange;
    }
}