# SpringBoot+Vue前后端分离集成JWT

>  源代码：https://github.com/AlanLee97/code-demos/tree/master/jwt

## 一、搭建SpringBoot项目

1. 添加jwt依赖

   ```xml
   <!-- ======BEGIN jwt ====== -->
   <dependency>
       <groupId>com.auth0</groupId>
       <artifactId>java-jwt</artifactId>
       <version>3.10.3</version>
   </dependency>
   
   <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt</artifactId>
       <version>0.9.1</version>
   </dependency>
   <!-- ======END jwt ====== -->
   ```



2. 编码

目录结构

![image-20200906223514064](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200906223514064-1599405347357.png)



JwtConfigProperties.java

```java
package top.alanlee.template.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置属性类
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties{
    private long expire;
    private String secret;

    public JwtConfigProperties() {
    }

    public JwtConfigProperties(long expire, String secret) {
        this.expire = expire;
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "JwtConfigProperties{" +
                "expire=" + expire +
                ", secret='" + secret + '\'' +
                '}';
    }
}
```

JwtConfigProperties是自定义的配置类，创建了这个类之后就可以在application.yml添加配置了

```yml
# 自定义的jwt配置
jwt:
  # 过期时间：2小时
  expire: 7200000
  # 密钥
  secret: 6Dx8SIuaHXJYnpsG18SSpjPs50lZcT52
```



JWTUtil.java

```java
package top.alanlee.template.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import top.alanlee.template.config.JwtConfigProperties;

import java.util.Date;

public class JWTUtil {
    /**
     * 校验token是否正确
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, JwtConfigProperties jwtConfigProperties) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfigProperties.getSecret());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println(jwt);
            return true;
        } catch (TokenExpiredException tokenExpiredException){
            throw new Exception(JwtVerifyConst.EXPIRED);
        } catch (SignatureVerificationException signatureVerificationException){
            System.out.println("token验证失败");
            throw new Exception(JwtVerifyConst.SIGNATURE_VERIFICATION);
        } catch (JWTDecodeException jwtDecodeException){
            System.out.println("token解析失败");
            throw new Exception(JwtVerifyConst.DECODE_ERROR);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new Exception(JwtVerifyConst.NOT_LOGIN);
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     * @param username 用户名
     * @return 加密的token
     */
    public static String sign(String username, JwtConfigProperties jwtConfigProperties) {
        Date date = new Date(System.currentTimeMillis()+jwtConfigProperties.getExpire());
        Algorithm algorithm = Algorithm.HMAC256(jwtConfigProperties.getSecret());
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }
}

/**
 * 验证结果常量
 */
class JwtVerifyConst{
    public static String SUCCESS = "token验证成功";
    public static String EXPIRED = "token已过期";
    public static String SIGNATURE_VERIFICATION = "token签名失败";
    public static String DECODE_ERROR = "token解析失败，请重新登录获取token";
    public static String NOT_LOGIN = "未登录";
}

```



JwtInterceptor.java

```java
package top.alanlee.template.interceptor;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.alanlee.template.config.JwtConfigProperties;
import top.alanlee.template.util.JWTUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtConfigProperties jwtConfigProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 这里是个坑，因为带请求带headers时，ajax会发送两次请求，
        // 第一次会发送OPTIONS请求，第二次才会发生get/post请求，所以要放行OPTIONS请求
        // 如果是OPTIONS请求，让其响应一个 200状态码，说明可以正常访问
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            // 放行OPTIONS请求
            return true;
        }

        // JwtConfigProperties通过@Autowired的值为null，所以需要通过以下方法获取bean
        if(jwtConfigProperties == null){
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            jwtConfigProperties = (JwtConfigProperties) factory.getBean("jwtConfigProperties");
        }
        boolean verify = false;
        String errMsg = "";
        // 获取header中的token
        String token = request.getHeader("Authorization");
        try {
            verify = JWTUtil.verify(token, jwtConfigProperties);
        } catch (Exception e) {
            errMsg = e.getMessage();
            e.printStackTrace();
        }
        // 验证token，如果验证失败就重定向到未登录页面
        if (!verify){
            errMsg = URLEncoder.encode(errMsg, "UTF-8");
            //这里是个坑，在重定向这里需要设置跨域，不然vue前端会报跨域问题
            response.setHeader("Access-Control-Allow-Origin", "*");
            // 重定向到未登录提示页面
            response.sendRedirect("/user/unlogin?msg=" + errMsg);
            return false;
        }
        return true;
    }
}
```



WebConfig.java

```java
package top.alanlee.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.alanlee.template.interceptor.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //配置跨域请求
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*");
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new JwtInterceptor());
        // 需拦截的路径
        interceptorRegistration.addPathPatterns("/**");
        // 需放行的路径
        interceptorRegistration.excludePathPatterns("/**/login", "/**/unlogin");
    }
}

```



UserController.java

