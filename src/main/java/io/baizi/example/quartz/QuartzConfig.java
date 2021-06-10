package io.baizi.example.quartz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author juwencheng
 * @date 2021 2021/6/10 9:52 上午
 */
@Configuration
@ComponentScan(basePackages = {
        "com.baiziio.lib.quartz.controller",
        "com.baiziio.lib.quartz.service",
})
@MapperScan(value = {"com.baiziio.lib.quartz.dao.mapper"})
public class QuartzConfig {

}
