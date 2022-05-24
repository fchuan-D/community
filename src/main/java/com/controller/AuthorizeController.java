// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.enity.User;
import com.enity.accessToken;
import com.provider.dto.giteeUser;
import com.mapper.userMapper;
import com.provider.giteeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Resource
    private userMapper userMapper;
    @Resource
    private giteeService giteeService;
    @Value("${gitee.client.id}")
    private String clientId;
    @Value("${gitee.client.secret}")
    private String clientSecret;
    @Value("${gitee.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                            HttpServletResponse response){
        accessToken accessToken=new accessToken();
        accessToken.setCode(code);
        accessToken.setRedirect_uri(redirectUri);
        accessToken.setState(state);
        accessToken.setClient_secret(clientSecret);
        accessToken.setClient_id(clientId);

        String Token = giteeService.getAccessToken(accessToken);
        giteeUser giteeUser = giteeService.getUser(Token);
        if (giteeUser!=null){
            User user = new User();
            // 生成token
            String token = UUID.randomUUID().toString();
            user.setName(giteeUser.getName());
            user.setToken(token);
            user.setAccountId(String.valueOf(giteeUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(giteeUser.getAvatarUrl());
            if (userMapper.findByAccountId(giteeUser.getId()) != null)
            {
                userMapper.update(user);
            } else {
                userMapper.insert(user);
            }
            // 发送token给浏览器
            response.addCookie(new Cookie("token",token));
        }else{
            // 登录失败
            return "index";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        user.setToken(null);
        userMapper.update(user);
        request.getSession().removeAttribute("user");
        return "redirect:/";
    }
}
