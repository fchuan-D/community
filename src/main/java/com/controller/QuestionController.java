// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.enity.Comment;
import com.enity.Question;
import com.service.commentService;
import com.service.questionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class QuestionController {
    @Resource
    private questionService questionService;

    @Resource
    private commentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id, Model model){
        Question question = questionService.getById(id);
        // 增加浏览次数
        questionService.incView(id);
        // 获取评论
        List<Comment> comments = commentService.getById(id);
        model.addAttribute("question",question);
        model.addAttribute("comments",comments);
        return "question";
    }
}
