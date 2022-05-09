// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.enity.accessToken;
import com.enity.giteeUser;
import com.service.giteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private giteeService giteeService;
    @Value("${gitee.client.id}")
    private String clientId;
    @Value("${gitee.client.secret}")
    private String clientSecret;
    @Value("${gitee.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        accessToken accessToken=new accessToken();
        accessToken.setCode(code);
        accessToken.setRedirect_uri(redirectUri);
        accessToken.setState(state);
        accessToken.setClient_secret(clientSecret);
        accessToken.setClient_id(clientId);
        String Token = giteeService.getAccessToken(accessToken);
        giteeUser user = giteeService.getUser(Token);
        System.out.println("user:"+user.getName());
        return "index";
    }
}
