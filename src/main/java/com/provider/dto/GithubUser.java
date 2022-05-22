// @author:樊川
// @email:945001786@qq.com
package com.provider.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
