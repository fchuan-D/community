// @author:樊川
// @email:945001786@qq.com
package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enity.hotNews;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface hotNewsMapper extends BaseMapper<hotNews> {
    @Select("insert into hotnews (title,`rank`,hotcount,url,gmt_create) values (#{title},#{rank},#{hotCount},#{url},#{gmtCreate})")
    void setHotsList(hotNews hotNews);
}
