package com.stumdet.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * pojo: User
 * Author: Jiahao Zhang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String uid;
    private String uname;
    private String pwd;
    private Timestamp dateJoined;
    private Timestamp lastLogin;
    private boolean isSuperuser;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isStaff;
    private boolean isActive;
}
