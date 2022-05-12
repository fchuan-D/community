// @author:樊川
// @email:945001786@qq.com
package com.service;

import com.DTO.PaginationDTO;
import com.enity.Question;
import com.enity.User;
import com.mapper.questionMapper;
import com.mapper.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class questionService {
    @Autowired
    userMapper userMapper;
    @Autowired
    questionMapper questionMapper;

    public PaginationDTO getList(Integer page, Integer size) {
        Integer offSet = size*(page-1);
        List<Question> questionList = questionMapper.list(offSet,size);
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            question.setUser(user);
        }
        paginationDTO.setQuestions(questionList);
        // 数据总条数
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;
    }
}
