# System Introduction

本系统完成从用户上传无人机影像到输出正射影像图、三维点云、数字地面模型、树冠检测结果，并最后展示三维场景的自动化流程。

- Springboot：用户系统、项目管理、任务管理、系统核心配置、上传服务、下载服务、格式转换服务
- Flask：
	- PyODM：合成三维点云、正射影像图、数字地面模型
	- YOLOv5：树冠检测
- Docker：
	- Nodeodm：为PyODM提供运行环境
- obj23dtiles：obj转3dtiles

# Dependencies

## Springboot Dependencies

| Dependencies Name                   | Version | Notes         |
| ----------------------------------- | ------- | ------------- |
| mybatis-spring-boot-starter         | 2.2.2   |               |
| mysql-connector-java                |         | scope=runtime |
| spring-boot-starter-jdbc            | 1.1.21  |               |
| spring-boot-starter-web             |         |               |
| spring-boot-configuration-processor |         | optional=true |
| aspectjrt                           | 1.5.4   |               |
| aspectjweaver                       |         |               |
| spring-aop                          |         |               |
| cglib                               | 2.1     |               |
| spring-boot-starter-thymeleaf       |         |               |
| commons-io                          | 2.6     |               |
| commons-fileupload                  | 1.4     |               |
| docker-java                         | 3.2.7   | Deprecated    |
| jackson-databind                    |         |               |
| java-jwt                            | 3.19.1  |               |
| log4j-api                           | 2.14.1  |               |
| log4j-core                          | 2.14.1  |               |
| lombok                              |         | optional=true |
| spring-boot-starter-test            |         | scope=test    |

## PyODM Dependencies

| Dependencies Name | Version | Notes |
| ----------------- | ------- | ----- |
| flask             | 1.1.1   |       |
| pyodm             | 1.5.9   |       |
| requests          |         |       |

## YOLOv5 Dependencies

| Dependencies Name | Version | Notes |
| ----------------- | ------- | ----- |
| Flask             | 1.1.1   |       |

## obj23dtiles Dependencies

| Dependencies Name | Version | Notes |
| ----------------- | ------- | ----- |
| Node              | 16.15.0 |       |

## Nodeodm Dependencies

| Dependencies Name | Version | Notes |
| ----------------- | ------- | ----- |
| Docker            |         |       |

# POJO

## User

用于表示用户信息

| Properties  | Type      | Notes            |
| ----------- | --------- | ---------------- |
| uid         | String    | 用户id           |
| uname       | String    | 用户名           |
| pwd         | String    | 密码             |
| dateJoined  | Timestamp | 创建时间         |
| lastLogin   | Timestamp | 最近登录         |
| isSuperuser | boolean   | 是否为超级管理员 |
| firstName   | String    | 名               |
| lastName    | String    | 姓               |
| email       | String    | 邮箱             |
| isStaff     | boolean   | 是否为员工       |
| isActive    | boolean   | 是否活跃         |

## ImageRepo

用于表示用户**上传记录**，保存了上传的位置、时间、上传体积、文件数量等。

一个上传记录对应文件系统中一个实体文件夹，代表一个**仓库**

| Properties   | Type      | Notes          |
| ------------ | --------- | -------------- |
| rid          | String    | 仓库id         |
| uid          | String    | 仓库拥有人     |
| repoLocation | String    | 保存位置       |
| createTime   | Timestamp | 创建时间       |
| count        | int       | 文件数量       |
| size         | int       | 仓库体积（MB） |

## Project

用于表示用户所创建的项目

| Properties  | Type      | Notes        |
| ----------- | --------- | ------------ |
| pid         | String    | 项目id       |
| uid         | String    | 项目拥有人   |
| pname       | String    | 项目名字     |
| description | String    | 项目描述     |
| createTime  | Timestamp | 项目创建时间 |

## Task

用于表示一个odm任务，和仓库在逻辑上保证一一对应

| Properties | Type      | Notes              |
| ---------- | --------- | ------------------ |
| tid        | String    | 任务id             |
| rid        | String    | 任务数据的来源仓库 |
| pid        | String    | 所属项目           |
| tname      | String    | 任务名             |
| process    | Double    | 处理进度           |
| status     | String    | 任务状态           |
| updateTime | Timestamp | 更新时间           |

# DataBase

## user

| Column       | Type        | Notes          |
| ------------ | ----------- | -------------- |
| uid          | varchar(64) | 用户id         |
| uname        | varchar(64) | 用户名         |
| pwd          | varchar(64) | 密码           |
| first_name   | varchar(64) | 名             |
| last_name    | varchar(64) | 姓             |
| email        | varchar(64) | 邮箱           |
| is_staff     | tinyint(1)  | 是否员工       |
| is_active    | tinyint(1)  | 是否活跃       |
| date_joined  | datetime    | 创建时间       |
| is_superuser | tinyint(1)  | 是否超级管理员 |
| last_login   | datetime    | 最近登录       |

## image_repo

