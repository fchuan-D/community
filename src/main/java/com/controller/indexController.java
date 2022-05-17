// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.DTO.PaginationDTO;
import com.service.questionService;
import jdk.jfr.Frequency;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class indexController {
    @Resource
    private questionService questionService;

    @GetMapping("/")
    public String index(
                        HttpServletResponse response,
                        @RequestParam(value = "page",defaultValue = "1")Integer page,
                        @RequestParam(value = "size",defaultValue = "5")Integer size,
                        Model model){
//        System.out.println(response.getHeader("Set-Cookie"));
        PaginationDTO pagination = questionService.getList(page, size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
