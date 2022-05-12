// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.DTO.PaginationDTO;
import com.enity.Question;
import com.enity.User;
import com.mapper.questionMapper;
import com.mapper.userMapper;
import com.service.questionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class indexController {
    @Autowired
    userMapper userMapper;
    @Autowired
    questionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        @RequestParam(value = "page",defaultValue = "1")Integer page,
                        @RequestParam(value = "size",defaultValue = "5")Integer size,
                        Model model){
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length!=0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        PaginationDTO pagination = questionService.getList(page, size);
        model.addAttribute("pagination",pagination);
        System.out.println("page:"+pagination.getPage());

        return"index";
    }
}
