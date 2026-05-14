package com.fank.f1k2.business.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 评估任务提交表
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AssessmentSubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId(type = IdType.AUTO)
    private Integer id;


    private Long taskId;

    private Long userId;

    /**
     * 状态 0未提交 1待评 2已评
     */
    private Integer submissionStatus;

    /**
     * 答题内容
     */
    private String answerContent;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 文档地址
     */
    private String documentUrl;

    /**
     * 是否草稿
     */
    private Integer draftFlag;

    /**
     * 自动评分
     */
    private BigDecimal autoScore;

    /**
     * 人工评分
     */
    private BigDecimal manualScore;

    /**
     * 最终得分
     */
    private BigDecimal finalScore;

    /**
     * 评语
     */
    private String teacherComment;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 评分管理员
     */
    private Long reviewedBy;

    /**
     * 评分时间
     */
    private LocalDateTime reviewedTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
