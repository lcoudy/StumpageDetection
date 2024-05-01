package com.stumdet.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@Scope("prototype")
@RequestMapping("/api")
public class FileDownloadController {

    @Value("${core.odm.output-location}")
    private String outputRoot;

    @Value("${core.odm.dem-name}")
    private String dem;

    @Value("${core.odm.ort-name}")
    private String ortho;

    @Value("${core.odm.tex-name}")
    private String obj;

    @Value("${core.yolo.detect-res}")
    private String detectImg;

    @RequestMapping("/assert/dem")
    @CrossOrigin
    public void getDemAssert(HttpServletResponse response, @RequestBody Map<String, Object> params) throws IOException {
        // params: rid
        String rid = (String)params.get("rid");
        File dem = new File(this.outputRoot + rid + this.dem);
        if(!dem.exists()){
            System.out.println("dem文件不存在");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) dem.length());
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(dem.getName(), "UTF-8"));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dem));
        byte[] buff = new byte[1024];
        OutputStream os = response.getOutputStream();
        int i = 0;
        while ((i = bis.read(buff)) != -1) {
            os.write(buff, 0, i);
            os.flush();
        }
        bis.close();
        os.close();
        System.out.println("下载成功");
    }

    @RequestMapping("/assert/ortho")
    @CrossOrigin
    public void getOrthoAssert(HttpServletResponse response, @RequestBody Map<String, Object> params) throws IOException {
        // params: rid
        String rid = (String)params.get("rid");
        File ortho = new File(this.outputRoot + rid + this.ortho);
        if(!ortho.exists()){
            System.out.println("ortho文件不存在");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) ortho.length());
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(ortho.getName(), "UTF-8"));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ortho));
        byte[] buff = new byte[1024];
        OutputStream os = response.getOutputStream();
        int i = 0;
        while ((i = bis.read(buff)) != -1) {
            os.write(buff, 0, i);
            os.flush();
        }
        bis.close();
        os.close();
        System.out.println("下载成功");
    }

    @RequestMapping("/assert/detect")
    @CrossOrigin
    public void DetectResult(HttpServletResponse response, @RequestBody Map<String, Object> params) throws IOException {
        // params: rid
        String rid = (String)params.get("rid");
        File ortho = new File(this.outputRoot + rid + this.detectImg);
        if(!ortho.exists()){
            System.out.println("检测图不存在");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) ortho.length());
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(ortho.getName(), "UTF-8"));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ortho));
        byte[] buff = new byte[1024];
        OutputStream os = response.getOutputStream();
        int i = 0;
        while ((i = bis.read(buff)) != -1) {
            os.write(buff, 0, i);
            os.flush();
        }
        bis.close();
        os.close();
        System.out.println("下载成功");
    }



    @RequestMapping("/assert/pc")
    @CrossOrigin
    public void writeZip(HttpServletResponse response, @RequestBody Map<String, Object> params){
        String zipname="PointCloud";
        File[] childrenFiles =  new File(this.outputRoot + (String)params.get("rid") + this.obj).listFiles();

        List<File> files = new ArrayList<>();
        for(File childFile : childrenFiles) {
            files.add(childFile);
        }
        String fileName = zipname + ".zip";
        response.setContentType("application/zip");
        response.setHeader("content-disposition", "attachment;filename=" + fileName);

        ZipOutputStream zos = null;
        BufferedInputStream bis = null;
        try{
            zos = new ZipOutputStream(response.getOutputStream());
            byte[] buf = new byte[8192];
            int len;
            for (int i = 0; i < files.size(); i++) {
                File file = new File(String.valueOf(files.get(i)));
                if (!file.isFile()) continue;
                ZipEntry ze = new ZipEntry(file.getName());
                zos.putNextEntry(ze);
                bis = new BufferedInputStream(new FileInputStream(file));
                while ((len = bis.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
            }
            zos.closeEntry();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if(bis != null){
                try{
                    bis.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zos != null){
                try{
                    zos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
