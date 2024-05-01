package com.stumdet.mapper;

import com.stumdet.pojo.Project;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ProjectMapper {
    // select
    Project getProjectByPid(String pid);
    List<Project> getProjectByUid(String uid);
    Project getProjectByPname(String pname);

    // insert
    void createProject(Project project);

    // delete
    void deleteProjectByPid(String pid);

    // update:
    // pid, name, description
    void updateProject(Map<String, Object> params);

}
