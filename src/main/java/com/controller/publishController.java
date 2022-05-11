// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.enity.Question;
import com.enity.User;
import com.mapper.questionMapper;
import com.mapper.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class publishController {
    @Autowired
    private questionMapper questionMapper;
    @Autowired
    private userMapper userMapper;
    @GetMapping("/publish")
    public String publish(){

        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title")String title,
            @RequestParam("description")String description,
            @RequestParam("tag")String tag,
            HttpServletRequest request,
            Model model){
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
        User user = null;
        Cookie[] cookies = request.getCookies();
        // cookies可能为空
        if (cookies!=null && cookies.length!=0) {
            for (Cookie cookie : cookies) {
                // 数据库中查找是否存在用户token
                // 判断是否已经登录
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        question.setTitle(title);
        question.setTag(tag);
        question.setDescription(description);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);

        return "redirect:/";
    }
}
