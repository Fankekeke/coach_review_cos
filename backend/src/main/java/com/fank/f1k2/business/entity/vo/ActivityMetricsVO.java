package com.fank.f1k2.business.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 活跃度指标统计 VO
 *
 * @author FanK fan1ke2ke@gmail.com
 */
@Data
@Accessors(chain = true)
public class ActivityMetricsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日活跃用户数（DAU）
     */
    private Integer dau;

    /**
     * 周活跃用户数（WAU）
     */
    private Integer wau;

    /**
     * 月活跃用户数（MAU）
     */
    private Integer mau;

    /**
     * 人均提问次数
     */
    private AvgQuestionsPerUser avgQuestionsPerUser;

    /**
     * 单次会话时长（分钟）
     */
    private AvgSessionDuration avgSessionDuration;

    /**
     * 活跃度趋势（最近 7 天）
     */
    private ActivityTrend activityTrend;

    /**
     * 人均提问次数（分时段）
     */
    @Data
    @Accessors(chain = true)
    public static class AvgQuestionsPerUser implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 日均值
         */
        private Double dailyAvg;

        /**
         * 周均值
         */
        private Double weeklyAvg;

        /**
         * 月均值
         */
        private Double monthlyAvg;
    }

    /**
     * 单次会话时长
     */
    @Data
    @Accessors(chain = true)
    public static class AvgSessionDuration implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 日均会话时长（分钟）
         */
        private Double dailyAvgMinutes;

        /**
         * 周均会话时长（分钟）
         */
        private Double weeklyAvgMinutes;

        /**
         * 月均会话时长（分钟）
         */
        private Double monthlyAvgMinutes;
    }

    /**
     * 活跃度趋势
     */
    @Data
    @Accessors(chain = true)
    public static class ActivityTrend implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 日期
         */
        private String date;

        /**
         * 活跃用户数
         */
        private Integer activeUsers;

        /**
         * 提问次数
         */
        private Integer questionCount;

        /**
         * 会话时长（分钟）
         */
        private Integer totalDuration;
    }
}