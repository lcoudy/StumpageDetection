package com.stumdet.mapper;

import com.stumdet.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Interface: UserMapper
 * Author: Yuyang Zhao
 * 用户持久层接口
 * 实现请参见resources.mybatis.mapper.UserMapper.xml
 */
@Mapper
@Repository
public interface UserMapper {
    // 根据用户id查询用户
    User getUserById(String uid);

    // 查询所有用户
    List<User> getAllUsers();

    // 根据用户名查询用户
    User getUserByName(String uname);

    // 添加用户
    void addUser(User newUser);

    //改变用户superuser身份
    void changeSuperuser(Map<String, Object> map);

    //改变用户staff状态
    void changeStaff(Map<String, Object> map);

    //改变用户active状态
    void changeActive(Map<String, Object> map);

    // 更改param:user中uid指定的用户信息
    void updateUserInfo(Map<String, Object> map);

    // 更改param:user中uid指定的用户登入账号密码
    void updateUserPass(Map<String, Object> map);

    // 根据用户id删除用户
    void deleteUserById(String uid);

    // 根据用户名删除用户
    void deleteUserByName(String uname);

}
