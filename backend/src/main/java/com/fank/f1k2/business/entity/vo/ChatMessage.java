package com.fank.f1k2.business.entity.vo;

import lombok.Data;
import java.io.Serializable;

@Data
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roomId;

    private Integer userId;

    private String userName;

    private String content;

    private String messageType;

    private String timestamp;

    private Integer toUserId;
}