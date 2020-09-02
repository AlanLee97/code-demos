package top.alanlee.template.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.alanlee.template.util.FtpFileUtil;
import top.alanlee.template.util.IDUtils;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
 
@RestController
@RequestMapping("/upload")
public class UploadController {

    // 文件在服务器端保存的主目录
    @Value("${ftp.basepathfile}")
    private String basePath;

    // 访问文件时的基础url
    @Value("${file.base.url}")
    private String baseUrl;

    @Autowired
    private FtpFileUtil ftpFileUtil;

    @PostMapping("/file")
    public void fileUpload(@RequestParam("file") MultipartFile uploadFile) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("/yyyy/MM/dd");
            //获取图片名字
            String oldName = uploadFile.getOriginalFilename();
            //创建新的名字
            String newName = IDUtils.getImageName();
            newName = newName + oldName.substring(oldName.lastIndexOf("."));
            //生成文件在服务器端存储的子目录
            String filePath = simpleDateFormat.format(new Date());
            //把文件上传到服务器
            //转io流
            InputStream inputStream = uploadFile.getInputStream();

            boolean result = ftpFileUtil.uploadFile(filePath, newName, basePath, inputStream);
            if (result) {
                System.out.println("文件地址：" + baseUrl + filePath + "/" + newName);
            } else {
                System.out.println("文件上传失败");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}