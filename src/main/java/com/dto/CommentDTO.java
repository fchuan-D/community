// @author:樊川
// @email:945001786@qq.com
package com.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