| Column        | Type         | Notes          |
| ------------- | ------------ | -------------- |
| rid           | varchar(64)  | 仓库id         |
| uid           | varchar(64)  | 所属用户       |
| count         | int          | 文件数量       |
| repo_location | varchar(256) | 仓库位置       |
| create_time   | datetime     | 创建时间       |
| size          | int          | 仓库容量（MB） |

## odm_project

| Column      | Type         | Notes    |
| ----------- | ------------ | -------- |
| pid         | varchar(64)  | 项目id   |
| uid         | varchar(64)  | 所属用户 |
| pname       | varchar(128) | 项目名称 |
| description | text         | 项目描述 |
| create_time | datetime     | 创建时间 |

## odm_task

| Column      | Type         | Notes    |
| ----------- | ------------ | -------- |
| tid         | varchar(64)  | 任务id   |
| rid         | varchar(64)  | 对应仓库 |
| pid         | varchar(64)  | 所属项目 |
| tname       | varchar(128) | 任务名   |
| process     | double       | 处理进度 |
| status      | varchar(64)  | 任务状态 |
| update_time | datetime     | 更新时间 |

# System Configuration

## application.yaml

| Name                          | Description                     |
| ----------------------------- | ------------------------------- |
| core.image-upload.upload-path | 文件上传绝对路径                |
| core.image-upload.tmp-path    | 文件上传临时路径                |
| core.odm.output-location      | odm输出根目录                   |
| core.odm.dem-name             | 数字高程模型相对输出目录位置    |
| core.odm.ort-name             | 正射影像图相对输出目录位置      |
| core.odm.tex-name             | 点云文件夹相对输出目录位置      |
| core.odm.obj-name             | 点云文件（obj）相对输出目录位置 |
| core.jwt.secret-key           | jwt签名密钥                     |
| core.env.obj23dtiles-bin      | obj23dtiles运行文件位置         |
| core.env.obj23dtiles-output   | obj23dtiles输出文件名           |
| core.env.tileset              | tileset名字                     |
| core.yolo.root                | yolo运行环境位置                |
| core.yolo.detect-res          | yolo输出检测结果文件名          |
| core.yolo.detect-json         | yolo输出json结果文件名          |

## configuration

### Cross Orgin

跨域配置

- CrosConfig

	| Allowed Origin        |
	| --------------------- |
	| http://127.0.0.1:8000 |
	| http://127.0.0.1:8080 |
	| http://127.0.0.1:5000 |
	| http://localhost:5100 |

- FilterConfig

	|         Response Header          | Value                                                        |
	| :------------------------------: | :----------------------------------------------------------- |
	|   Access-Control-Allow-Origin    | request.getHeader("Origin")                                  |
	|   Access-Control-Allow-Methods   | *                                                            |
	| Access-Control-Allow-Credentials | true                                                         |
	|   Access-Control-Allow-Headers   | Authorization,Origin, X-Requested-With, Content-Type, Accept,Access-Token |

### Interceptor

主要用于拦截登录请求，验证jwt有效期和登录有效性

- 获取请求头中的token
- 验证是否过期
- 验证登陆状态（status：false/true）

### 拦截器作用域

