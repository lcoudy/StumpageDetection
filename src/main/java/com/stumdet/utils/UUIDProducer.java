package com.stumdet.utils;

import java.util.UUID;

/**
 * Method: UUIDProducer
 * Author: Yuyang Zhao
 * 使用建造者模式，自定义用户类型或影像仓库的UUID
 * （当前只有两层链，随着系统迭代，自定义更加适合存储和区分的UUID）
 * UUID格式：标识前缀+UUID
 */
public class UUIDProducer {

    // 指定UUID类型
    private UUIDType uType = UUIDType.User;

    // 指定UUID后缀长度，最长不超过32，最短不小于8
    private int UUIDLen = MaxLen;

    private final static int MaxLen = 32;
    private final static int MinLen = 8;

    UUIDProducer(Builder builder){
        this.uType = builder.uType;
        this.UUIDLen = builder.UUIDLen;
    }

    // UUID建造者
    public static class Builder{
        private UUIDType uType = UUIDType.User;
        private int UUIDLen = MaxLen;

        // 设置类型
        public Builder setuType(UUIDType uType) {
            this.uType = uType;
            return this;
        }

        // 设置UUID长度，不包括前缀
        public Builder setUUIDLen(int UUIDLen) {
            if(UUIDLen > MaxLen)
                this.UUIDLen = MaxLen;
            else if(UUIDLen < MinLen)
                this.UUIDLen = MinLen;
            else
                this.UUIDLen = UUIDLen;
            return this;
        }

        // 构建UUID
        public UUIDProducer build(){
            return new UUIDProducer(this);
        }
    }

    // 返回UUID字符串
    public String toString(){

        StringBuilder sb = new StringBuilder();

        switch (this.uType){
            case User:
                sb.append("user-");
                sb.append(UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, this.UUIDLen));
                break;
            case ImageRepo:
                sb.append("repo-");
                sb.append(UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, this.UUIDLen));
                break;
            case ODMTask:
                sb.append("task-");
                sb.append(UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, this.UUIDLen));
                break;
            case ODMProject:
                sb.append("prj-");
                sb.append(UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, this.UUIDLen));
                break;
            default:
                sb.append(UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, this.UUIDLen));
                break;
        }

        return sb.toString();
    }
}

