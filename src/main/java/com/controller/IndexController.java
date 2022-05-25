// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.dto.PaginationDTO;
import com.exception.CustomizeException;
import com.exception.ErrorCode;
import com.mapper.questionMapper;
import com.service.questionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController {
    @Resource
    private questionService questionService;
    @Resource
    private questionMapper questionMapper;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${random}")
    private String random;

    @GetMapping("/")
    public String index(
                        HttpServletRequest request,
                        @RequestParam(value = "page",defaultValue = "1")Integer page,
                        @RequestParam(value = "size",defaultValue = "5")Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        Model model){
        if ((page-1)*size>questionMapper.count()){
            throw new CustomizeException(ErrorCode.PAGE_NOT_FOUND);
        }
        PaginationDTO pagination = questionService.getList(page, size);
        model.addAttribute("search", search);
        model.addAttribute("pagination",pagination);
        request.getSession().setAttribute("redirectUri", redirectUri);
        request.getSession().setAttribute("clientId", clientId);
        request.getSession().setAttribute("random", random);

        return "index";
    }

    @GetMapping("/search")
    public String getSearch(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "size",defaultValue = "5")Integer size,
            @RequestParam(name = "search", required = false) String search,
            Model model){
        if ((page-1)*size>questionMapper.count()){
            throw new CustomizeException(ErrorCode.PAGE_NOT_FOUND);
        }
        PaginationDTO pagination = questionService.getSearch(search,page, size);
        model.addAttribute("search",search);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
