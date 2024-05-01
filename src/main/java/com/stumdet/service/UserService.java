package com.stumdet.service;

import com.stumdet.mapper.UserMapper;
import com.stumdet.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service: UserService
 * Author: Jiahao Zhang
 * Checker: Yuyang Zhao
 * 用户操作Service层
 */
@Service
public class UserService implements UserMapper{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(String uid) {
        User user = this.userMapper.getUserById(uid);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = this.userMapper.getAllUsers();
        return users;
    }

    @Override
    public User getUserByName(String uname) {
        User user = this.userMapper.getUserByName(uname);
        return user;
    }


    @Override
    public void addUser(User newUser) {
        this.userMapper.addUser(newUser);
    }

    @Override
    public void changeSuperuser(Map<String, Object> map) {
        this.userMapper.changeSuperuser(map);
    }

    @Override
    public void changeStaff(Map<String, Object> map) {
        this.userMapper.changeStaff(map);
    }

    @Override
    public void changeActive(Map<String, Object> map) {
        this.userMapper.changeActive(map);
    }


    @Override
    public void updateUserPass(Map<String, Object> map) {
        this.userMapper.updateUserPass(map);
    }

    @Override
    public void updateUserInfo(Map<String, Object> map) {
        this.userMapper.updateUserInfo(map);
    }

    @Override
    public void deleteUserById(String uid) {
        this.userMapper.deleteUserById(uid);
    }

    @Override
    public void deleteUserByName(String uname) {
        this.userMapper.deleteUserByName(uname);
    }
}
