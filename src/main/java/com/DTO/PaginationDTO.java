// @author:樊川
// @email:945001786@qq.com
package com.DTO;

import com.enity.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装页码信息
 * @Param:
 *      showPrevious:是否有前一页
 *      showFirstPage:是否展示首页标签
 *      showNext:是否有下一页
 *      showEndPage:是否展示尾页标签
 *      page:当前页
 *      lastPage:最后一页
 *      pages:页码表
 */
@Data
public class PaginationDTO {
    private List<Question> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private Integer lastPage;
    private List<Integer> pages = new ArrayList<>();

    public Integer setPagination(Integer totalCount, Integer page, Integer size) {
        this.page=page;
        Integer totalPage;
        // 计算总页数
        if (totalCount % size == 0){
            totalPage = totalCount/size;
        }else {
            totalPage = totalCount/size + 1;
        }
        pages.add(page);
        for (int i = 1; i <= 4; i++) {
            // 若满足条件,往page前添加三个
            if (page-i>0){
                pages.add(0,page-i);
            }
            // 若满足条件,往page后添加三个
            if (page+i<=totalPage){
                pages.add(page+i);
            }
        }
        // 是否有前后页
        showPrevious = page != 1;
        showNext = !page.equals(totalPage);
        // 页码表是否包含首页或尾页
        // 包含首页，则前往首页标签不展示
        showFirstPage = !pages.contains(1);
        // 包含尾页，则前往尾页页标签不展示
        showEndPage = !pages.contains(totalPage);
        lastPage=totalPage;

        return totalPage;
    }
}
