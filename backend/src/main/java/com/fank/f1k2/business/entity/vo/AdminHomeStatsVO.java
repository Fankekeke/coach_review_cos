package com.fank.f1k2.business.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 后台管理首页统计 VO
 *
 * @author FanK fan1ke2ke@gmail.com
 */
@Data
@Accessors(chain = true)
public class AdminHomeStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 今日问答次数
     */
    private Integer todayQuestionCount;

    /**
     * 今日用户数
     */
    private Integer todayUserCount;

    /**
     * 解决率（智能回答准确度）
     */
    private Double resolutionRate;

    /**
     * 待人工确认的问答数量（低置信度）
     */
    private Integer pendingConfirmCount;

    /**
     * 用户反馈差评 Top 5
     */
    private List<FeedbackItem> topNegativeFeedbacks;

    /**
     * 最近 7 天提问量趋势
     */
    private List<TrendData> last7DaysTrend;

    /**
     * 反馈项
     */
    @Data
    @Accessors(chain = true)
    public static class FeedbackItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 问题 ID
         */
        private Integer questionId;

        /**
         * 问题标题
         */
        private String title;

        /**
         * 反馈内容
         */
        private String feedback;

        /**
         * 反馈时间
         */
        private String feedbackDate;
    }

    /**
     * 趋势数据
     */
    @Data
    @Accessors(chain = true)
    public static class TrendData implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 日期
         */
        private String date;

        /**
         * 提问数量
         */
        private Integer questionCount;

        /**
         * 回答数量
         */
        private Integer answerCount;
    }
}