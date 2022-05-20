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

@Service
public class commentService {

    @Resource
    private commentMapper commentMapper;

    @Resource
    private questionMapper questionMapper;

    @Resource
    private userMapper userMapper;

//    @Resource
//    private CommentExtMapper commentExtMapper;
//
//    @Resource
//    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(ErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(ErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            Comment dbComment = commentMapper.getById(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(ErrorCode.COMMENT_NOT_FOUND);
            }

            // 回复问题
            Question question = questionMapper.getById(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(ErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);

            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
//            commentExtMapper.incCommentCount(parentComment);
//
//            // 创建通知
//            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        } else {
            // 回复问题
            Question question = questionMapper.getById(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(ErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
//            questionExtMapper.incCommentCount(question);
//
//            // 创建通知
//            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
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

