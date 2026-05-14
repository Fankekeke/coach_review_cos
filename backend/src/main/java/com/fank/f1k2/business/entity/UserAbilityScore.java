package com.fank.f1k2.business.entity;

import java.math.BigDecimal;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 用户能力维度得分表
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserAbilityScore implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId(type = IdType.AUTO)
    private Integer id;


    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 所属维度
     */
    private String dimension;

    /**
     * 原始分数
     */
    private BigDecimal rawScore;

    /**
     * 最终分数
     */
    private BigDecimal finalScore;


}
