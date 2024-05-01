package com.stumdet.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String tid;
    private String rid;
    private String pid;
    private String tname;
    private Double process;
    private String status;
    private Timestamp updateTime;
}
