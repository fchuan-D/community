// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.dto.CommentDTO;
import com.dto.ResultDTO;
import com.enity.Comment;
import com.enity.User;
import com.exception.ErrorCode;
import com.service.commentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Resource
    private commentService commentService;

    // 接受问题回复
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(ErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentator(user.getId());
        commentService.insert(comment,user);
        return ResultDTO.okOf();
    }

    // 返回问题回复的评论
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO comments(@PathVariable("id") Long id, Model model){
        // 获取回复下的所有评论
        List<Comment> comments = commentService.getListByCId(id);
        model.addAttribute("childComments",comments);
        return ResultDTO.okOf(comments);
    }
}
