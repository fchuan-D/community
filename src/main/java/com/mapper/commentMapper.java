// @author:樊川
// @email:945001786@qq.com
package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enity.Comment;
import com.enums.CommentTypeEnum;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface commentMapper extends BaseMapper<Comment>{

    @Insert("insert into comment (parent_id, type, commentator, gmt_create, gmt_modified, content, like_count) values (#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{content},#{likeCount})")
    int insert(Comment comment);

    @Select("select * from comment where id = #{parentId}")
    Comment getById(Long parentId);

    @Update("update comment set comment_count = comment_count + 1 where id=#{id}")
    void incCommentCount(Comment parentComment);

    @Select("select * from comment where parent_id=#{parentId} and type=1 order by gmt_create DESC")
    List<Comment> commentList(Long parentId, CommentTypeEnum question);

    @Select("select * from comment where parent_id=#{commentId} and type=2 order by gmt_create DESC")
    List<Comment> childComments(Long commentId, CommentTypeEnum comment);
}
