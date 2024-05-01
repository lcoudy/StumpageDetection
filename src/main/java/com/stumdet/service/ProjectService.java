package com.stumdet.service;

import com.stumdet.mapper.ProjectMapper;
import com.stumdet.mapper.TaskMapper;
import com.stumdet.pojo.Project;
import com.stumdet.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectService implements ProjectMapper {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public void updateProject(Map<String, Object> params) {
        this.projectMapper.updateProject(params);
    }

    @Override
    public Project getProjectByPid(String pid) {
        return this.projectMapper.getProjectByPid(pid);
    }

    @Override
    public List<Project> getProjectByUid(String uid) {
        return this.projectMapper.getProjectByUid(uid);
    }

    @Override
    public void createProject(Project project) {
        this.projectMapper.createProject(project);
    }

    @Override
    public void deleteProjectByPid(String pid) {
        this.taskMapper.deleteTaskByPid(pid);
        this.projectMapper.deleteProjectByPid(pid);
    }

    @Override
    public Project getProjectByPname(String pname) {
        return this.projectMapper.getProjectByPname(pname);
    }
}
