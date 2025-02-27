package com.zw.zwplatform.esdao;

import com.zw.zwplatform.model.dto.question.QuestionEsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class QuestionEsDaoTest {

    @Resource
    private QuestionEsDao questionEsDao;

    @Test
    void findByUserId() {

        List<QuestionEsDTO> byUserId = questionEsDao.findByUserId(1L);

    }
}
