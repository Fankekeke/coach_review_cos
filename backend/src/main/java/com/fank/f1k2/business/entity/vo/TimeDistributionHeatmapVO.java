package com.fank.f1k2.business.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 时间分布热力图统计 VO
 *
 * @author FanK fan1ke2ke@gmail.com
 */
@Data
@Accessors(chain = true)
public class TimeDistributionHeatmapVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 一周 7 天的时段分布数据
     */
    private List<DayTimeSlot> weekData;

    /**
     * 最佳推送时间建议
     */
    private BestTimeSuggestion bestTimeSuggestion;

    /**
     * 时段汇总统计
     */
    private TimeSlotSummary timeSlotSummary;

    /**
     * 单日时段分布（星期几 x 小时）
     */
    @Data
    @Accessors(chain = true)
    public static class DayTimeSlot implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 星期几（0-6，0 代表周日）
         */
        private Integer dayOfWeek;

        /**
         * 星期名称
         */
        private String dayName;

        /**
         * 24 小时提问数量（索引 0-23 对应 0:00-23:00）
         */
        private List<Integer> hourlyCounts;

        /**
         * 当天总提问数
         */
        private Integer totalCount;

        /**
         * 高峰期时段（如 "19:00-22:00"）
         */
        private String peakHours;
    }

    /**
     * 最佳推送时间建议
     */
    @Data
    @Accessors(chain = true)
    public static class BestTimeSuggestion implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 最佳推送日期（如 "周一"）
         */
        private String bestDay;

        /**
         * 最佳推送时段（如 "晚间"）
         */
        private String bestTimeSlot;

        /**
         * 具体时间段（如 "19:00-22:00"）
         */
        private String specificTimeRange;

        /**
         * 该时段平均提问数
         */
        private Integer avgQuestions;

        /**
         * 推荐指数（1-5 星）
         */
        private Integer recommendationLevel;
    }

    /**
     * 时段汇总统计
     */
    @Data
    @Accessors(chain = true)
    public static class TimeSlotSummary implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 早晨（6:00-9:00）提问数
         */
        private Integer morningCount;

        /**
         * 上午（9:00-12:00）提问数
         */
        private Integer forenoonCount;

        /**
         * 中午（12:00-14:00）提问数
         */
        private Integer noonCount;

        /**
         * 下午（14:00-18:00）提问数
         */
        private Integer afternoonCount;

        /**
         * 傍晚（18:00-20:00）提问数
         */
        private Integer eveningCount;

        /**
         * 晚间（20:00-23:00）提问数
         */
        private Integer nightCount;

        /**
         * 深夜（23:00-6:00）提问数
         */
        private Integer lateNightCount;

        /**
         * 最活跃时段名称
         */
        private String mostActiveTimeSlot;
    }
}