// @author:樊川
// @email:945001786@qq.com
package com.service;

import com.enity.Comment;
import com.enity.Question;
import com.enity.User;
import com.enums.CommentTypeEnum;
import com.exception.CustomizeException;
import com.exception.ErrorCode;
import com.mapper.commentMapper;
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



    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(ErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(ErrorCode.TYPE_PARAM_WRONG);
        }
        // 评论评论
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            Comment parentComment = commentMapper.getById(comment.getParentId());
            if (parentComment == null) {
                throw new CustomizeException(ErrorCode.COMMENT_NOT_FOUND);
            }

            // 回复问题
            Question parentQuestion = questionMapper.getById(parentComment.getParentId());
            if (parentQuestion == null) {
                throw new CustomizeException(ErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            // 增加父评论评论数
            commentMapper.incCommentCount(parentComment);
            // 增加问题评论数
            questionMapper.incCommentCount(parentQuestion);
           // 创建通知
//            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        }
        // 评论问题
        else {
            // 回复问题
            Question parentQuestion = questionMapper.getById(comment.getParentId());
            if (parentQuestion == null) {
                throw new CustomizeException(ErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionMapper.incCommentCount(parentQuestion);

            // 创建通知
//            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    public List<Comment> getById(Long parentId) {
        List<Comment> comments = commentMapper.commentList(parentId);
        for (Comment comment : comments) {
            Long uerId = comment.getCommentator();
            comment.setUser(userMapper.findById(uerId));
        }
        return comments;
    }

//    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
//        if (receiver == comment.getCommentator()) {
//            return;
//        }
//        Notification notification = new Notification();
//        notification.setGmtCreate(System.currentTimeMillis());
//        notification.setType(notificationType.getType());
//        notification.setOuterid(outerId);
//        notification.setNotifier(comment.getCommentator());
//        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
//        notification.setReceiver(receiver);
//        notification.setNotifierName(notifierName);
//        notification.setOuterTitle(outerTitle);
//        notificationMapper.insert(notification);
//    }
//
//    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
//        CommentExample commentExample = new CommentExample();
//        commentExample.createCriteria()
//                .andParentIdEqualTo(id)
//                .andTypeEqualTo(type.getType());
//        commentExample.setOrderByClause("gmt_create desc");
//        List<Comment> comments = commentMapper.selectByExample(commentExample);
//
//        if (comments.size() == 0) {
//            return new ArrayList<>();
//        }
//        // 获取去重的评论人
//        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
//        List<Long> userIds = new ArrayList();
//        userIds.addAll(commentators);
//
//
//        // 获取评论人并转换为 Map
//        UserExample userExample = new UserExample();
//        userExample.createCriteria()
//                .andIdIn(userIds);
//        List<User> users = userMapper.selectByExample(userExample);
//        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
//
//
//        // 转换 comment 为 commentDTO
//        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
//            CommentDTO commentDTO = new CommentDTO();
//            BeanUtils.copyProperties(comment, commentDTO);
//            commentDTO.setUser(userMap.get(comment.getCommentator()));
//            return commentDTO;
//        }).collect(Collectors.toList());
//
//        return commentDTOS;
//    }
}

