package org.example.controller;

import org.example.common.response.ResultEntity;
import org.example.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@RestController
@RequestMapping("/example")
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @GetMapping("/log")
    public ResultEntity doSomething() {
        //开始时间
        long startTime = System.currentTimeMillis();
        try {
            //随机睡眠模拟业务执行
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //结束时间
        long endTime = System.currentTimeMillis();
        //时间差
        long diffTime = endTime - startTime;
        //记录到数据库
        logger.debug("接口处理时间为：{}毫秒", diffTime);
        return ResultEntity.ok();
    }

    @Log(module = "日志模块", info = "测试接口")
    @GetMapping("/aopLog")
    public ResultEntity aopDoSomething() {
        try {
            //随机睡眠模拟业务执行
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResultEntity.ok();
    }
}
