package com.zw.zwplatform.blackfilter;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

@Slf4j
public class BlackIpUtils {

    // 布隆过滤器，用于存储黑名单ip
    private static BitMapBloomFilter bloomFilter;

    //判断ip是否在黑名单中
    public static boolean isBlackIP(String ip) {
        return bloomFilter.contains(ip);
    }

    public static void rebuildBlackIp(String configInfo) {
        if(StringUtils.isBlank(configInfo)){
            configInfo= "{}";
        }
        //解析yaml文件
        Yaml yaml = new Yaml();
        Map map = yaml.loadAs(configInfo, Map.class);
        //获取黑名单ip
        List<String> blackIpList = (List<String>) map.get("blackIpList");

        //加锁防止并发
        synchronized (BlackIpUtils.class){
            if(CollectionUtil.isNotEmpty(blackIpList)){
                bloomFilter = new BitMapBloomFilter(958506);
                for (String ip : blackIpList) {
                    bloomFilter.add(ip);
                }
            }else{
                bloomFilter = new BitMapBloomFilter(100);
            }
        }
    }
}
