package top.alanlee.template.service;

import top.alanlee.template.base.BaseCRUD;
import top.alanlee.template.entity.User;

public interface UserService extends BaseCRUD<User> {
    User login(String username, String password);
}
