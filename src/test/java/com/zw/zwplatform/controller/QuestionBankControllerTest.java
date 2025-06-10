package com.zw.zwplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zw.zwplatform.common.BaseResponse;
import com.zw.zwplatform.model.vo.QuestionBankVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import com.zw.zwplatform.model.dto.questionbank.QuestionBankQueryRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionBankControllerTest {

    @Resource
    private QuestionBankController questionBankController;
    @Test
    void addQuestionBank() {
    }


        @Test
        void listMyQuestionBankVOByPage() {
            // 构造请求参数
            QuestionBankQueryRequest queryRequest = new QuestionBankQueryRequest();
            queryRequest.setCurrent(1);
            queryRequest.setPageSize(10);

            // 模拟 HttpServletRequest（如果 controller 中实际使用到了 request）
            HttpServletRequest request = new MockHttpServletRequest();

            // 调用方法
            BaseResponse<Page<QuestionBankVO>> pageBaseResponse = questionBankController.listQuestionBankVOByPage(queryRequest, request);

            // 获取分页数据
            Page<QuestionBankVO> result = pageBaseResponse.getData();
            // 执行断言
            assertNotNull(result, "分页结果不应为 null");
            assertTrue(result.getTotal() > 0, "总记录数应大于 0");
            assertEquals(10, result.getSize(), "每页大小应为 10");
            assertFalse(result.getRecords().isEmpty(), "返回的数据集合不应为空");
            assertInstanceOf(Page.class, result, "返回结果必须是 Page 类型");
        }



}