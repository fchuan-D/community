// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.dto.PaginationDTO;
import com.enity.User;
import com.service.questionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Resource
    private questionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(
            HttpServletRequest request,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "size",defaultValue = "5")Integer size,
            @PathVariable(name = "action")String action,
            Model model){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
        {
            return "redirect:/";
        }

        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }else{
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        PaginationDTO pagination = questionService.perList(user.getId(), page, size);
        model.addAttribute("pagination",pagination);

        return "profile";
    }
}
