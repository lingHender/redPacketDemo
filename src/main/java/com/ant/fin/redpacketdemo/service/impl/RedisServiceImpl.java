package com.ant.fin.redpacketdemo.service.impl;

import com.ant.fin.redpacketdemo.domain.RedPacket;
import com.ant.fin.redpacketdemo.service.RedisService;
import com.ant.fin.redpacketdemo.tools.SerializeUtil;
import org.springframework.stereotype.Service;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

@Service("redisService")
public class RedisServiceImpl implements RedisService {

    private final static String REDPACKET = "REDPACKET";

    private final static String REDPACKETMEMBER = "REDPACKETMEMBER";

    @Resource
    private JedisPool jedisPool;

    private final static Logger logger = Logger.getLogger(RedisServiceImpl.class);

    private Jedis jedis;

    public RedisServiceImpl() {
        this.jedis = jedisPool.getResource();
    }

    public void createRedPacketRedis(RedPacket redPacket) {
        String redPacketID = redPacket.getId();
        jedis.set((REDPACKET+redPacketID).getBytes(),SerializeUtil.serialize(redPacket));
        jedis.sadd(REDPACKETMEMBER+redPacketID);
    }

    public RedPacket getRedPacketById(String redPacketId) {
        byte[] redpacketBytes = jedis.get((REDPACKET+redPacketId).getBytes());
        return (RedPacket) SerializeUtil.unserialize(redpacketBytes);
    }

    public Boolean isRepeatGrab(Long userId, String redPacketId) {
        return jedis.sismember((REDPACKETMEMBER+redPacketId),userId.toString());
    }

    public void updateRedPacketRedis(Long userId, RedPacket redPacket) {
        String redPacketID = redPacket.getId();
        jedis.set((REDPACKET+redPacketID).getBytes(),SerializeUtil.serialize(redPacket));
        jedis.sadd((REDPACKETMEMBER+redPacketID),userId.toString());
    }

    public void addExpireEngineRedis(String key, String value) {
        jedis.sadd(key,value);
    }

    public Set<String> listExpireRedPacket(String key) {
        return jedis.smembers(key);
    }

    public void removeRedPacketRedis(String redPacketId) {
        jedis.del((REDPACKET+redPacketId).getBytes());
        jedis.del((REDPACKETMEMBER+redPacketId));
    }
}
