package com.zw.zwplatform.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zwplatform.common.ErrorCode;
import com.zw.zwplatform.constant.CommonConstant;
import com.zw.zwplatform.exception.ThrowUtils;
import com.zw.zwplatform.mapper.QuestionBankQuestionMapper;
import com.zw.zwplatform.model.dto.questionbankquestion.QuestionBankQuestionQueryRequest;
import com.zw.zwplatform.model.entity.Question;
import com.zw.zwplatform.model.entity.QuestionBank;
import com.zw.zwplatform.model.entity.QuestionBankQuestion;
import com.zw.zwplatform.service.QuestionBankQuestionService;
import com.zw.zwplatform.service.QuestionBankService;
import com.zw.zwplatform.service.QuestionService;
import com.zw.zwplatform.service.UserService;
import com.zw.zwplatform.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题库题目服务实现
 *
 * @author <a href="https://github.com/lizw">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Service
@Slf4j
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion> implements QuestionBankQuestionService {

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private QuestionService questionService;

    @Resource
    private QuestionBankService questionBankService;

    @Override
    public void validQuestion(QuestionBankQuestion questionBankQuestion, boolean add) {
        ThrowUtils.throwIf(questionBankQuestion == null, ErrorCode.PARAMS_ERROR);
        // 题目和题库必须存在
        Long questionId = questionBankQuestion.getQuestionId();
        if (questionId != null) {
            Question question = questionService.getById(questionId);
            ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        Long questionBankId = questionBankQuestion.getQuestionBankId();
        if (questionBankId != null) {
            QuestionBank questionBank = questionBankService.getById(questionBankId);
            ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");
        }
    }

    @Override
    public Page<Question> getQuestionPageByQuestionBankId(long id) {
        QueryWrapper<QuestionBankQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionBankId", id);
        List<QuestionBankQuestion> questionBankQuestionList = baseMapper.selectList(queryWrapper);
        List<Long> questionIds= questionBankQuestionList.stream().map(QuestionBankQuestion::getQuestionId).collect(Collectors.toList());
        // 题目ids 为空说明没有题目
        if (CollUtil.isEmpty(questionIds)) {
            return new Page<>();
        }
        /**第一种写法
        List<Question> questions = questionService.listByIds(questionIds);
        Page<Question> questionPage = new Page<>();
        questionPage.setRecords(questions);
        return questionPage;
         */
        //第二种写法
        return questionService.page(new Page<>(1, 100), new QueryWrapper<Question>().in("id", questionIds));
    }

    /**
     * 获取查询条件
     *
     * @param questionBankQuestionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionBankQuestion> getQueryWrapper(QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest) {
        QueryWrapper<QuestionBankQuestion> queryWrapper = new QueryWrapper<>();
        if (questionBankQuestionQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = questionBankQuestionQueryRequest.getId();
        Long notId = questionBankQuestionQueryRequest.getNotId();
        String title = questionBankQuestionQueryRequest.getTitle();
        String content = questionBankQuestionQueryRequest.getContent();
        String searchText = questionBankQuestionQueryRequest.getSearchText();
        String sortField = questionBankQuestionQueryRequest.getSortField();
        String sortOrder = questionBankQuestionQueryRequest.getSortOrder();
        List<String> tagList = questionBankQuestionQueryRequest.getTags();
        Long userId = questionBankQuestionQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


}
