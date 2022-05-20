// @author:樊川
// @email:945001786@qq.com
package com.enity;

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer commentCount;
    private Long likeCount;
    private String content;
}
