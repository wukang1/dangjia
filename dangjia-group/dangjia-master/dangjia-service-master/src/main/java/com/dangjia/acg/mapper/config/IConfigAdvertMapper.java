package com.dangjia.acg.mapper.config;

import com.dangjia.acg.modle.config.ConfigAdvert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * author: Ronalcheng
 * Date: 2018/10/31 0031
 * Time: 17:01
 */
@Repository
public interface IConfigAdvertMapper extends Mapper<ConfigAdvert> {

    List<ConfigAdvert> selectAllConfigAdvert();
    List<ConfigAdvert> selectConfigAdvert(@Param("cityId") String cityId,@Param("appType") String appType , @Param("advertType") String advertType);

    List<String> getTimeOutAd();

    List<String> getTimingAd();
}
