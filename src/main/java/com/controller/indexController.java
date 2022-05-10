// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.enity.user;
import com.mapper.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Controller
public class indexController {
    @Autowired
    userMapper userMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                String token = cookie.getValue();
                user user = userMapper.findByToken(token);
                if (user != null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        return"index";
    }
}
