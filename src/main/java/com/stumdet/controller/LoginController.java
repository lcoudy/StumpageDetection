package com.stumdet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stumdet.pojo.User;
import com.stumdet.service.JWTService;
import com.stumdet.service.UserService;
import com.stumdet.utils.SqlDateTimeProducer;
import com.stumdet.utils.UUIDProducer;
import com.stumdet.utils.UUIDType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    /**
     * Request Parameters:
     * - uname
     * - pwd
     * @param 
     * @return jwt
     * 返回的字段都是String类型，在前端需要进行转换
     * status->Boolean()
     */
    @RequestMapping("/token-auth")
    @ResponseBody
    @CrossOrigin
    public LoginStatus login(@RequestBody Map<String, Object> map, HttpServletResponse response) throws JsonProcessingException {
        int status = 200;
        Map<String, Object> userInfo = new HashMap<>();

        String uname = (String)map.get("uname"); //request.getParameter("uname");
        String pwd = (String)map.get("pwd");//request.getParameter("pwd");
        System.out.println(uname +  pwd);
        User loginUser = this.userService.getUserByName(uname);

        // 判断密码是否正确
        boolean success = false;
        Map<String, Object> claims = new HashMap<>();

        if(loginUser == null){
            success = false;
            status = 400;
        }
        else{
            success = pwd.equals(loginUser.getPwd()) ? true : false;
            status = success ? 200 : 400;

        }


        claims.put("status", success);
        response.setStatus(status);
        String token = null;

        if(success){
            claims.put("uname", loginUser.getUname());
            claims.put("uid", loginUser.getUid());
            userInfo.put("uname", loginUser.getUname());
            userInfo.put("uid", loginUser.getUid());
            token = jwtService.getJWT(claims, 24 * 60 * 60);
            System.out.print(token);
        }
        else{
            token = null;
        }

        return new LoginStatus(status, success, userInfo, token, loginUser);
    }

    @RequestMapping("/admin/users")
    @ResponseBody
    @CrossOrigin
    public UsersReturn adminusers(){
        List<UserReturn> userReturnList = new ArrayList<>();
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            userReturnList.add(new UserReturn(user.getUid(), user.getPwd(), user.getLastLogin().toString(),
                    user.isSuperuser(), user.getUname(), user.getFirstName(), user.getLastName(),
                    user.getEmail(), user.isStaff(), user.isActive(), user.getDateJoined().toString(), new int[]{}, new String[]{}));
        }
        UsersReturn res = new UsersReturn(users.size(), null, null, userReturnList);

        return res;
    }

    @RequestMapping("/user/create")
    @ResponseBody
    @CrossOrigin
    public boolean createUser(@RequestBody Map<String, Object> map){
        String uname = (String)map.get("uname");
        String pwd = (String)map.get("pwd");
        String uid = new UUIDProducer.Builder()
                .setuType(UUIDType.User)
                .setUUIDLen(32).build().toString();
        User newUser = new User(uid, uname, pwd, SqlDateTimeProducer.getCurrentDateTime(),
                SqlDateTimeProducer.getCurrentDateTime(), false, "", "", "", false, true);
        try{
            userService.addUser(newUser);
        }catch(Exception e) {
            return false;
        }
        return true;
    }

    @RequestMapping("/user/logout")
    @ResponseBody
    @CrossOrigin
    public boolean userLogout(@RequestBody Map<String, Object> map){
        //TODO
        return true;
    }

    @RequestMapping("/user/update/superuser")
    @ResponseBody
    @CrossOrigin
    public boolean changeSuperuser(@RequestBody Map<String, Object> map){
        String uid = (String)map.get("uid");
        boolean isSuperuser = Boolean.parseBoolean((String)map.get("isSuperuser"));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("uid", uid);
        params.put("isSuperuser", isSuperuser);
        try {
            userService.changeSuperuser(params);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    @RequestMapping("/user/update/active")
    @ResponseBody
    @CrossOrigin
    public boolean changeActive(@RequestBody  Map<String, Object> map){
        String uid = (String)map.get("uid");
        boolean isActive = Boolean.parseBoolean((String)map.get("isActive"));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("uid", uid);
        params.put("isActive", isActive);
        try {
            userService.changeActive(params);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    @RequestMapping("/user/update/pwd")
    @ResponseBody
    @CrossOrigin
    public boolean changePwd(@RequestBody  Map<String, Object> map){
        String uid = (String)map.get("uid");
        String pwd = (String)map.get("pwd");
        String uname = (String)map.get("uname");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("uid", uid);
        params.put("pwd", pwd);
        params.put("uname", uname);
        try {
            userService.updateUserPass(params);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    @RequestMapping("/user/update/userinfo")
    @ResponseBody
    @CrossOrigin
    public boolean changeUserInfo(@RequestBody  Map<String, Object> map){
        String uid = (String)map.get("uid");
        String firstName = (String)map.get("firstname");
        String lastName = (String)map.get("lastname");
        String email = (String)map.get("email");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("uid", uid);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("email", email);
        try {
            userService.updateUserPass(params);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }


}

@Data
@AllArgsConstructor
class LoginStatus{
    public int status;
    public Boolean msg;
    public Map<String, Object> userInfo;
    public String token;
    public User user;
}

@Data
@AllArgsConstructor
class UsersReturn{
    public int count;
    public UserReturn next=null;
    public UserReturn previous=null;
    public List<UserReturn> results;
}
@Data
@AllArgsConstructor
class UserReturn{
    public String id;
    public String password;
    public String last_login=null;
    public boolean is_superuser;
    public String username;
    public String first_name;
    public String last_name;
    public String email="";
    public boolean is_staff;
    public boolean is_active;
    public String date_joined;
    public int[] groups;
    public String[] user_permissions;
}
