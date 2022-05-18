// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.dto.PaginationDTO;
import com.exception.CustomizeException;
import com.exception.ErrorCode;
import com.mapper.questionMapper;
import com.service.questionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


@Controller
public class IndexController {
    @Resource
    private questionService questionService;
    @Resource
    private questionMapper questionMapper;

    @GetMapping("/")
    public String index(
                        HttpServletResponse response,
                        @RequestParam(value = "page",defaultValue = "1")Integer page,
                        @RequestParam(value = "size",defaultValue = "5")Integer size,
                        Model model){
        System.out.println("Set-Cookie:"+response.getHeader("Set-Cookie"));
        if ((page-1)*size>questionMapper.count()){
            throw new CustomizeException(ErrorCode.PAGE_NOT_FOUND);
        }
        PaginationDTO pagination = questionService.getList(page, size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
