// @author:樊川
// @email:945001786@qq.com
package com.provider;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.utility.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class QFileProvider {
    @Value("${qcloud.qfile.SecretId}")
    private String SECRETID;
    @Value("${qcloud.qfile.SecretKey}")
    private String SECRETKEY;
    @Value("${qcloud.qfile.bucketName}")
    private String BUCKET;
    @Value("${qcloud.qfile.region}")
    private String REGION;

    Region region = new Region("ap-nanjing");
    String bucketName = "community-1304870863";

    // 创建 COSClient 实例，这个实例用来后续调用请求
    COSClient createCOSClient() {
        // 设置用户身份信息。
        // SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        String secretId = SECRETID;
        String secretKey = SECRETKEY;
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

        // ClientConfig 中包含了后续请求 COS 的客户端设置：
        ClientConfig clientConfig = new ClientConfig();

        // 设置 bucket 的地域
        // COS_REGION 请参照 https://cloud.tencent.com/document/product/436/6224
        clientConfig.setRegion(region);

        // 设置请求协议, http 或者 https
        // 5.6.53 及更低的版本，建议设置使用 https 协议
        // 5.6.54 及更高版本，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);

        // 以下的设置，是可选的：

        // 设置 socket 读取超时，默认 30s
        clientConfig.setSocketTimeout(30*1000);
        // 设置建立连接超时，默认 30s
        clientConfig.setConnectionTimeout(30*1000);

        // 如果需要的话，设置 http 代理，ip 以及 port
//        clientConfig.setHttpProxyIp("httpProxyIp");
//        clientConfig.setHttpProxyPort(80);

        // 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }

    // 创建 TransferManager 实例，这个实例用来后续调用高级接口
    TransferManager createTransferManager() {
        COSClient cosClient = createCOSClient();
        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);

        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);

        // 设置高级接口的配置项
        // 分块上传阈值和分块大小分别为 5MB 和 1MB
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(5*1024*1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);

        return transferManager;
    }

    public String upload(MultipartFile multiFile) throws FileNotFoundException {
        File localFile = FileUtils.MultipartFileToFile(multiFile);
        System.out.println(localFile.getPath());
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String key = "img/"+localFile.getName();
        COSClient cosClient = createCOSClient();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        cosClient.putObject(putObjectRequest);
        URL url = cosClient.getObjectUrl(bucketName, key);

        // 删除本地的临时文件
        localFile.delete();

        return url.toString();
    }

    public void delete(String localFilePath) {
        // 指定被删除的文件在 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示删除位于 folder 路径下的文件 picture.jpg
        COSClient cosClient = createCOSClient();
        cosClient.deleteObject(bucketName, localFilePath);
    }
}
