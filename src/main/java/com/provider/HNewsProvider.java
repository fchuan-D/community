// @author:樊川
// @email:945001786@qq.com
package com.provider;

import com.enity.hotNews;
import com.exception.CustomizeException;
import com.exception.ErrorCode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class HNewsProvider {
    public List <hotNews> getHotNews(){
        List <hotNews> res = new ArrayList<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response;

        HttpGet request = new HttpGet("https://s.weibo.com/top/summary");
        // 设置请求头，将爬虫伪装成浏览器
        request.setHeader(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Mobile Safari/537.36"
        );
        request.setHeader(
                "cookie",
                "SUB=_2AkMV2Fapf8NxqwFRmPkSym3ja4t0yA7EieKjhKdyJRMxHRl-yT8Xqm4ttRB6Plh4RgET0cLnGr2rjOdvrEfrwCwLUaCX; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5rpNFwvNbw8zs30U8jhwxv; UOR=www.baidu.com,s.weibo.com,www.baidu.com; SINAGLOBAL=6761739168129.361.1652873636536; _s_tentry=www.baidu.com; Apache=3393860491845.5996.1655857363929; ULV=1655857363938:2:1:1:3393860491845.5996.1655857363929:1652873636541"
        );

        // 执行get请求，相当于在输入地址栏后敲回车键
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            throw new CustomizeException(ErrorCode.FAIL_GET);
        }

        // 4.判断响应状态为200，进行处理
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 5.获取响应内容
            HttpEntity httpEntity = response.getEntity();
            String html;
            try {
                html = EntityUtils.toString(httpEntity, "utf-8");
            } catch (IOException e) {
                throw new CustomizeException(ErrorCode.FAIL_SPIDER_PARSE);
            }

            // 下面是Jsoup展现自我的平台
            // 6.Jsoup解析html
            Document document = Jsoup.parse(html);
            // 像js一样，通过标签获取title
            Element item = document.selectFirst(".list_a");
            Elements items = item.getElementsByTag("li");
            int count = 0;
            for (Element tmp : items) {
                if (count == 0) {
                    count++;
                    continue;
                }
                hotNews hotNews = new hotNews();
                Elements textEle = tmp.select("a");
                String href = textEle.attr("href");
                String url = "https://s.weibo.com";
                url += href;
                String titleText = String.valueOf(textEle.select("span"));
                String[] split = titleText.split("<em>");
                String title = split[0].split("<span>")[1];
                String hotCount = split[1].split("</em>")[0];
                String rank = tmp.select("a").select("strong").text();

                hotNews.setGmtCreate(System.currentTimeMillis());
                hotNews.setUrl(url);
                hotNews.setTitle(title);
                hotNews.setHotCount(hotCount);
                hotNews.setRank(rank);

                res.add(hotNews);
                count++;
            }
        } else {
            throw new CustomizeException(ErrorCode.FAIL_SPIDER);
        }
        //6.关闭
        HttpClientUtils.closeQuietly(response);
        HttpClientUtils.closeQuietly(httpClient);
        return res;
    }
}
