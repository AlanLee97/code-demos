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
