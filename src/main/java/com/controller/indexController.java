// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.DTO.PaginationDTO;
import com.service.questionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;


@Controller
public class indexController {
    @Resource
    questionService questionService;

    @GetMapping("/")
    public String index(@RequestParam(value = "page",defaultValue = "1")Integer page,
                        @RequestParam(value = "size",defaultValue = "5")Integer size,
                        Model model){
        PaginationDTO pagination = questionService.getList(page, size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