| Intercepter Name           | Path Patterns   | Type    |
| -------------------------- | --------------- | ------- |
| FilterConfig               | /**             | include |
| getLoginHandlerIntercepter | /**             | include |
| getLoginHandlerIntercepter | /api/token-auth | exclude |
| getLoginHandlerIntercepter | /               | exclude |
| getLoginHandlerIntercepter | /index          | exclude |
| getLoginHandlerIntercepter | /css/**         | exclude |
| getLoginHandlerIntercepter | /img/**         | exclude |
| getLoginHandlerIntercepter | /imagerepo/**   | exclude |
| getLoginHandlerIntercepter | /odmoutput/**   | exclude |

### 上传Multipart配置

```java
@Configuration
public class UploadConfig {
    @Bean(name="multipartResolver")
    public MultipartResolver multipartResolver(){
        return new CommonsMultipartResolver();
    }
}
```

### 页面路径映射

| 源路径                        | URL           |
| ----------------------------- | ------------- |
| index.html                    | /             |
| index.html                    | /index        |
| F:/StumdetRoot/ImageRepo/     | /imagerepo/** |
| F:/StumdetRoot/ODMOutputRoot/ | /odmoutput/** |



# Utils

## FileUploadUtils

### 介绍

对上层提供透明文件传输功能

### 属性列表

| Properties Name | Type                | Description             |
| --------------- | ------------------- | ----------------------- |
| request         | HttpServletRequest  | 包含文件和参数的请求    |
| size            | double              | 上传体积                |
| count           | int                 | 上传文件数量            |
| tmpPath         | String              | 临时目录                |
| targetPath      | String              | 目标路径                |
| fileItems       | List<FileItem>      | 从request中分离的文件项 |
| params          | Map<String, Object> | 从request中分离的参数项 |
| *bufSize*       | Integer             | 缓冲大小                |
| *singleMaxSize* | Long                | 单个文件最大体积        |
| *totalMaxSize*  | Long                | 一次上传体积上限        |

### 函数列表

- saveFiles

	- **描述**

		保存已经被拆分成MultipartFile的文件

	- **参数**

		| Field Name | Type                | Description      |
		| ---------- | ------------------- | ---------------- |
		| files      | List<MultipartFile> | 待保存的文件列表 |
		| target     | String              | 保存文件的位置   |

	- **返回值**：Map<String, Object>

		| Name   | Type    | Description |
		| ------ | ------- | ----------- |
		| status | boolean | 上传状态    |
		| count  | int     | 文件数量    |
		| size   | int     | 文件体积    |

- loadRequest

	- **描述**

		设置包含文件的请求

	- 参数

		| Field Name | Type               | Description    |
		| ---------- | ------------------ | -------------- |
		| request    | HttpServletRequest | 包含文件的请求 |

	- **返回值**：void

- setPath

	- **描述**

		设置保存文件的临时目录和目标文件夹

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| targetPath | String | 目标文件夹  |
		| tmpPath    | String | 临时目录    |

	- **返回值**：void

- preProcess

	- **描述**

		上传前处理，创建目标文件夹

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| targetPath | String | 目标文件夹  |

	- **返回值**：void

- CheckNecessity

	- **描述**

		判断文件上传必要条件，

	- **参数**

		无

	- **返回值**：boolean

		| Name | Type    | Description      |
		| ---- | ------- | ---------------- |
		| bool | boolean | 是否具备上传条件 |

- splitRequest

	- **描述**

		将类属性中包含文件的上传请求拆分成文件项和参数表，并保存至类属性中。在调用前需要设置request属性和路径属性

	- **参数**

		无

	- **返回值**：Map<String, Object>

		request中的参数信息

- saveFiles

	- **描述**

		将类属性中的文件项列表通过I/O输出至文件系统。需要保证类属性中包含文件列表。

	- **参数**

		无

	- **返回值**：boolean

		是否产生异常

## SqlDateTimeProducer

### 介绍

提供Timestamp的产生和转换，方便向数据库中写入datetime类型的数据

### 函数列表

- getCurrentDateTime

	- **描述**

		获取当前时间的Timestamp对象

	- **参数**

		无

	- **返回值**：Timestamp

- toTimestamp

	- **描述**

		将时间戳转换成Timestamp对象

	- **参数**

		| Field Name | Type | Description |
		| ---------- | ---- | ----------- |
		| stamp      | long | 时间戳      |

	- **返回值**：Timestamp

- toTimestamp

	- **描述**

		将Date类型转换成Timestamp对象

	- **参数**

		| Field Name | Type | Description |
		| ---------- | ---- | ----------- |
		| date       | Date | 日期        |

	- **返回值**：Timestamp

- toTimestamp

	- **描述**

		将字符串类型转换成Timestamp对象

	- **参数**

		| Field Name | Type                          | Description    |
		| ---------- | ----------------------------- | -------------- |
		| dateStr    | String（yyyy-MM-dd HH:mm:ss） | 字符串类型日期 |

	- **返回值**：Timestamp

## TokenUtils

### 介绍

提供Token生成工具

### 属性列表

| Properties Name | Type   | Description |
| --------------- | ------ | ----------- |
| sKey            | String | 私钥        |
| alg             | String | 加密算法    |
| typ             | String | token类型   |
| lifeSpan        | int    | 保存时长    |

### 函数列表

- setHeader

	- **描述**

		设置token头

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| alg        | String | 加密算法    |
		| typ        | String | Token类型   |

	- **返回值**：this

- setLifeSpan

	- **描述**

		设置token生命周期

	- **参数**

		| Field Name | Type | Description |
		| ---------- | ---- | ----------- |
		| span       | int  | 生命周期    |

	- **返回值**：this

- setSecretKey

	- **描述**

		设置token私钥

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| sKey       | String | 密钥        |

	- **返回值**：this

- createToken

	- **描述**

		创建token

	- **参数**

		| Field Name | Type                | Description |
		| ---------- | ------------------- | ----------- |
		| claims     | Map<String, Object> | 负载        |

	- **返回值**：String

- getPayload

	- **描述**

		获取token中的载荷

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| token      | String | Token       |
		| key        | String | 负载的键    |

	- **返回值**：String

## UUIDProducer

### 介绍

生成UUID的工具类

### 属性列表

| Properties Name | Type     | Description |
| --------------- | -------- | ----------- |
| uType           | UUIDType | UUID类型    |
| UUIDLen         | int      | UUID的长度  |
| *MaxLen*        | int      | 最长长度    |
| *MinLen*        | int      | 最短长度    |

### 函数列表

- toString

	- **描述**

		使用已有属性，合成一个UUID字符串并返回

	- **返回值**：String

		UUID字符串

# Mapper Layer

## UserMapper

### 介绍

用户系统Mapper层，用户信息数据库操作的基本接口

### 接口列表

- getUserById

  - **描述**

  	根据用户id查询用户

  - **参数**

  	| Field Name | Type   | Description |
  	| ---------- | ------ | ----------- |
  	| uid        | String | 用户UUID    |

  - **返回值**：User

  - **Sql**

  	```sql
  	SELECT * FROM `user` WHERE `uid`=#{uid}
  	```

- getAllUsers

  - **描述**

  	获取所有用户

  - **参数**

  	无

  - **返回值**：List<User>

  - **Sql**

  	```sql
  	SELECT * FROM `user`
  	```
  	
  	

- getUserByName

  - **描述**

  	根据用户名查询用户

  - **参数**

  	| Field Name | Type   | Description |
  	| ---------- | ------ | ----------- |
  	| uname      | String | 用户名      |

  - **返回值**：User

  - **Sql**

  	```sql
  	SELECT * FROM `user` WHERE `uname`=#{uname}
  	```
  	
  	

- addUser

  - **描述**

  	添加一个用户

  - **参数**

  	| Field Name | Type | Description |
  	| ---------- | ---- | ----------- |
  	| newUser    | User | 新用户      |

  - **返回值**：void

  - **Sql**

  	```sql
  	INSERT INTO `user`(uid, uname, pwd, date_joined, last_login, is_superuser, first_name, last_name, email, is_staff, is_active)
  	VALUES(#{uid}, #{uname}, #{pwd}, #{dateJoined}, #{lastLogin}, #{isSuperuser}, #{firstName}, #{lastName}, #{email}, #{isStaff}, #{isActive})
  	```

- changeSuperuser

  - **描述**

  	设置用户是否为超级管理员

  - **参数**：Map<String, Object> map

  	| Field Name  | Type    | Description      |
  	| ----------- | ------- | ---------------- |
  	| uid         | String  | 用户UUID         |
  	| isSuperuser | boolean | 是否为超级管理员 |

  - **返回值**：void

  - **Sql**

  	```sql
  	UPDATE `user`
  	SET `is_superuser`=#{isSuperuser}
  	WHERE `uid`=#{uid}
  	```
  	
  	

- changeStaff

  - **描述**

  	设置用户是否为员工

  - **参数**：Map<String, Object> map

  	| Field Name | Type    | Description |
  	| ---------- | ------- | ----------- |
  	| uid        | String  | 用户UUID    |
  	| isStaff    | boolean | 是否为员工  |

  - **返回值**：void

  - **Sql**

  	```sql
  	UPDATE `user`
  	SET `is_staff`=#{isStaff}
  	WHERE `uid`=#{uid}
  	```
  	
  	

- changeActive

  - **描述**

  	设置用户的活跃状态

  - **参数**：Map<String, Object> map

  	| Field Name | Type    | Description |
  	| ---------- | ------- | ----------- |
  	| uid        | String  | 用户UUID    |
  	| isActive   | boolean | 是否活跃    |

  - **返回值**：void

  - **Sql**

  	```sql
  	UPDATE `user`
  	SET `is_active`=#{isActive}
  	WHERE `uid`=#{uid}
  	```
  	
  	

- updateUserInfo

  - **描述**

  	更新用户信息：firstname、lastname、email

  - **参数**：Map<String, Object> map

  	| Field Name | Type   | Description |
  	| ---------- | ------ | ----------- |
  	| uid        | String | 用户UUID    |
  	| firstName  | String | 名          |
  	| lastName   | String | 姓          |
  	| email      | String | 邮箱        |

  - **返回值**：void

  - **Sql**

  	```sql
  	UPDATE `user`
  	SET `first_name`=#{firstName}, `last_name`=#{lastName}, `email`=#{email}
  	WHERE `uid`=#{uid}
  	```
  	
  	

- updateUserPass

  - **描述**

  	修改用户名、密码

  - **参数**：Map<String, Object> map

  	| Field Name | Type   | Description |
  	| ---------- | ------ | ----------- |
  	| uid        | String | 用户UUID    |
  	| uname      | String | 新用户名    |
  	| pwd        | String | 新密码      |

  - **返回值**：void

  - **Sql**

  	```sql
  	UPDATE `user`
  	SET `uname`=#{uname}, `pwd`=#{pwd}
  	WHERE `uid`=#{uid}
  	```
  	
  	

- deleteUserById

	- **描述**

		根据uid删除用户

	- **参数**:String

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| uid        | String | 用户UUID    |

	- **返回值**：void

	- **Sql**

		```sql
		DELETE FROM `user` WHERE `uid`=#{uid}
		```

		

- deleteUserByName

	- **描述**

		根据用户名删除用户

	- **参数**：String

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| uname      | String | 用户名      |

	- **返回值**：void

	- **Sql**

		```sql
		DELETE FROM `user` WHERE `uname`=#{uname} 
		```
		
		

## ProjectMapper

### 介绍

项目系统Mapper层，项目信息数据库操作的基本接口

### 接口列表

- getProjectByPid

	- **描述**

		向数据库查询id对应的项目

	- **参数**：Project

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| pid        | String | 项目id      |

	- **返回值**

	- **sql**

		```sql
		SELECT * FROM `odm_project` WHERE `pid`=#{pid}
		```

- getProjectByUid

	- **描述**

		向数据库查询用户所属的所有项目

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| uid        | String | 用户id      |

	- **返回值**：List<Project>

	- **sql**

		```sql
		SELECT * FROM `odm_project` WHERE `uid`=#{uid}
		```

		

- getProjectByPname

	- **描述**

		根据项目名查询项目信息

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| pname      | String | 项目名      |

	- **返回值**：Project

	- **sql**

		```sql
		SELECT * FROM `odm_project` WHERE `pname`=#{pname}
		```

		

- createProject

	- **描述**

		向数据库添加一条项目记录

	- **参数**

		| Field Name | Type    | Description |
		| ---------- | ------- | ----------- |
		| project    | Project | 新项目对象  |

	- **返回值**：void

	- **sql**

		```sql
		INSERT INTO `odm_project`(pid, uid, pname, description, create_time)
		VALUES(#{pid}, #{uid}, #{pname}, #{description}, #{createTime})
		```

		

- deleteProjectByPid

	- **描述**

		删除数据库中id对应的记录

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| pid        | String | 项目id      |

	- **返回值**：void

	- **sql**

		```sql
		DELETE FROM `odm_project` WHERE `pid`=#{pid}
		```

		

- updateProject

	- **描述**

		修改数据库中id对应项目的项目名和描述

	- **参数**：Map<String, Object>

		| Field Name  | Type   | Description |
		| ----------- | ------ | ----------- |
		| pid         | String | 项目id      |
		| pname       | String | 项目名      |
		| description | String | 项目描述    |

	- **返回值**：void

	- **sql**

		```sql
		UPDATE `odm_project`
		SET `pname`=#{pname}, `description`=#{description}
		WHERE `pid`=#{pid}
		```


## TaskMapper

### 介绍

任务系统Mapper层，任务信息数据库操作的基本接口

### 接口列表

- getTaskByPid

	- **描述**

		查询数据库中一个项目对应的所有任务

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| pid        | String | 项目id      |

	- **返回值**：List<Task>

	- **sql**

		```sql
		SELECT * FROM `odm_task` WHERE `pid`=#{pid}
		```

		

- getTaskByRid

	- **描述**

		查询数据库中仓库对应的任务（一一对应）

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| rid        | String | 仓库id      |

	- **返回值**：Task

	- **sql**

		```sql
		SELECT * FROM `odm_task` WHERE `rid`=#{rid}
		```

		

- getTaskByTid

	- **描述**

		查询数据库中任务id对应的任务

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| tid        | String | 任务id      |

	- **返回值**：Task

	- **sql**

		```sql
		SELECT * FROM `odm_task` WHERE `tid`=#{tid}
		```

		

- getTaskByName

	- **描述**

		查询数据库中任务名对应的任务

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| tname      | String | 任务名      |

	- **返回值**：Task

	- **sql**

		```sql
		SELECT * FROM `odm_task` WHERE `tname`=#{tname}
		```

		

- createTask

	- **描述**

		向数据库插入一条任务记录

	- **参数**

		| Field Name | Type | Description |
		| ---------- | ---- | ----------- |
		| task       | Task | 新任务对象  |

	- **返回值**：void

	- **sql**

		```sql
		INSERT INTO `odm_task`(tid, rid, pid, tname, process, status, update_time)
		VALUES(#{tid}, #{rid}, #{pid}, #{tname}, #{process}, #{status}, #{updateTime})
		```

		

- updateTaskInfo

	- **描述**

		向数据库更新任务进度、状态信息

	- **参数**：Map<String, Object>

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| tid        | String | 任务id      |
		| status     | String | 任务状态    |
		| process    | double | 进度        |

	- **返回值**：void

	- **sql**

		```sql
		UPDATE `odm_task`
		SET `process`=#{process}, `status`=#{status}, `update_time`=#{updateTime}
		WHERE `tid`=#{tid}
		```

		

- deleteTaskByTid

	- **描述**

		删除数据库中任务id对应的任务

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| tid        | String | 任务id      |

	- **返回值：void**

	- **sql**

		```sql
		DELETE FROM `odm_task` WHERE `tid`=#{tid}
		```

		

- deleteTaskByRid

	- **描述**

		删除数据库中仓库id对应的任务

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| rid        | String | 仓库id      |

	- **返回值**：void

	- **sql**

		```sql
		DELETE FROM `odm_task` WHERE `rid`=#{rid}
		```

		

- deleteTaskByPid

	- **描述**

		删除数据库中一个项目对应的所有任务

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| pid        | String | 项目id      |

	- **返回值：void**

	- **sql**

		```sql
		DELETE FROM `odm_task` WHERE `pid`=#{pid}
		```

		

- deleteTaskByName

	- **描述**

		删除数据库中任务名对应的任务

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| tname      | String | 任务名      |

	- **返回值：void**

	- **sql**

		```sql
		DELETE FROM `odm_task` WHERE `tname`=#{tname}
		```

## ImageRepoMapper

### 介绍

仓库操作Mapper层，仓库信息数据库操作的基本接口

### 接口列表

- getRepoById

	- **描述**

		查询数据库中id对应的仓库信息

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| rid        | String | 仓库id      |

	- **返回值**：ImageRepo

	- **sql**

		```sql
		SELECT * FROM `image_repo` WHERE `rid`=#{rid}
		```

		

- getRepoByUser

	- **描述**

		查询数据库中一个用户拥有的多有仓库

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| uid        | String | 用户id      |

	- **返回值**：List<ImageRepo>

	- **sql**

		```sql
		SELECT * FROM `image_repo` WHERE `uid`=#{uid}
		```

		

- createRepo

	- **描述**

		向数据库中添加一个仓库记录

	- **参数**

		| Field Name | Type      | Description  |
		| ---------- | --------- | ------------ |
		| repo       | ImageRepo | 新的仓库对象 |

	- **返回值**：void

	- **sql**

		```sql
		INSERT INTO `image_repo`(rid, uid, count, repo_location, create_time, size)
		VALUES(#{rid}, #{uid}, #{count}, #{repoLocation}, #{createTime}, #{size})
		```

		

- deleteRepo

	- **描述**

		删除数据库中id对应的一个仓库记录

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| rid        | String | 仓库id      |

	- **返回值**：void

	- **sql**

		```sql
		DELETE FROM `image_repo` WHERE `rid`=#{rid}
		```

		

- updateRepoInfo

	- **描述**

		修改数据库中仓库的数据：文件数量、仓库容量

	- **参数**：Map<String, Object>

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| rid        | String | 仓库id      |
		| count      | int    | 文件数量    |
		| size       | int    | 仓库容量    |

	- **返回值**：void

	- **sql**

		```sql
		UPDATE `image_repo`
		SET `count`=#{count}, `size`=#{size}
		WHERE `rid`=#{rid}
		```

		

# Service Layer

## UserService

用户系统Service层，向Controller层提供透明访问Mapper层功能，屏蔽所有对数据库的操作。

### 成员变量

| Properties | Type       | Description      |
| ---------- | ---------- | ---------------- |
| userMapper | UserMapper | 用户Mapper层对象 |

### 成员方法

*同UserMapper，仅作简单调用。*

## ProjectService

项目系统Service层，向Controller层提供透明访问Mapper层功能，屏蔽所有对数据库的操作。

### 成员变量

| Properties    | Type          | Description      |
| ------------- | ------------- | ---------------- |
| projectMapper | ProjectMapper | 项目Mapper层对象 |
| taskMapper    | TaskMapper    | 任务Mapper层对象 |

### 成员方法

- deleteProjectByPid

	由于数据库外键的依赖，在删除一个项目前需要删除Project对应的所有Task

*其余同ProjectMapper，仅作简单调用。*

## TaskService

任务系统Service层，向Controller层提供透明访问Mapper层功能，屏蔽所有对数据库的操作。

### 成员变量

| Properties | Type       | Description      |
| ---------- | ---------- | ---------------- |
| taskMapper | TaskMapper | 任务Mapper层对象 |

### 成员方法

- createTask

	为了保证仓库和任务的一一对应性，在创建任务时，先删除对应rid的任务。

*其余同TaskMapper，仅作简单调用。*

## ImagRepoService

仓库系统Service层，向Controller层提供透明访问Mapper层功能，屏蔽所有对数据库的操作。

### 成员变量

| Properties      | Type            | Description          |
| --------------- | --------------- | -------------------- |
| imageRepoMapper | ImageRepoMapper | 图像仓库Mapper层对象 |

### 成员方法

*同ImageRepoMapper，仅作简单调用。*

## ImageUploadService

封装FileUploadUtils，向Controller层提供透明图片上传功能。

### 成员变量

| Properties      | Type               | Description             |
| --------------- | ------------------ | ----------------------- |
| imageRepoMapper | ImageRepoMapper    | 图像仓库Mapper层对象    |
| request         | HttpServletRequest | 包含文件和参数的request |
| rid             | String             | 仓库id                  |
| uid             | String             | 用户id                  |
| uploadRoot      | String             | 上传根目录              |
| tmpPath         | String             | 临时目录                |

### 成员方法

- initUpload

	初始化上传操作：创建仓库文件夹、向数据库添加一条记录、生成仓库id并返回

	需要：imageRepoMapper、rid、uploadRoot

- handleUpload

	处理上传，仅在没有设置MultipartResolver时使用

## JWTService

封装TokenUtils，向Controller层提供JWT生成功能。

### 成员变量

| Properties | Type       | Description     |
| ---------- | ---------- | --------------- |
| secretKey  | String     | 密钥            |
| tokenUtils | TokenUtils | token工具类对象 |

### 成员方法

- getJWT：根据参数生成一个jwt

	- **参数**

		| Field Name | Type                | Description |
		| ---------- | ------------------- | ----------- |
		| claims     | Map<String, Object> | 负载        |
		| span       | int                 | 生命周期    |

	- **返回值**

		| Name | Type   | Description     |
		| ---- | ------ | --------------- |
		| jwt  | String | jwt的字符串类型 |

		

- getPayload：获得jwt中key对应的值

	- **参数**

		| Field Name | Type   | Description |
		| ---------- | ------ | ----------- |
		| token      | String | JWT字符串   |
		| key        | String | 键          |

	- **返回值**

		| Name  | Type   | Description     |
		| ----- | ------ | --------------- |
		| value | String | JWT中ke对应的值 |

# Controller Layer

## LoginController

- /api/token-auth

	负责登录功能，登陆成功返回200状态、用户信息、token；登陆失败仅返回400

	request：raw-json

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| uname          | String | 用户名      |
	| pwd            | String | 密码        |

	```json
	successful
	{
	    "status": 200,
	    "msg": true,
	    "userInfo": {
	        "uid": "user-6d55494fbab5474580aed318aa3ecef3",
	        "uname": "test1"
	    },
	    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiJ1c2VyLTZkNTU0OTRmYmFiNTQ3NDU4MGFlZDMxOGFhM2VjZWYzIiwidW5hbWUiOiJ0ZXN0MSIsImV4cCI6MTY1MTAyNTcyNCwic3RhdHVzIjoidHJ1ZSJ9.cZozAs-njge7ewB7sbBtZSM5EzpkKtxRaAvUrepWDs4",
	    "user": {
	        "uid": "user-6d55494fbab5474580aed318aa3ecef3",
	        "uname": "test1",
	        "pwd": "test-123456",
	        "dateJoined": "2022-04-18T14:03:24.000+00:00",
	        "lastLogin": "2022-04-18T14:03:24.000+00:00",
	        "firstName": "",
	        "lastName": "",
	        "email": "",
	        "active": true,
	        "superuser": true,
	        "staff": true
	    }
	}
	failed
	{
	    "status": 400,
	    "msg": false,
	    "userInfo": {},
	    "token": null,
	    "user": null
	}
	```

- /api/admin/users

	针对管理员用户，获取所有用户的信息

	no parameters

	```json
	success
	{
	    "count": 2,
	    "next": null,
	    "previous": null,
	    "results": [
	        {
	            "id": "user-6d55494fbab5474580aed318aa3ecef3",
	            "password": "test-123456",
	            "last_login": "2022-04-18 22:03:24.0",
	            "is_superuser": true,
	            "username": "test1",
	            "first_name": "",
	            "last_name": "",
	            "email": "",
	            "is_staff": true,
	            "is_active": true,
	            "date_joined": "2022-04-18 22:03:24.0",
	            "groups": [],
	            "user_permissions": [],
	            "_staff": true,
	            "_active": true,
	            "_superuser": true
	        },
	        {
	            "id": "user-7cf82d94b4884592ba20f4eb19f7d7fb",
	            "password": "6522017+",
	            "last_login": "2022-04-19 16:38:14.0",
	            "is_superuser": false,
	            "username": "LYOUL",
	            "first_name": "",
	            "last_name": "",
	            "email": "",
	            "is_staff": false,
	            "is_active": true,
	            "date_joined": "2022-04-19 16:38:14.0",
	            "groups": [],
	            "user_permissions": [],
	            "_staff": false,
	            "_active": true,
	            "_superuser": false
	        }
	    ]
	}
	```

	

- /api/user/create

	创建一个用户

	request：raw-json

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| uname          | String | 用户名      |
	| pwd            | String | 密码        |

	return：true/false

	

- /api/user/logout

	退出登录

	*// TODO*

