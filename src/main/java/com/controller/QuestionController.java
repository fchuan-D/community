// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.enity.Question;
import com.service.questionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

@Controller
public class QuestionController {
    @Resource
    private questionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id, Model model){
        Question question = questionService.getById(id);
        // 增加浏览次数
        questionService.incView(id);
        model.addAttribute("question",question);
        return "question";
    }
}
