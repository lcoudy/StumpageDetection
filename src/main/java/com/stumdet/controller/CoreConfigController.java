package com.stumdet.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class CoreConfigController {

    @Value("${core.image-upload.upload-path}")
    private String repoRoot;

    @Value("${core.odm.output-location}")
    private String outputRoot;

    @Value("${core.odm.dem-name}")
    private String dsm;

    @Value("${core.odm.ort-name}")
    private String ortho;

    @Value("${core.yolo.root}")
    private String yoloRoot;

    @RequestMapping("/get/runtaskpath")
    @CrossOrigin
    @ResponseBody
    public Map<String, Object> getRepoLoc(){
        Map<String, Object> res = new HashMap<>();
        res.put("outputRoot", this.outputRoot);
        res.put("repoRoot", this.repoRoot);
        return res;
    }

    @RequestMapping("/get/outputloc")
    @CrossOrigin
    @ResponseBody
    public Map<String, Object> getOutputLoc(){
        Map<String, Object> res = new HashMap<>();
        res.put("outputRoot", this.outputRoot);
        return res;
    }

    @RequestMapping("/get/detect")
    @CrossOrigin
    @ResponseBody
    public Map<String, Object> getDetectPath(){
        Map<String, Object> res = new HashMap<>();
        res.put("outputRoot", this.outputRoot);
        res.put("dsm", this.dsm);
        res.put("ortho", this.ortho);
        res.put("yoloRoot", this.yoloRoot);
        return res;
    }

}
