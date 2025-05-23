package com.fengzhi.event_manager.controller;

import com.fengzhi.event_manager.pojo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
public class FileLoadController {

    @Value("${file.upload-path}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result<String> updateFile(@RequestParam("file") MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        file.transferTo(new File(uploadPath+fileName));
        //文件上传到阿里云的OSS
//        String url =  AliOSSUtil.uploadFile(fileName,file.getInputStream());
        //文件存储到本地，采用静态资源映射进行获取，拦截器对该资源的访问放行
        return Result.success("/images/"+fileName);
    }
}
