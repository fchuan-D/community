// @author:樊川
// @email:945001786@qq.com
package com.enity;

import lombok.Data;

@Data
public class accessToken {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
