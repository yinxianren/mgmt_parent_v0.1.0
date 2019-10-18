package com.rxh.pojo.merchant;

import java.io.Serializable;

public class MerchantQuestionWithBLOBs extends MerchantQuestion implements Serializable {
    public final static short PROCESSED = 1;
    public final static short BEPROCESSED = 2;
    public final static short CLOSED = 3;
    /**
     *  商户咨询的问题
     */
    private String content;

    // 回复
    private String answer;

    /**
     * 商户咨询的问题
     * @return content 商户咨询的问题
    */
    public String getContent() {
        return content;
    }

    /**
     * 商户咨询的问题
     * @param content 商户咨询的问题
    */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 回复
     * @return answer 回复
    */
    public String getAnswer() {
        return answer;
    }

    /**
     * 回复
     * @param answer 回复
    */
    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }
}