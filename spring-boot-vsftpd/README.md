# Spring Boot集成FTP文件上传服务

> 源码地址：

文件服务器使用的是vsftpd

## 一、Docker搭建vsftpd文件服务器

1. 创建用户组

```
groupadd ftpgroups
```

2. 创建用户

```sh
useradd -d /home/ftp/ftptest -g ftpgroups ftptest
```

3. 设置用户密码

```sh
passwd ftptest
123456
```

4. 设置不允许登录

```sh
usermod -s /sbin/nologin ftptest
```

5. 创建file文件夹存放文件

```sh
mkdir -p /home/ftp/ftptest/file
```

6. 修改文件夹权限（否则文件上传不上来）

```sh
chmod 777 /home/ftp/ftptest/file
```

7. 下载镜像并运行容器

```sh
docker run -d \
-v /home/ftp:/home/vsftpd \
-p 20:20 \
-p 21:21 \
-p 21100-21110:21100-21110 \
-e FTP_USER=ftptest \
-e FTP_PASS=123456 \
--name vsftpd fauria/vsftpd
```





## 二、搭建Spring Boot项目

1. 添加依赖

```xml
<!-- ======BEGIN 文件上传 ====== -->
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.3.2</version>
</dependency>

<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.4</version>
</dependency>

<dependency>
    <groupId>commons-net</groupId>
    <artifactId>commons-net</artifactId>
    <version>3.6</version>
</dependency>

<!-- ======END 文件上传 ====== -->
```



2. 编写代码

目录结构如下

![image-20200902103239181](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200902103239181.png)



VsftpdApplication.java

```java
package top.alanlee.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VsftpdApplication {
    public static void main(String[] args) {
        SpringApplication.run(VsftpdApplication.class, args);
    }
}
```



application.yml

```yml
server:
  port: 8080

spring:
  #配置文件上传器
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB

#ftp相关配置
ftp:
  address: 192.168.3.100
  port: 21
  username: ftptest
  password: 123456
  basepathfile: /file

#文件地址
file:
  base:
    url: http://192.168.3.100/file
```



FtpFileUtil.java

```java
package top.alanlee.template.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FtpFileUtil {

    @Value("${ftp.address}")
    private String host;
    // 端口
    @Value("${ftp.port}")
    private int port;
    // ftp用户名
    @Value("${ftp.username}")
    private String userName;
    // ftp用户密码
    @Value("${ftp.password}")
    private String passWord;

    /**
     * Description: 向FTP服务器上传文件
     * <p>
     * host     FTP服务器ip
     * port     FTP服务器端口
     * username FTP登录账号
     * password FTP登录密码
     * basePath FTP服务器基础目录,/file
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2018/05/28。文件的路径为basePath+filePath
     *
     * @param filename    上传到FTP服务器上的文件名
     * @param inputStream 输入流
     * @return 成功返回true，否则返回false
     */
    public boolean uploadFile(String filePath, String filename,String basePath, InputStream inputStream) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            // 连接FTP服务器
            ftp.connect(host, port);
            // 登录
            ftp.login(userName, passWord);
            int reply;
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            //切换到上传目录
            boolean changed = ftp.changeWorkingDirectory(filePath + filePath);
            if (!changed) {
                //如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)){
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            System.out.println("当前目录" + ftp.printWorkingDirectory());
            //设置为被动模式
            // ftp.enterLocalPassiveMode();
            ftp.setControlEncoding("UTF-8");
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //上传文件
            if (!ftp.storeFile(new String(filename.getBytes("UTF-8"), "ISO-8859-1"), inputStream)) {
                System.out.println("上传文件失败");
                return result;
            }

            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    inputStream.close();
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
}
```



IDUtils.java

```java
package top.alanlee.template.util;

import java.util.Random;
 
public class IDUtils {
    public static String getImageName() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0
        String str = millis + String.format("%03d", end3);
 
        return str;
    }
}
```



UploadController.java

```java
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
```



WebConfig.java（可以不配置）

```java
package top.alanlee.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //配置跨域请求
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*");
    }
}
```



## 三、通过Nginx配置虚拟主机服务访问文件

**使用docker-compose方式安装Nginx**

1. 创建目录

```sh
mkdir -p /usr/local/docker/nginx
cd /usr/local/docker/nginx
```

2. 编写docker-compose.yml文件

```sh
vim docker-compose.yml
```

docker-compose.yml

```yml
version: '3.1'
services:
  nginx:
    restart: always
    image: nginx
    container_name: nginx
    ports:
     - 80:80
    volumes:
     - ./conf/nginx.conf:/etc/nginx/nginx.conf
	# 将ftp的目录映射到nginx中
     - /home/ftp/ftptest/file:/usr/share/nginx/wwwroot/file

```

4. 创建目录用于挂载配置文件

```sh
mkdir ./wwwroot
mkdir ./conf
vim ./conf/nginx.conf
```

nginx.conf

```conf
user  root;
worker_processes  1;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;
    server {
        listen       80;
        server_name  localhost;

        location / {
            root   /usr/share/nginx/wwwroot;
            index  index.html index.htm;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    }

}

```

5. 运行

```sh
docker-compose up -d
```



## 四、测试

使用postman测试文件上传

![image-20200902105704618](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200902105704618.png)

打开idea的控制台

![image-20200902105826182](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200902105826182.png)



浏览器访问该地址即可

![image-20200902110043599](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200902110043599.png)