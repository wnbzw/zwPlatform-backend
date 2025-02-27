package com.zw.zwplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zw.zwplatform.model.entity.Question;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;


/**
* @author 16247
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2025-02-13 22:12:16
* @Entity model.domain.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 查询题目列表（包括已被删除的数据）
     */
    @Select("select * from question where updateTime >= #{fiveMinutesAgoDate}")
    List<Question> listQuestionWithDelete(Date fiveMinutesAgoDate);
}




