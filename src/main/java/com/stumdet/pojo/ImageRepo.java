package com.stumdet.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * pojo: ImageRepo
 * Author: YuYang Zhao
 * Properties:
 *  rid -> varchar(64): 影像仓库id
 *  uid -> varchar(64): 用户外键，上传人
 *  count -> int: 图像数量
 *  repoLocation(repo_location) -> varchar(256): 影像存放的位置
 *  createTime(create_time) -> Timestamp: 创建时间，即上传时间
 *  size -> int: 文件夹总大小，单位（MB）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRepo {
    private String rid;
    private String uid;
    private String repoLocation;
    private Timestamp createTime;
    private int count;
    private int size;
}
