// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.dto.PaginationDTO;
import com.enity.Notification;
import com.enity.Question;
import com.enity.User;
import com.exception.CustomizeException;
import com.exception.ErrorCode;
import com.service.NotificationService;
import com.service.questionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Resource
    private questionService questionService;

    @Resource
    private NotificationService notificationService;

    @GetMapping("/{action}")
    public String doProfile(
            HttpServletRequest request,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "size",defaultValue = "5")Integer size,
            @PathVariable(name = "action")String action,
            Model model){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
        {
           throw new CustomizeException(ErrorCode.NO_LOGIN);
        }

        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            PaginationDTO<Question> pagination = questionService.perList(user.getId(), page, size);
            model.addAttribute("pagination",pagination);
        }else if ("replies".equals(action)){
            PaginationDTO<Notification> pagination = notificationService.getNotifyList(user.getId(), page, size);
            model.addAttribute("section","replies");
            model.addAttribute("pagination",pagination);
            model.addAttribute("sectionName","最新回复");
        }
        return "profile";
    }
}
