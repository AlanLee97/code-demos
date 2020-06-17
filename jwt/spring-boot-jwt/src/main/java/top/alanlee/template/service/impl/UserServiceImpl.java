package top.alanlee.template.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.alanlee.template.entity.User;
import top.alanlee.template.mapper.UserMapper;
import top.alanlee.template.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    public int add(User user) {
        return 0;
    }

    public int delete(int id) {
        return 0;
    }

    public int update(User user) {
        return 0;
    }

    public User getOneById(int id) {
        return null;
    }

    public List<User> getAll() {
        return userMapper.getAll();
    }
}
