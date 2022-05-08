// @author:樊川
// @email:945001786@qq.com
package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class indexController {
    @GetMapping("/")
    public String index(){
        return"index";
    }
}
