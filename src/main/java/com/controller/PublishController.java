// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.enity.Question;
import com.enity.User;
import com.mapper.questionMapper;
import com.service.questionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Resource
    private questionMapper questionMapper;
    @Resource
    private questionService questionService;

    /**
     * 回显已发布问题
     */
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model){
        Question question = questionMapper.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());

        return "publish";
    }
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    /**
     * 发布问题
     */
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title")String title,
            @RequestParam("description")String description,
            @RequestParam("tag")String tag,
            @RequestParam("id")Long id,
            HttpServletRequest request,
            Model model){
        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if (title==null || title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description==null || description==""){
            model.addAttribute("error","描述不能为空");
            return "publish";
        }
        if (tag==null || tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        Question question=new Question();

        question.setTitle(title);
        question.setTag(tag);
        question.setDescription(description);
        question.setCreator(user.getId());
        question.setId(id);

        questionService.createOrUpdate(question);

        return "redirect:/";
    }
}
