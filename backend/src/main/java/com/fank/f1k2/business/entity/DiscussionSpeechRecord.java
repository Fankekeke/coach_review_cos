package com.fank.f1k2.business.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 讨论发言记录表
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DiscussionSpeechRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId(type = IdType.AUTO)
    private Integer id;


    private Integer roomId;

    private Integer userId;

    /**
     * 发言内容
     */
    private String speechContent;

    private String speechTime;

    /**
     * 观点是否被采纳
     */
    private Integer adoptedFlag;

    /**
     * 是否参与冲突调解
     */
    private Integer conflictResolutionFlag;

    private String createTime;


}