- /api/user/update/superuser

	设置是否为超级管理员

	request：raw-json

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| uid            | String | 用户名      |
	| isSuperuser    | bool   | 是/否       |

	return：true/false

	

- /api/update/active

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| uid            | String | 用户名      |
	| isActive       | bool   | 是/否       |

	return：true/false

	

- /api/user/update/pwd

	改变用户名密码

	request：raw-json

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| uid            | String | 用户id      |
	| uname          | String | 用户名      |
	| pwd            | String | 密码        |

	return：true/false

	

- /api/user/update/userinfo

	改变用户firstname、lastname、email

	request：raw-json

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| uid            | String | 用户id      |
	| firstname      | String | 名          |
	| lastname       | String | 姓          |
	| email          | String | 邮箱        |

	return：true/false

	

## CoreConfigController

- /api/get/runtaskpath

	获得odm运行所需要的路径

	```json
	{
	    "outputRoot": "F:/StumdetRoot/ODMOutputRoot/",
	    "repoRoot": "F:/StumdetRoot/ImageRepo/"
	}
	```

	

- /api/get/outputloc

	获得odm输出路径

	```json
	{
	    "outputRoot": "F:/StumdetRoot/ODMOutputRoot/"
	}
	```

	

## ProjectController

- /api/project/getall

	获取一个用户下的所有项目

	request：raw-json

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| uid            | String | 用户id      |

	```json
	success
	[
	    {
	        "id": "prj-b4ce06d7288847b5b484229e51b00dbe",
	        "tasks": [
	            "d99137e5-e927-4a88-8df6-81a332641df2"
	        ],
	        "created_at": "2022-04-18T14:11:02.000+00:00",
	        "permissions": [
	            "add",
	            "change",
	            "delete",
	            "view"
	        ],
	        "name": "测试项目",
	        "description": "这是一个测试的项目"
	    }
	]
	```

	

