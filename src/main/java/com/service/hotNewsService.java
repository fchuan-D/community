// @author:樊川
// @email:945001786@qq.com
package com.service;

import com.enity.hotNews;
import com.mapper.hotNewsMapper;
import com.provider.HNewsProvider;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class hotNewsService {
    @Resource
    private HNewsProvider newsProvider;
    @Resource
    private hotNewsMapper hotNewsMapper;

    public List<hotNews> setHotNews() {
        // 爬虫获取热搜
        // 并返回爬虫数据
        List<hotNews> hotNews = newsProvider.getHotNews();
        for (hotNews hotNew : hotNews) {
            hotNewsMapper.setHotsList(hotNew);
        }
        return hotNews;
    }
}
