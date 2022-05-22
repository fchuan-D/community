// @author:樊川
// @email:945001786@qq.com
package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface notificationMapper extends BaseMapper<Notification> {

    @Select("select count(1) from notification where receiver = ${userId}")
    Integer count(Long receiverId);

    @Select("select * from notification where receiver=#{receiverId} and status = 0 limit #{offSet},#{size}")
    List<Notification> list(Long receiverId, Integer offSet, Integer size);

    @Select("select * from notification")
    List<Notification> select();

    @Select("select count(1) from notification where receiver = ${userId} and status=0")
    Long unreadCount(Long id);
}
