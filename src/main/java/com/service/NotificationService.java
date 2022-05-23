// @author:樊川
// @email:945001786@qq.com
package com.service;

import com.dto.PaginationDTO;
import com.enity.Notification;
import com.enity.User;
import com.enums.NotificationStatusEnum;
import com.enums.NotificationTypeEnum;
import com.exception.CustomizeException;
import com.exception.ErrorCode;
import com.mapper.notificationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Resource
    private notificationMapper notificationMapper;

    public PaginationDTO getNotifyList(Long receiverId, Integer page, Integer size) {
        Integer offSet = size*(page-1);
        List<Notification> notifications = notificationMapper.list(receiverId,offSet,size);
        PaginationDTO<Notification> paginationDTO = new PaginationDTO<>();
        for (Notification notification : notifications) {
            notification.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        }
        paginationDTO.setData(notifications);
        // 数据总条数
        Integer totalCount = notificationMapper.count(receiverId);
        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;
    }

    public Notification read(Long id , User user){
        Notification notification = notificationMapper.selectById(id);
        if (notification == null){
            throw new CustomizeException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())){
            throw new CustomizeException(ErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateById(notification);

        return notification;
    }

    public Long getUnread(Long id) {
        Long unreadCount = notificationMapper.unreadCount(id);
        return unreadCount;
    }
}
