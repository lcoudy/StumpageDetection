package com.stumdet.mapper;

import com.stumdet.pojo.ImageRepo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Interface: ImageRepoMapper
 * Author: Yuyang Zhao
 * 上传记录信息持久层接口
 * 实现请参见resources.mybatis.mapper.ImageRepoMapper.xml
 *
 * 仓库（repo）是由用户上传影像文件后保存文件的位置
 * 在数据库中表现为逻辑仓库信息
 * 在运行环境中表现为具体的文件夹
 * 一般认为上传后的记录就不再改变（也许还包含我没想到的情况）,所以不添加修改功能
 */
@Mapper
@Repository
public interface ImageRepoMapper {

    // 查询特定的rid对应的仓库
    ImageRepo getRepoById(String rid);

    // 查询用户所拥有的仓库
    List<ImageRepo> getRepoByUser(String uid);

    // 添加一个仓库记录
    void createRepo(ImageRepo repo);

    // 删除一个仓库
    void deleteRepo(String rid);

    //修改仓库数据
    void updateRepoInfo(Map<String, Object> param);

}
 