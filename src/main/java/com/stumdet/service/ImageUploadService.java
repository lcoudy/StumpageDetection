package com.stumdet.service;

import com.stumdet.mapper.ImageRepoMapper;
import com.stumdet.pojo.ImageRepo;
import com.stumdet.utils.FileUploadUtils;
import com.stumdet.utils.SqlDateTimeProducer;
import com.stumdet.utils.UUIDProducer;
import com.stumdet.utils.UUIDType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Service: ImageUploadService
 * Author: Yuyang Zhao
 * 图片上传服务，向Controller层提供透明图片上传功能
 */
@Service
@Scope("prototype")
public class ImageUploadService {
    private HttpServletRequest request;
    private String rid;
    private String uid;
    private String uploadRoot;
    private String tmpPath;

    @Autowired
    private ImageRepoMapper imageRepoMapper;

    /**
     * 上传图片初始化:
     * - 生成仓库rid
     * - 创建目标仓库
     * - 写入数据库
     * @return rid
     */
    public String initUpload(){
        // 需要uid uploadRoot imageRepoMapper
        assert imageRepoMapper != null && uid != null && uploadRoot != null : "ImageUploadService.initUpload: Missing parameters.";

        this.rid = new UUIDProducer.Builder()
                .setuType(UUIDType.ImageRepo)
                .setUUIDLen(32)
                .build().toString();
        String repoPath = uploadRoot + rid;
        imageRepoMapper.createRepo(new ImageRepo(rid, uid, repoPath, SqlDateTimeProducer.getCurrentDateTime(), 0, 0));
        FileUploadUtils.preProcess(repoPath);
        return this.rid;
    }

    /**
     * 处理上传:
     * - 拆分request
     * - 获得参数
     * - 将文件保存至目标文件夹
     * - 修改数据库中size和count字段
     * @return success
     */
    public boolean handleUpload(){
        // 需要uploadRoot tmpPath request imageRepoMapper
        assert uploadRoot!= null && tmpPath != null && request != null && imageRepoMapper != null : "ImageUploadService.handleUpload: Missing parameters.";

        FileUploadUtils uploadUtils = new FileUploadUtils();
        uploadUtils.setPath(null, this.tmpPath);
        uploadUtils.loadRequest(this.request);
        Map<String, Object> res = uploadUtils.splitRequest();   // 分割了参数和文件，返回所有参数
        this.rid = (String)res.get("rid");

        System.out.println(this.rid);

        String targetPath = this.uploadRoot + this.rid + "/";
        uploadUtils.setPath(targetPath, this.tmpPath);  // 由参数中的rid得到targetPath并传入uploadUtils
        boolean success = uploadUtils.saveFiles();
        this.imageRepoMapper.updateRepoInfo(res);
        return success;
    }

    /* 类属性get&set方法 */
    public HttpServletRequest getRequest() {
        return request;
    }

    public ImageUploadService setRequest(HttpServletRequest request) {
        this.request = request;
        return this;
    }

    public String getRid() {
        return rid;
    }

    public ImageUploadService setRid(String rid) {
        this.rid = rid;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public ImageUploadService setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getUploadRoot() {
        return uploadRoot;
    }

    public ImageUploadService setUploadRoot(String uploadRoot) {
        this.uploadRoot = uploadRoot;
        return this;
    }

    public String getTmpPath() {
        return tmpPath;
    }

    public ImageUploadService setTmpPath(String tmpPath) {
        this.tmpPath = tmpPath;
        return this;
    }

}
