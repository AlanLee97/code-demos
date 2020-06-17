package top.alanlee.template.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.alanlee.template.entity.dto.ApiJson;
import top.alanlee.template.entity.Audience;
import top.alanlee.template.service.impl.UserServiceImpl;
import top.alanlee.template.util.JwtTokenUtil;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
@Api(value = "/user", tags = "用户接口")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private Audience audience;

    /**
     * 获取所有用户，返回json数据
     * @return 返回json数据
     */
    @RequestMapping("/get/all")
    @ApiOperation(value = "查询所有用户")
    public ApiJson getAllUsers(){
//        List<User> allUsers = userService.getAll();
//        return allUsers != null ? ApiJson.ok(allUsers) : ApiJson.error();
        return ApiJson.ok("get ok");
    }

    @PostMapping("/login")
    public ApiJson login(HttpServletResponse response, String username, String password){
        String role = "admin";
        Integer userId = 1;

        try {
            String token = JwtTokenUtil.createJwt(userId, username, role, audience);
            response.setHeader(JwtTokenUtil.AUTH_HEADER_KEY, JwtTokenUtil.TOKEN_PREFIX + token);
            return ApiJson.ok(token);
        } catch (Exception e) {
            //e.printStackTrace();
            return ApiJson.error("未登录，请先登录");
        }

    }
}
