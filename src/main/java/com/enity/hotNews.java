// @author:樊川
// @email:945001786@qq.com
package com.enity;

import lombok.Data;

@Data
public class hotNews {
    private Long id;
    private String title;
    private String rank;
    private String hotCount;
    private String url;
    private Long gmtCreate;
}
