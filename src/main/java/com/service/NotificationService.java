// @author:樊川
// @email:945001786@qq.com
package com.service;

import com.dto.PaginationDTO;
import com.enity.Notification;
import com.enums.NotificationTypeEnum;
import com.mapper.notificationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NotificationService {

    @Resource
    private notificationMapper notificationMapper;

    public PaginationDTO getNotifyList(Long receiverId, Integer page, Integer size) {
        Integer offSet = size*(page-1);
        List<Notification> notifyList = notificationMapper.list(receiverId,offSet,size);
        PaginationDTO<Notification> paginationDTO = new PaginationDTO<>();
        for (Notification notification : notifyList) {
            notification.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        }
        paginationDTO.setData(notifyList);
        // 数据总条数
        Integer totalCount = notificationMapper.count(receiverId);
        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;
    }

    public Long getUnread(Long id) {
        Long unreadCount = notificationMapper.unreadCount(id);
        return unreadCount;
    }
}
