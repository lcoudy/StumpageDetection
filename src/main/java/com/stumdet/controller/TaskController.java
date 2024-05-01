package com.stumdet.controller;

import com.stumdet.pojo.ImageRepo;
import com.stumdet.pojo.Task;
import com.stumdet.service.ImageRepoService;
import com.stumdet.service.TaskService;
import com.stumdet.utils.SqlDateTimeProducer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.RequestMethod;
import com.stumdet.dto.TaskDeleteRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.Console;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller: TaskController
 * Author: Yuyang Zhao
 * 由python falsk端进行调用的函数，保证数据库的一致性：
 * - createTask
 * - updateTask
 * - deleteTask
 * 由前端调用，进行数据显示
 * -
 */
@Controller
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ImageRepoService imageRepoService;

    //写死数据，后期需要修改
    private static List<String> can_rerun_from;

    static {
        can_rerun_from = new ArrayList<>();
        List<String> can_rerun_from = new ArrayList<String>();
        can_rerun_from.add("");
        can_rerun_from.add("dataset");
        can_rerun_from.add("split");
        can_rerun_from.add("merge");
        can_rerun_from.add("opensfm");
        can_rerun_from.add("openmvs");
        can_rerun_from.add("odm_filterpoints");
        can_rerun_from.add("odm_meshing");
        can_rerun_from.add("mvs_texturing");
        can_rerun_from.add("odm_georeferencing");
        can_rerun_from.add("odm_dem");
        can_rerun_from.add("odm_orthophoto");
        can_rerun_from.add("odm_report");
        can_rerun_from.add("odm_postprocess");
    }

//    @RequestMapping("/task/create")
//    @ResponseBody
//    @CrossOrigin
//    public Task createTask(HttpServletRequest request){
//        String tid = request.getParameter("tid");
//        String rid = request.getParameter("rid");
//        String pid = request.getParameter("pid");
//        String tname = request.getParameter("tname");
//        Task task = new Task(tid, rid, pid, tname, 0.0, "NULL", SqlDateTimeProducer.getCurrentDateTime());
//        try{
//            System.out.println("task"+task);
//            System.out.println("创建任务成功");
//            taskService.createTask(task);
//        }
//        catch(Exception e){
//            System.out.println("Creating task error");
//        }
//        return task;
//    }

    @RequestMapping(value = "/task/create", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Task createTask(@RequestBody Task task) {
        try {
            taskService.createTask(task);
        } catch (Exception e) {
            System.out.println("Creating task error");
            e.printStackTrace();  // 更好地了解异常
        }
        return task;
    }

    @RequestMapping("/task/update")
    @ResponseBody
    @CrossOrigin
    public void updateTask(HttpServletRequest request) {
        String tid = request.getParameter("tid");
        Double process = Double.parseDouble(request.getParameter("process"));
        String status = request.getParameter("status");
        Map<String, Object> info = new HashMap<>();
        info.put("tid", tid);
        info.put("updateTime", SqlDateTimeProducer.getCurrentDateTime());
        info.put("process", process);
        info.put("status", status);
        taskService.updateTaskInfo(info);
    }

//    @RequestMapping("/task/delete")
//    @ResponseBody
//    @CrossOrigin
//    public String deleteTask(HttpServletRequest request){
//        String tid = request.getParameter("tid");
//        System.out.println(tid);
//        this.taskService.deleteTaskByTid(tid);
//        return tid;
//    }

    @RequestMapping("/task/delete")
    @ResponseBody
    @CrossOrigin
    public String deleteTask(@RequestBody Map<String, Object> data) {
        String tid = (String) data.get("tid");
        this.taskService.deleteTaskByTid(tid);
        return tid;
    }

    @RequestMapping("/task/getall")
    @ResponseBody
    @CrossOrigin
    public List<TaskReturnStructure> getAllTasksByPid(@RequestBody Map<String, Object> params) {
        String pid = (String) params.get("pid");
        List<Task> tasks = taskService.getTaskByPid(pid);
        List<TaskReturnStructure> res = new ArrayList<>();
        for (Task task : tasks) {
            ImageRepo repo = imageRepoService.getRepoById(task.getRid());
            int count = repo.getCount();
            // TODO
            res.add(new TaskReturnStructure(task.getTid(), task.getPid(), -1, "UNKNOW",
                    this.can_rerun_from, new HashMap<String, Object>(), task.getTid(),
                    task.getTname(), -1, true, task.getStatus(), null, new HashMap<String, Object>(),
                    new ArrayList<String>(), task.getUpdateTime(), null, false, -1, 1, 0, task.getProcess(),
                    "", count, false, new HashMap<String, Object>(), null));
        }

        return res;
    }

    @RequestMapping("/task/getid")
    @ResponseBody
    @CrossOrigin
    public Object getTaskIdByName(@RequestBody Map<String, Object> params) {
        String tname = (String) params.get("tname");
        class Return {
            public String tid;

            Return(String tid) {
                this.tid = tid;
            }
        }
        String tid = this.taskService.getTaskByName(tname).getTid();
        return new Return(tid);
    }

    @RequestMapping("/task/getrid")
    @ResponseBody
    @CrossOrigin
    public Object getTaskRIdByTid(@RequestBody Map<String, Object> params) {
        String tid = (String) params.get("tid");
        class Return {
            public String rid;

            Return(String rid) {
                this.rid = rid;
            }
        }
        String rid = this.taskService.getTaskByTid(tid).getRid();
        return new Return(rid);
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class TaskReturnStructure {
    private String id;// 和下面的uuid一样-tid
    private String pid;// project的pid-pid
    private int processing_node;
    private String processing_node_name;
    private List<String> can_rerun_from;
    private Map<String, Object> statistics;
    private String uuid;//-tid
    private String name;//-tname
    private int processing_time;
    private boolean auto_processing_node;
    private String status;//-status
    private String last_error;
    private Map<String, Object> options;
    private List<String> available_assets;
    private Timestamp create_at;//-createTime
    private String pending_action;
    private boolean pub1ic;
    private int resize_to;
    private int upload_progress;
    private int resize_progress;
    private Double running_progress;//-process
    private String import_url;
    private int images_count;
    private boolean partial;
    private Map<String, Object> potree_scene;
    private String epsg;
}
