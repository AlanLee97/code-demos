package top.alanlee.demo.dubbo.provider.user.service.impl;

import org.apache.dubbo.config.annotation.Service;
import top.alanlee.demo.dubbo.consumer.UserService;

@Service(version = "${service.user.version}", interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello() {
        return "Hello Dubbo!";
    }
}
