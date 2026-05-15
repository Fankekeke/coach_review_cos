package com.fank.f1k2.business.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 自我评估任务表
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AssessmentTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId(type = IdType.AUTO)
    private Integer id;


    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型 1视频 2文档
     */
    private Integer taskType;

    /**
     * 任务维度
     */
    private String taskDimension;

    /**
     * 任务描述
     */
    private String taskDescription;

    /**
     * 评分标准
     */
    private String scoringStandard;

    /**
     * 参考材料地址
     */
    private String referenceMaterial;

    /**
     * 截止时间
     */
    private String deadline;

    /**
     * 状态 0关闭 1进行中
     */
    private Integer status;

    private Integer createdBy;

    private String createTime;

    private String updateTime;

    private Integer tagId;


}
