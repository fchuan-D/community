// @author:樊川
// @email:945001786@qq.com
package com.service;

import com.alibaba.fastjson.JSON;
import com.enity.accessToken;
import com.enity.giteeUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
public class giteeService {
    public String getAccessToken(accessToken accessToken) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessToken));
        Request request = new Request.Builder()
                .url("https://gitee.com/oauth/token?grant_type=authorization_code"
                        +"&code="+accessToken.getCode()
                        +"&client_id="+accessToken.getClient_id()
                        +"&redirect_uri="+accessToken.getRedirect_uri()
                        +"&client_secret="+accessToken.getClient_secret())
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String str1 = string.split(":")[1];
            String token = str1.split("\"")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public giteeUser getUser(String Token){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token="+Token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            giteeUser giteeUser = JSON.parseObject(string, giteeUser.class);
            return giteeUser;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

