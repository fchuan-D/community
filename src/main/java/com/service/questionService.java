// @author:樊川
// @email:945001786@qq.com
package com.service;

import com.dto.PaginationDTO;
import com.enity.Question;
import com.enity.User;
import com.exception.CustomizeException;
import com.exception.ErrorCode;
import com.mapper.questionMapper;
import com.mapper.userMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class questionService {
    @Resource
    private userMapper userMapper;
    @Resource
    private questionMapper questionMapper;

    /**
     * 根据标签选择相关问题
     */
    public List<Question> selectRelated(Question queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays
                .stream(tags)
                .filter(StringUtils::isNotBlank)
                .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(","));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionMapper.selectRelated(question);

        return questions;
    }

    /**
     * 修改已发布问题
     */
    public void createOrUpdate(Question question) {
        if (question.getId()==null){
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            boolean created = questionMapper.create(question);
            if (!created){
                throw new CustomizeException(ErrorCode.QUESTION_NOT_CREATE);
            }
        }else{
            // 修改
            question.setGmtModified(System.currentTimeMillis());
            boolean updated = questionMapper.update(question);
            if (!updated){
                throw new CustomizeException(ErrorCode.QUESTION_NOT_UPDATE);
            }
        }
    }

    /**
     * 主页获取所有问题列表展示
     */
    public PaginationDTO getList(Integer page, Integer size) {
        Integer offSet = size*(page-1);
        List<Question> questionList = questionMapper.list(offSet,size);
        PaginationDTO<Question> paginationDTO = new PaginationDTO<>();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            question.setUser(user);
        }
        paginationDTO.setData(questionList);
        // 数据总条数
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;
    }

    /**
     * 个人页面获取问题列表展示
     */
    public PaginationDTO perList(Long userId, Integer page, Integer size) {
        Integer offSet = size*(page-1);
        List<Question> questionList = questionMapper.perList(userId,offSet,size);
        PaginationDTO<Question> paginationDTO = new PaginationDTO<>();
        User user = userMapper.findById(userId);
        for (Question question : questionList) {
            question.setUser(user);
        }
        paginationDTO.setData(questionList);
        // 数据总条数
        Integer totalCount = questionMapper.perCount(userId);
        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;
    }

    /**
     * 获取单个问题的详情
     */
    public Question getById(Long id) {
        Question question = questionMapper.getById(id);
        if (question == null){
            throw new CustomizeException(ErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.findById(question.getCreator());
        question.setUser(user);
        return question;
    }

    /**
     * 增加阅读数
     */
    public void incView(Long id) {
        questionMapper.incView(id);
    }
}
