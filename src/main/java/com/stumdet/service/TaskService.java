package com.stumdet.service;

import com.stumdet.mapper.TaskMapper;
import com.stumdet.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import java.util.List;
import java.util.Map;

@Service
public class TaskService implements TaskMapper {

    @Autowired
    TaskMapper taskMapper;

    @Override
    public List<Task> getAllTasks() {
        return taskMapper.getAllTasks();
    }

    @Override
    public List<Task> getTaskByPid(String pid) {
        return taskMapper.getTaskByPid(pid);
    }

    @Override
    public Task getTaskByRid(String rid) {
        return taskMapper.getTaskByRid(rid);
    }

    @Override
    public Task getTaskByTid(String tid) {
        return taskMapper.getTaskByTid(tid);
    }

    @Override
    public Task getTaskByName(String name) {
        return taskMapper.getTaskByName(name);
    }

    /**
     * 保证仓库和任务一一对应性
     *
     * @param task
     */
//    @Override
//    public void createTask(Task task) {
//
//        Task existingTask = taskMapper.getTaskByRid(task.getRid());
//        if(existingTask != null){
//            //System.out.println("Task id: " + existingTask.getTid() + "has been deleted");
//            taskMapper.deleteTaskByRid(task.getRid());
//        }
//        System.out.println("task:"+task);
//        taskMapper.createTask(task);
//    }
    @Override
    public void createTask(Task task) {
        Task existingTask = taskMapper.getTaskByRid(task.getRid());
    if (existingTask != null) {
        taskMapper.deleteTaskByRid(task.getRid());  // 删除已存在的任务
    }

    // 生成唯一的tid
    String uniqueID = UUID.randomUUID().toString();
    task.setTid(uniqueID);  // 设置新的唯一任务ID

    // 设置任务的进度、状态和更新时间
    task.setProcess(10.0); // 设置进度为10
    task.setStatus("运行中"); // 设置状态为“运行中”
    task.setUpdateTime(Timestamp.valueOf(LocalDateTime.now())); // 设置当前时间为更新时间

    taskMapper.createTask(task);  // 创建新任务
    }


    @Override
    public void updateTaskInfo(Map<String, Object> info) {
        taskMapper.updateTaskInfo(info);
    }

    @Override
    public void deleteTaskByTid(String tid) {
        taskMapper.deleteTaskByTid(tid);
    }

    @Override
    public void deleteTaskByRid(String rid) {
        taskMapper.deleteTaskByRid(rid);
    }

    @Override
    public void deleteTaskByName(String tname) {
        taskMapper.deleteTaskByName(tname);
    }

    @Override
    public void deleteTaskByPid(String pid) {
        taskMapper.deleteTaskByPid(pid);
    }
}
