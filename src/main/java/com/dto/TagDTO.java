// @author:樊川
// @email:945001786@qq.com
package com.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
