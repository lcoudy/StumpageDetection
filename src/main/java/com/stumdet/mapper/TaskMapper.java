package com.stumdet.mapper;

import com.stumdet.pojo.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 仓库和任务一一对应
 */
@Mapper
@Repository
public interface TaskMapper {
    // select
    List<Task> getTaskByPid(String pid);    // 获取项目下的所有odm任务
    Task getTaskByRid(String rid);  // 获得一个仓库对应的odm任务
    Task getTaskByTid(String tid);  // 根据odm任务唯一标识，获得odm任务
    Task getTaskByName(String tname);
    List<Task> getAllTasks();

    // insert
    void createTask(Task task); // 创建一个任务

    // update
    void updateTaskInfo(Map<String, Object> info);  // 更新进度、状态等

    // delete
    void deleteTaskByTid(String tid);
    void deleteTaskByRid(String rid);
    void deleteTaskByPid(String pid);
    void deleteTaskByName(String tname);

}
