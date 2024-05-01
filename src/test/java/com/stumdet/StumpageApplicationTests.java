package com.stumdet;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stumdet.mapper.ImageRepoMapper;
import com.stumdet.mapper.ProjectMapper;
import com.stumdet.mapper.UserMapper;
import com.stumdet.pojo.ImageRepo;
import com.stumdet.pojo.Task;
import com.stumdet.pojo.User;

import com.stumdet.service.TaskService;
import com.stumdet.utils.SqlDateTimeProducer;
import com.stumdet.utils.TokenUtils;
import com.stumdet.utils.UUIDProducer;
import com.stumdet.utils.UUIDType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import com.sun.management.OperatingSystemMXBean;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

@SpringBootTest
class StumpageApplicationTests {
    @Autowired
    private ProjectMapper projectMapper;
    @Test
    public void TEst01(){
        System.out.println(projectMapper.getProjectByUid("user-7cf82d94b4884592ba20f4eb19f7d7fb"));
    }

}
