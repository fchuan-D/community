// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.cache.TagCache;
import com.dto.TagDTO;
import com.enity.Question;
import com.enity.User;
import com.mapper.questionMapper;
import com.service.questionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Resource
    private questionMapper questionMapper;
    @Resource
    private questionService questionService;

    /**
     * 回显已发布问题
     */
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model){
        Question question = questionMapper.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("tags",TagCache.get());
        model.addAttribute("id",question.getId());

        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    /**
     * 发布问题
     */
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title")String title,
            @RequestParam("description")String description,
            @RequestParam("tag")String tag,
            @RequestParam(value = "id",required = false)Long id,
            HttpServletRequest request,
            Model model){
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("tags",TagCache.get());
        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        // 已填写内容回显
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        String invalid = TagCache.filterInvalid(tag);

        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (StringUtils.length(title) > 50) {
            model.addAttribute("error", "标题最多 50 个字符");
            return "publish";
        }
        if (StringUtils.isBlank(description)) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
//        if (user.getDisable() != null && user.getDisable() == 1) {
//            model.addAttribute("error", "操作被禁用，如有疑问请联系管理员");
//            return "publish";
//        }
//        if (questionRateLimiter.reachLimit(user.getId())) {
//            model.addAttribute("error", "操作太快，请求被限制");
//            return "publish";
//        }
        Question question=new Question();

        question.setTitle(title);
        question.setTag(tag);
        question.setDescription(description);
        question.setCreator(user.getId());
        question.setId(id);

        questionService.createOrUpdate(question);

        return "redirect:/";
    }
}
