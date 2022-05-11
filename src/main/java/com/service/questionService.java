// @author:樊川
// @email:945001786@qq.com
package com.service;

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

    public List<Question> getList() {
        List<Question> questionList = questionMapper.list();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            question.setUser(user);
        }
        return questionList;
    }
}
