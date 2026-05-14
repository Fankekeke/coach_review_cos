package com.fank.f1k2.business.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 高频词云/话题排行统计 VO
 *
 * @author FanK fan1ke2ke@gmail.com
 */
@Data
@Accessors(chain = true)
public class WordCloudVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 词云数据（高频词汇）
     */
    private List<WordCloudItem> wordCloud;

    /**
     * 话题排行榜 TOP 10
     */
    private List<TopicRankItem> topicRanking;

    /**
     * 分类统计（理论知识、政策热点、工作实务）
     */
    private CategoryAnalysis categoryAnalysis;

    /**
     * 新兴话题趋势
     */
    private List<EmergingTopic> emergingTopics;

    /**
     * 词云项
     */
    @Data
    @Accessors(chain = true)
    public static class WordCloudItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 词语
         */
        private String word;

        /**
         * 出现频次
         */
        private Integer frequency;

        /**
         * 权重值（用于词云大小展示）
         */
        private Double weight;

        /**
         * 所属类别
         */
        private String category;
    }

    /**
     * 话题排行项
     */
    @Data
    @Accessors(chain = true)
    public static class TopicRankItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 排名
         */
        private Integer rank;

        /**
         * 话题关键词
         */
        private String keyword;

        /**
         * 提及次数
         */
        private Integer mentionCount;

        /**
         * 参与人数
         */
        private Integer participantCount;

        /**
         * 热度指数（综合计算）
         */
        private Integer hotScore;

        /**
         * 热度趋势（+1 上升，-1 下降，0 持平）
         */
        private Integer trend;

        /**
         * 相关话题示例
         */
        private List<String> relatedQuestions;
    }

    /**
     * 分类统计
     */
    @Data
    @Accessors(chain = true)
    public static class CategoryAnalysis implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 理论知识类 TOP5
         */
        private List<CategoryItem> theoryTopics;

        /**
         * 政策热点类 TOP5
         */
        private List<CategoryItem> policyTopics;

        /**
         * 工作实务类 TOP5
         */
        private List<CategoryItem> practiceTopics;
    }

    /**
     * 分类项
     */
    @Data
    @Accessors(chain = true)
    public static class CategoryItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 话题名称
         */
        private String topic;

        /**
         * 提及次数
         */
        private Integer count;

        /**
         * 占比百分比
         */
        private Double percentage;
    }

    /**
     * 新兴话题
     */
    @Data
    @Accessors(chain = true)
    public static class EmergingTopic implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 话题关键词
         */
        private String keyword;

        /**
         * 近 7 天增长次数
         */
        private Integer growthCount;

        /**
         * 增长率（%）
         */
        private Double growthRate;

        /**
         * 潜力指数（1-10）
         */
        private Integer potentialIndex;
    }
}