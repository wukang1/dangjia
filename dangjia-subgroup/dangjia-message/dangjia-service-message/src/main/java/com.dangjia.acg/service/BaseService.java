package com.dangjia.acg.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author: QiYuXiang
 * @date: 2018/10/24
 */
@Component
public class BaseService {
    @Value("${spring.data.jmessage.zhuangxiu.appkey}")
    private String zx_appkey;

    @Value("${spring.data.jmessage.zhuangxiu.masterSecret}")
    private String zx_masterSecret;

    @Value("${spring.data.jmessage.zhuangxiu.appkey}")
    private String gj_appkey;

    @Value("${spring.data.jmessage.zhuangxiu.masterSecret}")
    private String gj_masterSecret;


    public Map<String, String>  map = new HashMap<String, String>();

    @PostConstruct //加上该注解表明该方法会在bean初始化后调用
    public void init(){
        map.put("zx_appkey", zx_appkey);
        map.put("gj_appkey", gj_appkey);
        map.put("zx_masterSecret", zx_masterSecret);
        map.put("gj_masterSecret", gj_masterSecret);
    }
    private String appkey;
    private String masterSecret;

    public String getAppkey(String appType) {
        appkey=map.get(appType+"_appkey");
        return appkey;
    }
    public String getMasterSecret(String appType) {
        masterSecret=map.get(appType+"_masterSecret");
        return masterSecret;
    }

    public  final Logger LOG = LoggerFactory.getLogger(BaseService.class);
}

