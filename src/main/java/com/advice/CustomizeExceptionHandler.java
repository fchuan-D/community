// @author:樊川
// @email:945001786@qq.com
package com.advice;

import com.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ModelAndView handleControllerException(Throwable ex, Model model) {
        if (ex instanceof CustomizeException){
            model.addAttribute("message", ex.getMessage());
        }else{
            model.addAttribute("message", "服务器冒烟了，请稍后再试");
        }
        return new ModelAndView("error");
    }
}
