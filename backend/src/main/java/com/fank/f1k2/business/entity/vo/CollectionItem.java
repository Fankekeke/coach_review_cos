package com.fank.f1k2.business.entity.vo;

import lombok.Data;

@Data
public class CollectionItem {

    /**
     * 题目
     */
    private String collection;

    /**
     * 选项
     */
    private String collectionOptions;

    /**
     * 给出的答案
     */
    private String answer;

    /**
     * 正确答案
     */
    private String trueAnswer;

    /**
     * 结果 0.错误 1.正确
     */
    private String result;
}
