package top.alanlee.template.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.alanlee.template.base.BaseCRUD;
import top.alanlee.template.entity.User;

@Mapper
@Repository
public interface UserMapper extends BaseCRUD<User> {

}
