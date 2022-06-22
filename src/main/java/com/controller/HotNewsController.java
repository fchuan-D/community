// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.enity.Question;
import com.enity.hotNews;
import com.provider.HNewsProvider;
import com.service.hotNewsService;
import com.service.questionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class HotNewsController {
    @Resource
    private questionService questionService;
    @Resource
    private hotNewsService hotNewsService;


    @GetMapping("/getHotNews")
    public String getHotNews(Model model){
        List<Question> hotQues = questionService.getHotList(5);
        model.addAttribute("hotQues", hotQues);
        List<hotNews> hotNews = hotNewsService.setHotNews();
        model.addAttribute("hotNews",hotNews);
        return "Hots";
    }
}
