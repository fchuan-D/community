// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.enity.User;
import com.enity.accessToken;
import com.exception.CustomizeException;
import com.exception.ErrorCode;
import com.mapper.userMapper;
import com.provider.dto.GithubUser;
import com.provider.githubService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Resource
    private userMapper userMapper;
    @Resource
    private githubService githubService;

    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                            HttpServletResponse response){
        accessToken accessToken=new accessToken();
        accessToken.setCode(code);
        accessToken.setState(state);

        String Token = githubService.getAccessToken(accessToken);
        GithubUser githubUser = githubService.getUser(Token);

        if(githubUser.getId()!=null) {
            User user = new User();
            // 生成token
            String userToken = UUID.randomUUID().toString();
            user.setName(githubUser.getName());
            user.setToken(userToken);
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());

            if (userMapper.findByAccountId(githubUser.getId()) != null) {
                userMapper.update(user);
                // 发送token给浏览器
                response.addCookie(new Cookie("token", userToken));
            } else {
                if (user.getName() == null){
                    int random = (int)(Math.random()*10);
                    user.setName("未命名用户"+ random);
                }
                if (user.getAvatarUrl() == null){
                    user.setAvatarUrl("/static/images/default-avatar.png");
                }
                userMapper.insert(user);
                // 发送token给浏览器
                response.addCookie(new Cookie("token", userToken));
            }
        }else{
            // 登录失败
            throw new CustomizeException(ErrorCode.LOGIN_FAIL);
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        user = userMapper.findByAccountId(Long.valueOf(user.getAccountId()));
        user.setToken(UUID.randomUUID().toString());
        userMapper.update(user);
        request.getSession().removeAttribute("user");
        return "redirect:/";
    }
}
