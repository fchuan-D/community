// @author:樊川
// @email:945001786@qq.com
package com.controller;

import com.dto.FileDTO;
import com.exception.CustomizeException;
import com.exception.ErrorCode;
import com.provider.QFileProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@Slf4j
public class FileController {

    @Resource
    QFileProvider qFileProvider;


    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) throws IOException {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multiFile = multipartRequest.getFile("editormd-image-file");
        String url = qFileProvider.upload(multiFile);
        if (url!=null){
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(url);
            fileDTO.setMessage("上传成功");
            return fileDTO;
        }else{
            throw new CustomizeException(ErrorCode.FILE_UPLOAD_FAIL);
        }
    }
}
