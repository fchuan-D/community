// @author:樊川
// @email:945001786@qq.com
package com.mapper;

import com.enity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface commentMapper {

    @Insert("insert into comment values (#{coment.})")
    void insert(Comment comment);

    @Select("select * from comment where id = #{parentId}")
    Comment getById(Long parentId);
}
