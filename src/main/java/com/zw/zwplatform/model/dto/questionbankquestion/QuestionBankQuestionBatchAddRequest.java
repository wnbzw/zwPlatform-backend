package com.zw.zwplatform.model.dto.questionbankquestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建批量题库题目请求
 */
@Data
public class QuestionBankQuestionBatchAddRequest implements Serializable {

    /**
     * 题库id
     */
    private Long questionBankId;

    /**
     * 题目id
     */
    private List<Long> questionIdList;

    private static final long serialVersionUID = 1L;
}