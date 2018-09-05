package com.ant.fin.redpacketdemo.service;

import com.ant.fin.redpacketdemo.domain.RedPacket;

import java.util.Set;

public interface RedisService {

    void createRedPacketRedis(RedPacket redPacket);

    RedPacket getRedPacketById(String redPacketId);

    Boolean isRepeatGrab(Long userId,String redPacketId);

    void updateRedPacketRedis(Long userId,RedPacket redPacket);

    void addExpireEngineRedis(String key,String value);

    Set<String> listExpireRedPacket(String key);

    void removeRedPacketRedis(String redPacketId);
}
