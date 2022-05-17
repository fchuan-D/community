// @author:樊川
// @email:945001786@qq.com
package com.service;

import com.DTO.PaginationDTO;
import com.enity.Question;
import com.enity.User;
import com.mapper.questionMapper;
import com.mapper.userMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class questionService {
    @Resource
    userMapper userMapper;
    @Resource
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

    public PaginationDTO perList(Integer userId, Integer page, Integer size) {
        Integer offSet = size*(page-1);
        List<Question> questionList = questionMapper.perList(userId,offSet,size);
        PaginationDTO paginationDTO = new PaginationDTO();
        User user = userMapper.findById(userId);
        for (Question question : questionList) {
            question.setUser(user);
        }
        paginationDTO.setQuestions(questionList);
        // 数据总条数
        Integer totalCount = questionMapper.perCount(userId);
        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;
    }
}
