package com.stumdet.controller;

import com.stumdet.mapper.ImageRepoMapper;
import com.stumdet.service.ImageUploadService;
import com.stumdet.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Controller: ImageUploadController
 * Author: Yuyang Zhao
 * 图像上传控制层
 */
@Controller
@Scope("prototype")
@RequestMapping("/api")
public class ImageUploadController {

    @Value("${core.image-upload.upload-path}")
    private String uploadRoot;

    @Value("${core.image-upload.tmp-path}")
    private String tmpPath;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private ImageRepoMapper imageRepoMapper;

    /**
     * 初始化图片上传，并返回一个仓库id
     * @param
     * @return rid
     */
    @PostMapping("upload/init")
    @CrossOrigin
    @ResponseBody
    public String initUpload(@RequestBody Map<String, Object> map){
        //System.out.println(this.imageUploadService.hashCode());
        String uid = (String)map.get("uid");
        this.imageUploadService.setUid(uid)
                .setUploadRoot(this.uploadRoot);

        String rid = imageUploadService.initUpload();   // 初始化上传
        return rid;
    }
    @RequestMapping("upload/upload")
    @CrossOrigin
    @ResponseBody
    public String upload(@RequestPart("uploadFile") List<MultipartFile> uploadFiles, @RequestParam("rid") String rid){
        //System.out.println("upload2");
        Map<String, Object> res = FileUploadUtils.saveFiles(uploadFiles, uploadRoot + rid + "/");
        res.put("rid", rid);
        imageRepoMapper.updateRepoInfo(res);
        return String.valueOf(res.get("status"));
    }


}
