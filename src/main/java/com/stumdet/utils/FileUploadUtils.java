package com.stumdet.utils;

import com.stumdet.mapper.ImageRepoMapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utils: FileUploadUtils
 * Author: Yuyang Zhao
 * 文件上传工具类
 * 封装文件上传所需的所有功能，向Service层提供透明上传服务
 */
public class FileUploadUtils {
    private HttpServletRequest request = null; // 包含文件的请求
    private double size = 0;    // 上传文件的总大小
    private int count = 0;  //文件数量
    private String tmpPath = null; // 上传临时文件夹
    private String targetPath = null;  //目标文件夹, 最后加斜杠!!!!!!!!!!!!!!!!!!!!

    private List<FileItem> fileItems;   // request中的文件
    private Map<String, Object> params; // request中的参数

    private final static Integer bufSize = 128 * 1024 * 1024;   // 128MB缓冲大小
    private final static Long singleMaxSize = (long)128 * 1024 * 1024; // 128MB单文件最大体积
    private final static Long totalMaxSize = (long)10 * 1024 * 1024 * 1024; // 16GB单词上传上限

    /**
     * 参数设置：
     * Request-包含文件的请求
     * targetPath-目标文件夹，即文件要上传的位置
     * tmpPath-临时文件夹
     * @param request
     */
    public void loadRequest(HttpServletRequest request){
        this._setRequest(request);
    }

    public void setPath(String targetPath, String tmpPath){
        this._setTargetPath(targetPath);
        this._setTmpPath(tmpPath);
    }

    /**
     * 预处理：
     * 创建目标文件夹等
     * 随着系统迭代可以添加更多
     * @param targetPath
     */
    public static void preProcess(String targetPath){
        File targetFile = new File(targetPath);
        targetFile.mkdir();
    }

    /**
     * 检查必要条件：
     * 目标文件夹和临时文件夹是否被创建
     * @return whether meet the conditions
     */
    public boolean CheckNecessity(){
        assert (this._getTargetPath() != null) && (this._getTmpPath() != null) : "Nullpointer, path is empty";
        File targetFile = new File(this._getTargetPath());
        File tmpFile = new File(this._getTmpPath());
        if(!targetFile.exists() || !tmpFile.exists()){
            return false;
        }
        if(request == null){
            return false;
        }
        return true;
    }

    /**
     * 拆分request对象：
     * 将Request拆分成参数对象和文件对象，并将参数对象返回给上层
     * @return Map<String, Object>
     *     - size
     *     - count
     *     - fields
     *       * rid
     *       * uid
     *       ...
     *     ...
     */
    public Map<String, Object> splitRequest(){
        assert this.request != null : "HttpServletRequest request is null";
        assert CheckNecessity() : "Check necessity failed";

        // 接收前初始化对象-begin
        this.fileItems = new ArrayList<FileItem>();
        this.params = new HashMap<String, Object>();

        File tmpFile = new File(this._getTmpPath());

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(this.bufSize);
        factory.setRepository(tmpFile);

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");  // 设置编码
        upload.setFileSizeMax(this.singleMaxSize);  // 设置单文件上限
        upload.setSizeMax(this.totalMaxSize);   // 设置一次上传上限
        // 接收前初始化对象-end

        try {
            List<FileItem> items = upload.parseRequest(this.request);   // 接收request并解析
            for (FileItem item : items){
                if(item.isFormField()){
                    String field = item.getFieldName();
                    String value = item.getString("UTF-8");
                    this.params.put(field, value);  // 参数放入map中
                }
                else{
                    this.fileItems.add(item);   // 文件对象放入list中，提供下一步使用
                    this._setSize(item.getSize() + this._getSize());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(this.fileItems.size());

        this._setCount(this.fileItems.size());
        this.params.put("count", this._getCount());
        this.params.put("size", this._getSize() / 1024 / 1024);

        return this.params;
    }

    /**
     * 将文件保存至目标位置
     * @return
     */
    public boolean saveFiles(){
        assert this.fileItems != null : "List<FileItem> fileItems is null";
        try {
            for (FileItem fileItem : this.fileItems) {
                String fullFileName = fileItem.getName();
                String fileName = fullFileName.substring(fullFileName.lastIndexOf("/") + 1);
                String fileSuffix = fullFileName.substring(fullFileName.lastIndexOf(".") + 1);
                InputStream is = fileItem.getInputStream();
                FileOutputStream fos = new FileOutputStream(this._getTargetPath() + fileName);
                int len = 0;
                byte[] buf = new byte[this.bufSize];
                while((len = is.read(buf)) > 0){
                    fos.write(buf, 0, len);
                }
                is.close();
                fos.close();
                fileItem.delete();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("file save error");
            return false;
        }
        return true;
    }

    public static Map<String, Object> saveFiles(List<MultipartFile> files, String target){
        Map<String, Object> res = new HashMap<>();
        Double size = new Double(0.0);
        boolean status = true;
        try {
            for (MultipartFile file : files) {
                String fullFileName = file.getOriginalFilename();
                System.out.println(fullFileName);
                //String fileName = fullFileName.substring(fullFileName.lastIndexOf("/") + 1);
                String fileSuffix = fullFileName.substring(fullFileName.lastIndexOf(".") + 1);
                InputStream is = file.getInputStream();
                FileOutputStream fos = new FileOutputStream(target + fullFileName);
                int len = 0;
                byte[] buf = new byte[128*1024*1024];
                while((len = is.read(buf)) > 0){
                    size += len;
                    fos.write(buf, 0, len);
                }
                is.close();
                fos.close();
                status = true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("file save error");
            status = false;
        }
        res.put("status", status);
        res.put("count", files.size());
        res.put("size", size / 1024 / 1024);
        return res;
    }

    /* 类属性get&set方法 */
    public HttpServletRequest _getRequest() {
        return request;
    }

    public void _setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public double _getSize() {
        return size;
    }

    public void _setSize(double size) {
        this.size = size;
    }

    public int _getCount() {
        return count;
    }

    public void _setCount(int count) {
        this.count = count;
    }

    public String _getTmpPath() {
        return tmpPath;
    }

    public void _setTmpPath(String tmpPath) {
        this.tmpPath = tmpPath;
    }

    public String _getTargetPath() {
        return targetPath;
    }

    public void _setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
}
