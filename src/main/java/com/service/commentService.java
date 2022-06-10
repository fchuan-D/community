// @author:樊川
// @email:945001786@qq.com
package com.service;

import com.enity.Comment;
import com.enity.Notification;
import com.enity.Question;
import com.enity.User;
import com.enums.CommentTypeEnum;
import com.enums.NotificationStatusEnum;
import com.enums.NotificationTypeEnum;
import com.exception.CustomizeException;
import com.exception.ErrorCode;
import com.mapper.commentMapper;
import com.mapper.notificationMapper;
import com.mapper.questionMapper;
import com.mapper.userMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class commentService {

    @Resource
    private commentMapper commentMapper;

    @Resource
    private questionMapper questionMapper;

    @Resource
    private userMapper userMapper;

    @Resource
    private notificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(ErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(ErrorCode.TYPE_PARAM_WRONG);
        }
        // 评论评论
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            // 被回复评论
            Comment parentComment = commentMapper.getById(comment.getParentId());
            if (parentComment == null) {
                throw new CustomizeException(ErrorCode.COMMENT_NOT_FOUND);
            }

            // 获取被回复问题
            Question parentQuestion = questionMapper.getById(parentComment.getParentId());
            if (parentQuestion == null) {
                throw new CustomizeException(ErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            // 增加父评论评论数
            commentMapper.incCommentCount(parentComment);
            // 增加问题评论数
            questionMapper.incCommentCount(parentQuestion);
           // 创建通知——>被回复评论者
            createNotify(comment, parentComment.getCommentator(), commentator.getName(), parentQuestion.getTitle(), NotificationTypeEnum.REPLY_COMMENT, parentQuestion.getId());
        }
        // 评论问题
        else {
            // 被回复问题
            Question parentQuestion = questionMapper.getById(comment.getParentId());
            if (parentQuestion == null) {
                throw new CustomizeException(ErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionMapper.incCommentCount(parentQuestion);

            // 创建通知——>被回复问题者
            createNotify(comment, parentQuestion.getCreator(), commentator.getName(), parentQuestion.getTitle(), NotificationTypeEnum.REPLY_QUESTION, parentQuestion.getId());
        }
    }

    /**
     * 创建通知,重构获取回复或评论的发表者和类型
     */
    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if (receiver.equals(comment.getCommentator())) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notification.setId(0L);
        notificationMapper.insert(notification);
    }

    /**
     * 查询某问题下的所有评论
     */
    public List<Comment> getListByQId(Long parentId) {
        List<Comment> comments = commentMapper.commentList(parentId,CommentTypeEnum.QUESTION);
        for (Comment comment : comments) {
            Long uerId = comment.getCommentator();
            comment.setUser(userMapper.findById(uerId));
        }
        return comments;
    }

    public List<Comment> getListByCId(Long commentId) {
        List<Comment> comments = commentMapper.childComments(commentId,CommentTypeEnum.COMMENT);
        for (Comment comment : comments) {
            Long uerId = comment.getCommentator();
            comment.setUser(userMapper.findById(uerId));
        }
        return comments;
    }
}

