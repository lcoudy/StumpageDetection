package com.stumdet.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.lang.Runtime.getRuntime;

@Controller
@Scope("prototype")
@RequestMapping("/api")
public class TilesetController {
    @Value("${core.odm.output-location}")
    private String outputRoot;

    @Value("${core.odm.obj-name}")
    private String objPath;

    @Value("${core.env.obj23dtiles-output}")
    private String tilesetPath;

    @Value("${core.env.obj23dtiles-bin}")
    private String obj23dtiles;

    @Value("${core.odm.tex-name}")
    private String texPath;

    @RequestMapping("/convert/tileset")
    @CrossOrigin
    @ResponseBody
    public String create3DTiles(@RequestBody Map<String, Object> params) throws IOException, InterruptedException {
        String rid = (String)params.get("rid");
        String objLoc = this.outputRoot + rid + this.objPath;
        String tilesetLoc = this.outputRoot + rid + this.texPath + this.tilesetPath;

        System.out.println(objLoc);
        System.out.println(tilesetLoc);

        File tilesetFile = new File(tilesetLoc);
        File objFile = new File(objLoc);
        if(tilesetFile.exists()){
            tilesetFile.delete();
        }
        assert objFile.exists() : "obj not exists";

        String cmd = "node " + obj23dtiles + " -i " + objLoc + " --tileset";
        System.out.println(cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        while(!tilesetFile.exists()){
            Thread.sleep(1000);
            System.out.println("wait for tileset to be created");
        }
        System.out.println("finished");

        return "finished";

    }


}
