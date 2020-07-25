package top.alanlee.demo.dubbo.consumer.user.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.alanlee.demo.dubbo.consumer.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(version = "${service.user.version}")
    private UserService userService;

    @GetMapping("/hello")
    public String sayHello(){
        return userService.sayHello();
    }


}