- /api/project/get

	获取指定项目信息

	request：raw-json

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| pid            | String | 项目id      |

	```json
	success
	{
	    "id": "prj-b4ce06d7288847b5b484229e51b00dbe",
	    "tasks": [
	        "d99137e5-e927-4a88-8df6-81a332641df2"
	    ],
	    "created_at": "2022-04-18T14:11:02.000+00:00",
	    "permissions": [
	        "add",
	        "change",
	        "delete",
	        "view"
	    ],
	    "name": "测试项目",
	    "description": "这是一个测试的项目"
	}
	```

	

- /api/project/create

	创建一个项目

	request：raw-json

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| uid            | String | 用户id      |
	| pname          | String | 项目名      |
	| description    | String | 项目描述    |

	```json
	success：被创建的项目信息
	{
	    "id": "prj-b4ce06d7288847b5b484229e51b00dbe",
	    "tasks": [
	        "d99137e5-e927-4a88-8df6-81a332641df2"
	    ],
	    "created_at": "2022-04-18T14:11:02.000+00:00",
	    "permissions": [
	        "add",
	        "change",
	        "delete",
	        "view"
	    ],
	    "name": "测试项目",
	    "description": "这是一个测试的项目"
	}
	```

	

- /api/project/delete

	删除指定项目

	request：raw-json

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| pid            | String | 项目id      |

	return：pid

	

