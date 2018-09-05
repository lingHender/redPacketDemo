package com.ant.fin.redpacketdemo.aspect;

import com.ant.fin.redpacketdemo.service.RedisService;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
public class ExpireEngineAspect {

    private final static Logger logger = Logger.getLogger(ExpireEngineAspect.class);

    @Resource
    private RedisService redisService;

    @AfterReturning(value = "execution(* com.ant.fin.redpacketdemo.service.RedPacketService.createRedPacket() )",returning = "redPacketId")
    public void recordRedPacket(String redPacketId){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        StringBuilder sb = new StringBuilder();
        sb.append("RedPacketExpireKey").append(simpleDateFormat.format(new Date()));
        redisService.addExpireEngineRedis(sb.toString(),redPacketId);
    }
}
