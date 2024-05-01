package com.stumdet.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private String pid;
    private String uid;
    private String pname;
    private String description;
    private Timestamp createTime;
}