- /api/project/edit

	改变项目信息

	request：raw-json

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| pid            | String | 项目id      |
	| pname          | String | 项目名      |
	| description    | String | 描述        |

	return：true/false

	

## TaskController

- /api/task/create

	创建一个任务

	request：params

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| pid            | String | 项目id      |
	| tid            | String | 任务id      |
	| rid            | String | 仓库id      |
	| tname          | String | 项目名      |

	```json
	success: 被创建的task
	
	```

	

- /api/task/update

	更新任务进度、状态

	request：params

	| Parameter Name | Type    | Description |
	| -------------- | ------- | ----------- |
	| tid            | String  | 任务id      |
	| process        | process | 进度        |
	| status         | String  | 状态        |

	return：none

	

- /api/task/delete

	删除指定任务

	request：params

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| tid            | String | 任务id      |

	return：被删除的tid

	

- /api/task/getall

	获取一个项目下所有的任务

	request：raw-json

	| Parameter Name | Type   | Description |
	| -------------- | ------ | ----------- |
	| pid            | String | 项目id      |

	```json
	[
	    {
	        "id": "d99137e5-e927-4a88-8df6-81a332641df2",
	        "pid": "prj-b4ce06d7288847b5b484229e51b00dbe",
	        "processing_node": -1,
	        "processing_node_name": "UNKNOW",
	        "can_rerun_from": [],
	        "statistics": {},
	        "uuid": "d99137e5-e927-4a88-8df6-81a332641df2",
	        "name": "测试任务-70-1",
	        "processing_time": -1,
	        "auto_processing_node": true,
	        "status": "TaskStatus.COMPLETED",
	        "last_error": null,
	        "options": {},
	        "available_assets": [],
	        "create_at": "2022-04-18T14:23:20.000+00:00",
	        "pending_action": null,
	        "pub1ic": false,
	        "resize_to": -1,
	        "upload_progress": 1,
	        "resize_progress": 0,
	        "running_progress": 100.0,
	        "import_url": "",
	        "images_count": 70,
	        "partial": false,
	        "potree_scene": {},
	        "epsg": null
	    }
	]
	```

	

