// @author:樊川
// @email:945001786@qq.com
package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController{
    @GetMapping("/error")
    public String doError(){
        return "error";
    }
}
