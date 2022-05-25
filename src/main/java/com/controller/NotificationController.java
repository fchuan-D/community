// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.enity.Notification;
import com.enity.User;
import com.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Resource
    NotificationService notificationService;

    @RequestMapping("/notification/{id}")
    public String read(@PathVariable("id") Long id, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return "redirect:/";
        }
        Notification read = notificationService.read(id, user);
        return "redirect:/question/"+read.getOuterid();
    }
}
