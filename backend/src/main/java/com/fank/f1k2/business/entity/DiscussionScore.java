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
 * 讨论评分表
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DiscussionScore implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId(type = IdType.AUTO)
    private Integer id;


    private Long roomId;

    private Long userId;

    /**
     * 发言次数评分
     */
    private BigDecimal speechCountScore;

    /**
     * 观点采纳评分
     */
    private BigDecimal adoptedScore;

    /**
     * 团队协作评分
     */
    private BigDecimal teamworkScore;

    /**
     * 沟通能力评分
     */
    private BigDecimal communicationScore;

    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 评语
     */
    private String comment;

    /**
     * 评分管理员
     */
    private Long scoredBy;

    private LocalDateTime scoredTime;


}
