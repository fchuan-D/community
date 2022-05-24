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

    @Select("select * from notification where receiver=#{receiverId} order by gmt_create DESC limit #{offSet},#{size}")
    List<Notification> list(Long receiverId, Integer offSet, Integer size);

    @Select("select * from notification")
    List<Notification> select();

    @Select("select count(1) from notification where receiver = ${userId} and status=0")
    Long unreadCount(Long id);

    @Select("select id,notifier,receiver,outerid,type,gmt_create,status,notifier_name,outer_title FROM notification WHERE id=#{id}")
    Notification selectById(Long id);
}
