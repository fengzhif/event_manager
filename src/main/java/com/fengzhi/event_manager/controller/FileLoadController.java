package com.fengzhi.event_manager.controller;

import com.fengzhi.event_manager.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
public class FileLoadController {

    @PostMapping("/upload")
    public Result<String> updateFile(@RequestParam("file") MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        file.transferTo(new File("C:\\tmp\\t\\tmpfile\\"+fileName));
//        String url =  AliOSSUtil.uploadFile(fileName,file.getInputStream());
        return Result.success("file url in OSS....");
    }
}
