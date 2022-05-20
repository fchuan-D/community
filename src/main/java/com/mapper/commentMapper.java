// @author:樊川
// @email:945001786@qq.com
package com.mapper;

import com.enity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface commentMapper {

    @Insert("insert into comment (parent_id, type, commentator, gmt_create, gmt_modified, content, like_count) values (#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{content},#{likeCount})")
    void insert(Comment comment);

    @Select("select * from comment where id = #{parentId}")
    Comment getById(Long parentId);

    @Update("update comment set comment_count = comment_count + 1 where id=#{id}")
    void incCommentCount(Comment parentComment);

    @Select("select * from comment where parent_id=#{parentId}")
    List<Comment> commentList(Long parentId);
}
