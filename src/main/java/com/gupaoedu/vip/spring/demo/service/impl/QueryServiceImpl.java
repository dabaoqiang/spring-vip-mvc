package com.gupaoedu.vip.spring.demo.service.impl;

import com.gupaoedu.vip.spring.demo.service.IQueryService;
import com.gupaoedu.vip.spring.framework.annotation.GPService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiaoqiang
 * @Title: QueryServiceImpl
 * @ProjectName spring-vip-mvc
 * @Description: TODO
 * @date 2019-01-18 23:08
 */
@GPService(value = "iQueryService")
public class QueryServiceImpl implements IQueryService {

    /**
     * 查询
     */
    public String query(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
        return json;
    }
}