```java
package top.alanlee.template.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.alanlee.template.config.JwtConfigProperties;
import top.alanlee.template.dto.ApiJson;
import top.alanlee.template.entity.User;
import top.alanlee.template.service.impl.UserServiceImpl;
import top.alanlee.template.util.JWTUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(value = "/user", tags = "用户接口")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtConfigProperties jwtConfigProperties;


    @PostMapping("/login")
    public ApiJson login(String username, String password){
        User user = userService.login(username, password);
        if (user != null){
            // 登录成功，返回token
            return ApiJson.ok("登录成功", JWTUtil.sign(username, jwtConfigProperties));
        }else {
            return ApiJson.error("登录失败");
        }
    }

    /**
     * 未登录，没有token会重定向到这个方法来
     * @param msg
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/unlogin")
    public ApiJson unlogin(String msg) throws UnsupportedEncodingException {
        return ApiJson.error(new String(msg.getBytes(), "UTF-8"));
    }

    /**
     * 获取所有用户，返回json数据，访问时需要在header中带上token（headers加上Authorization: token内容）
     * @return 返回json数据
     */
    @RequestMapping("/get/all")
    @ApiOperation(value = "查询所有用户")
    public ApiJson getAllUsers(){
        List<User> allUsers = userService.getAll();
        return allUsers != null ? ApiJson.ok(allUsers) : ApiJson.error();
    }


}

```



3. 总结下流程

- 用户为未登录的情况下访问需认证的模块
- 我们设置了jwt拦截器，会把用户拦截下来进行jwt的认证
- 第一次访问，用户没有token，后端会转发到未登录的页面`/user/unlogin`中，并提示相关信息
- 用户根据提示去登录
- 登录成功之后，后端会颁发token给用户
- 用户拿到token之后，在前端把token存储起来（存到`localStorage`）
- 用户再次访问需认证的模块时，在headers中带上token就可以访问数据





二、搭建vue前端项目

1. 目录结构

   ![image-20200906225440478](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200906225440478.png)

2. 编码

index.js

```js
<template>
  <div>
    <img src="../assets/logo.png" alt="logo" width="100px" height="100px">

    <h1>JWT Demo</h1>

    <div class="al-flex-justify-space-around">
      <div class="al-p-20px al-box-container al-box-pretty" style="width: 500px; height: 300px">
        <p style="word-break: break-all; padding: 0 10px; height: 250px" >{{result === null ? "无结果" : result}}</p>

        <el-button @click="getNetworkData">发起网络请求（需认证）</el-button>
      </div>

      <div class="al-p-20px al-box-container al-box-pretty" style="width: 500px; height: 300px">
        <h3>登录</h3>
        <div class="al-flex-container al-flex-direction-col">
          <div class="al-flex-container al-flex-container-center-v">
            <div style="width: 60px">帐号：</div>
            <el-input v-model="formData.username"/>
          </div>
          <div style="height: 20px"></div>
          <div class="al-flex-container al-flex-container-center-v">
            <div style="width: 60px">密码：</div>
            <el-input v-model="formData.password"/>
          </div>
        </div>
        <el-button @click="login" type="primary">登录</el-button>
      </div>
    </div>

  </div>
</template>

<script>
  import {request} from "@/util/network/request";
  import qs from "qs";

  export default {
    name: "Index",
    //组件
    components: {},
    //属性
    props: {},

    //数据
    data() {
      return {
        result: null,
        formData: {
          username: "",
          password: ""
        }
      }
    },

    //挂载完成时
    mounted() {
      //this.getNetworkData();
      localStorage.setItem("token", "")
    },

    //方法
    methods: {
      getNetworkData() {
        request({
          url: "http://localhost:8080/user/get/all",
          data: qs.stringify(this.data),
          headers:{
            "Authorization": localStorage.getItem("token")
          }
        }).then(res => {
          console.log(res);
          this.result = res.data;
        }).catch(err => {
          console.log(err)
        })
      },

      login(){
        let data = {
          username: this.formData.username,
          password: this.formData.password,
        }
        console.log(data);

        request({
          url: "http://localhost:8080/user/login",
          method: 'post',
          data: qs.stringify(data)
        }).then(res => {
          console.log(res);
          this.result = res.data;
          localStorage.setItem("token", res.data.data);
        }).catch(err => {
          console.log(err)
        })
      }
    },

    //计算属性
    computed: {},

    //监听
    watch: {}
  }
</script>

<style scoped>

</style>

```



网络请求用axios

request.js

```js
import axios from 'axios';

let ip = "localhost";
let port = "8080";
let prefix = "http://" + ip + ":" + port + "/";

export function request(config) {
  const instance = axios.create({
    baseURL: prefix,
    timeout: 100000
  });

  return instance(config);
}

```



3. 前端界面

![image-20200906225756774](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200906225756774.png)



三、测试

1. 未登录情况下进行发起网络请求

![image-20200906230059220](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200906230059220.png)

2. 登录获取token

![image-20200906230441571](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200906230441571.png)

3. 再次点击发起网络请求，就可以获取到数据了

![image-20200906230649729](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200906230649729.png)



需要在headers中带上Authorization：token内容

![image-20200906230833875](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200906230833875.png)

![image-20200906231047636](https://gitee.com/AlanLee97/assert/raw/master/note_images/image-20200906231047636.png)