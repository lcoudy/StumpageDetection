package com.stumdet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stumdet.pojo.Task;
import com.stumdet.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import com.sun.management.OperatingSystemMXBean;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

@Controller
@RequestMapping("/api")
public class CesiumController {

    @Value("${core.odm.tex-name}")
    private String texPath;

    @Value("${core.root}")
    private String root;

    @Value("${core.env.obj23dtiles-output}")
    private String tilesetPath;

    @Value("${core.env.tileset}")
    private String tilesetName;

    @Value("${core.yolo.detect-json}")
    private String jsonPath;

    @Value("${core.yolo.detect-res}")
    private String detectImg;

    @Value("${core.odm.output-location}")
    private String outputRoot;

    @Autowired
    private TaskService taskService;

    @RequestMapping("/cesium/tileset")
    @CrossOrigin
    @ResponseBody
    public CesiumModelInfo getCesiumModel(@RequestBody Map<String, Object> params){
        String rid = (String)params.get("rid");
        String tilesetLoc = "/odmoutput/" + rid + this.texPath + this.tilesetPath + this.tilesetName;
        return new CesiumModelInfo(tilesetLoc);
    }
    
    @RequestMapping("/cesium/info")
    @CrossOrigin
    @ResponseBody
    public TreeModel getCesiumInfo(@RequestBody Map<String, Object> params) throws IOException {
        String rid = (String)params.get("rid");
        String jsonLoc = this.outputRoot + rid + this.jsonPath;
        String json = "";
        ObjectMapper om = new ObjectMapper();
        TreeModel res = om.readValue(new File(jsonLoc), TreeModel.class);
        return res;
    }

    @RequestMapping("/system/info")
    @CrossOrigin
    @ResponseBody
    public SystemUsage getSystemInfo() {
        SystemUsage res = new SystemUsage();
        SystemInfo systemInfo = new SystemInfo();

        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        // 椎内存使用情况
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        File file = new File(root);
        res.setStorageSpace(file.getTotalSpace() / 1024.0 / 1024 / 1024);
        res.setStorageSpaceUsed((file.getTotalSpace() - file.getUsableSpace()) / 1024.0 / 1024 / 1024);
        res.setStorageSpaceUnUsed(file.getUsableSpace() / 1024.0 / 1024 / 1024);

        res.setMemorySpace(osmxb.getTotalPhysicalMemorySize() / 1024.0 / 1024 / 1024);
        res.setMemorySpaceUsed((osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / 1024.0 / 1024 / 1024);
        res.setMemorySpaceUnUsed(osmxb.getFreePhysicalMemorySize() / 1024.0 / 1024 / 1024);

        int taskQueueing = 0;
        int taskFailed = 0;
        int taskCompleted = 0;
        int taskCancled = 0;
        int taskRunning = 0;

        List<Task> tasks = taskService.getAllTasks();
        for (Task task : tasks) {
            switch(task.getStatus()){
                case "TaskStatus.QUEUED":
                    taskQueueing++;
                    break;
                case "TaskStatus.RUNNING":
                    taskRunning++;
                    break;
                case "TaskStatus.FAILED":
                    taskFailed++;
                    break;
                case "TaskStatus.COMPLETED":
                    taskCompleted++;
                    break;
                case "TaskStatus.CANCELED":
                    taskCancled++;
                    break;
            }
        }

        res.setTaskCompleted(taskCompleted);
        res.setTaskCompromised(taskFailed);
        res.setTaskNotRunning(taskQueueing + taskFailed + taskCompleted + taskCancled);
        res.setTaskRunning(taskRunning);
        res.setTaskNumber(tasks.size());

        return res;
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class CesiumModelInfo{
    private String tilesetPath;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class TreeModel{
    private ModelCenter center;
    private List<TreeInfo> tree;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ModelCenter{
    private Double longitude;
    private Double latitude;
    private Double elevation;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class TreeInfo{
    private int name;
    private Double height;
    private Double guanfu;
    private Double dbh;
    private Double top_latitude;
    private Double top_longitude;
    private Double but_latitude;
    private Double but_longitude;
    private Double tree_elevation;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class SystemUsage{
    private Integer taskNumber;
    private Integer taskNotRunning;
    private Integer taskRunning;
    private Integer taskCompromised;
    private Integer taskCompleted;
    private Double storageSpace;
    private Double storageSpaceUsed;
    private Double storageSpaceUnUsed;
    private Double memorySpace;
    private Double memorySpaceUsed;
    private Double memorySpaceUnUsed;
}