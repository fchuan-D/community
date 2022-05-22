// @author:樊川
// @email:945001786@qq.com
package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enity.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface questionMapper extends BaseMapper<Question> {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    Boolean create(Question question);

    @Select("select * from question order by gmt_create DESC limit #{offSet},#{size}")
    List<Question> list(Integer offSet, Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator=#{userId} order by gmt_create DESC limit #{offSet},#{size}")
    List<Question> perList(Long userId, Integer offSet, Integer size);

    @Select("select count(1) from question where creator=#{userId}")
    Integer perCount(Long userId);

    @Select("select * from question where id=#{id}")
    Question getById(Long id);

    @Update("update question set title = #{title},description=#{description},tag=#{tag},gmt_modified=#{gmtModified} where id=#{id}")
    Boolean update(Question question);

    @Update("update question set view_count = view_count+1 where id=#{id}")
    void incView(Long id);

    @Update("update question set comment_count = comment_count+1 where id=#{id}")
    void incCommentCount(Question parentQuestion);

    @Select("select * from question where tag like #{tag} and id !=${id}")
    List<Question> selectRelated(Question question);
}
