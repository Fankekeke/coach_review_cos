package com.fank.f1k2.business.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 讨论房间表
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DiscussionRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId(type = IdType.AUTO)
    private Integer id;


    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 讨论主题
     */
    private String topic;

    /**
     * 状态 0未开始 1进行中 2已结束
     */
    private Integer roomStatus;

    /**
     * 最大人数
     */
    private Integer maxMemberCount;

    /**
     * 讨论时长(分钟)
     */
    private Integer durationMinutes;

    /**
     * 当前人数
     */
    private Integer currentMemberCount;

    /**
     * 是否自动开始
     */
    private Integer autoStart;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建人
     */
    private Integer createdBy;

    private String createTime;

    private String updateTime;
    private String images;


}
