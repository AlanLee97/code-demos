package top.alanlee.template.base;

import java.util.List;

public interface BaseCRUD<T> {
    //添加
    int add(T t);

    //删除
    int delete(int id);

    //更新
    int update(T t);

    //通过ID查询单个数据
    T getOneById(int id);

    //查询所有数据
    List<T> getAll();

}