- /api/task/getid

- /api/task/getrid

## ImageUploadController

## FileDownloadController

## CesiumController

## TilesetController



# PyODM

# YOLOv5



# Centos部署

https://blog.csdn.net/qq_29183811/article/details/123558987

https://www.jb51.net/article/207867.htm

![img](F:\StumpageDetection\DOCUMENT.assets\31a557400ec370064ae37b02fd62aa7135e299ea.png@942w_827h_progressive.png)

![image-20220425211113228](F:\StumpageDetection\DOCUMENT.assets\image-20220425211113228.png)

sudo ./NVIDIA-Linux-x86_64-396.18.run -no-x-check -no-nouveau-check -no-opengl-files 作者：丶魂珏 https://www.bilibili.com/read/cv13462627 出处：bilibili

```php
docker volume ls -q -f driver=nvidia-docker | xargs -r -I{} -n1 docker ps -q -a -f volume={} | xargs -r docker rm -f
```

```php
curl -s -L https://nvidia.github.io/nvidia-docker/gpgkey | \
  sudo apt-key add -
distribution=$(. /etc/os-release;echo $ID$VERSION_ID)
curl -s -L https://nvidia.github.io/nvidia-docker/$distribution/nvidia-docker.list | \
  sudo tee /etc/apt/sources.list.d/nvidia-docker.list
```

