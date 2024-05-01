package com.stumdet.controller;

import com.stumdet.pojo.Project;
import com.stumdet.pojo.Task;
import com.stumdet.service.ProjectService;
import com.stumdet.service.TaskService;
import com.stumdet.utils.SqlDateTimeProducer;
import com.stumdet.utils.UUIDProducer;
import com.stumdet.utils.UUIDType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    // 写死数据后期需要修改
    private static List<String> permissions;
    static {
        permissions = new ArrayList<String>();
        permissions.add("add");
        permissions.add("change");
        permissions.add("delete");
        permissions.add("view");
    }

    @RequestMapping("/project/getall")
    @ResponseBody
    @CrossOrigin
    public List<ProjectReturnStructure> getAllProjects(@RequestBody Map<String, Object> params){
        String uid = (String)params.get("uid");
        List<Project> prjs = projectService.getProjectByUid(uid);
        List<ProjectReturnStructure> res = new ArrayList<ProjectReturnStructure>();
        for (Project prj : prjs) {
            List<Task> tasks = taskService.getTaskByPid(prj.getPid());
            List<String> taskIds = new ArrayList<String>();
            for(Task task : tasks){
                taskIds.add(task.getTid());
            }
            res.add(new ProjectReturnStructure(prj.getPid(), taskIds, prj.getCreateTime(),
                    this.permissions, prj.getPname(), prj.getDescription()));
        }
        return res;
    }

    @RequestMapping("/project/get")
    @ResponseBody
    @CrossOrigin
    public ProjectReturnStructure getProjectById(@RequestBody Map<String, Object> params){
        String pid = (String)params.get("pid");
        Project prj = projectService.getProjectByPid(pid);
        List<Task> tasks = taskService.getTaskByPid(prj.getPid());
        List<String> taskIds = new ArrayList<String>();
        for(Task task : tasks){
            taskIds.add(task.getTid());
        }
        ProjectReturnStructure res = new ProjectReturnStructure(prj.getPid(), taskIds, prj.getCreateTime(),
                this.permissions, prj.getPname(), prj.getDescription());
        return res;
    }

    @RequestMapping("/project/create")
    @ResponseBody
    @CrossOrigin
    public Project createProjects(@RequestBody Map<String, Object> params){
        String uid = (String)params.get("uid");
        String pname = (String)params.get("pname");
        String description = (String)params.get("description");
        String pid = new UUIDProducer.Builder()
                .setUUIDLen(32)
                .setuType(UUIDType.ODMProject)
                .build().toString();
        Timestamp createTime = SqlDateTimeProducer.getCurrentDateTime();
        Project prj = new Project(pid, uid, pname, description, createTime);
        try {
            this.projectService.createProject(prj);
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Creating project error");
        }
        return prj;
    }

    @RequestMapping("/project/delete")
    @ResponseBody
    @CrossOrigin
    public String deleteProjects(@RequestBody Map<String, Object> params){
        String pid = (String)params.get("pid");
        try{
            this.projectService.deleteProjectByPid(pid);
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Deleting Peoject error");
        }

        return pid;
    }

    @RequestMapping("/project/edit")
    @ResponseBody
    @CrossOrigin
    public String editProjects(@RequestBody Map<String, Object> params){
        try{
            this.projectService.updateProject(params);
        }
        catch(Exception e){
            System.out.println("Deleting Peoject error");
            return "false";
        }

        return "true";
    }

}

/**
 * 临时返回类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class ProjectReturnStructure{
    private String id;// pid
    private List<String> tasks;// tid
    private Timestamp created_at;
    private List<String> permissions;
    private String name;
    private String description;
}
