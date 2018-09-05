package com.ant.fin.redpacketdemo.service.impl;

import com.ant.fin.redpacketdemo.domain.Message;
import com.ant.fin.redpacketdemo.domain.RedPacket;
import com.ant.fin.redpacketdemo.domain.SegmentRedPacket;
import com.ant.fin.redpacketdemo.mock.MockInterface;
import com.ant.fin.redpacketdemo.service.RedPacketService;
import com.ant.fin.redpacketdemo.service.RedisService;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("redPacketService")
public class RedPacketServiceImpl implements RedPacketService {

    private final static Logger logger = Logger.getLogger(RedPacketServiceImpl.class);

    @Resource
    private MockInterface mockInterface;

    @Resource
    private RedisService redisService;

    @Transactional
    public String createRedPacket(RedPacket redPacket) {
        String redPacketId = null;
        try{
            //1.生成红包ID
            redPacketId = generateID(redPacket.getGroupId(),redPacket.getInitiatorId(),redPacket.getTime());
            //2.存入数据库(Mock)
            mockInterface.saveRedPacketToDB(redPacket);
            //43存入redis
            redisService.createRedPacketRedis(redPacket);
        }catch (Exception e){
            logger.info("RedPacketServiceImpl createRedPacket is error:",e);
            return null;
        }
        return redPacketId;
    }

    @Transactional
    public Message prepareRedPacket(String redPacketId) {
        Message msg = Message.success();
        try{
            //1.分小红包
            RedPacket redPacket = redisService.getRedPacketById(redPacketId);
            segmentRedPacket(redPacket);
            //2.更新缓存
            redisService.createRedPacketRedis(redPacket);
        }catch (Exception e){
            logger.info("RedPacketServiceImpl prepareRedPacket is error,redPackId:"+redPacketId,e);
            msg = Message.failure();
            msg.setData("准备红包出错");
        }
        return msg;
    }

    public Message grabRedPacket(Long userId, String redPacketId) {
        Message msg = Message.success();
        synchronized (this){
            try{
                //1.获取红包
                RedPacket redPacket = redisService.getRedPacketById(redPacketId);
                if(null == redPacket){
                    logger.info("红包已过期,userId:"+userId+",redPacketId:"+redPacketId);
                    msg.setData("红包已过期");
                    return msg;
                }
                //2.检查用户是否抢过该红包
                if(redisService.isRepeatGrab(userId,redPacketId)){
                    logger.info("您已经抢过该红包,userId:"+userId+",redPacketId:"+redPacketId);
                    msg.setData("您已经抢过该红包");
                    return msg;
                }
                //3.找到需要下发的红包
                Double grapMoney = findSegmentRedPacket(userId,redPacket);
                if(grapMoney.equals(0.0)){
                    logger.info("红包已抢完,userId:"+userId+",redPacketId:"+redPacketId);
                    msg.setData("手慢了，红包已抢完");
                    return msg;
                }
                //4.更新红包状态
                redisService.updateRedPacketRedis(userId,redPacket);
                msg.setData(grapMoney.toString());
                logger.info("红包已获取,userId:"+userId+",redPacketId:"+redPacketId +"money:"+grapMoney);
                return msg;
            }catch (Exception e){
                logger.info("RedPacketServiceImpl grabRedPacket is error,userId:"+userId+",redPacketId:"+redPacketId,e);
                msg = Message.failure();
                msg.setData("系统异常");
                return msg;
            }
        }
    }

    /*
    生成红包id
     */
    private String generateID(Long groupId,Long initiatorId,String time){
        StringBuilder sb = new StringBuilder();
        sb.append(groupId).append(initiatorId).append(time);
        return sb.toString();
    }

    /**
     * 分小红包
     */
    private void segmentRedPacket(RedPacket redPacket){
        List<SegmentRedPacket> segmentRedPacketList = new ArrayList<SegmentRedPacket>();
        String parentRedPacketId = redPacket.getId();
        Integer count = redPacket.getCount();
        Double money = redPacket.getAllMoney();
        for(int i = count; i > 0; i--){
            Double segmentRedPacketMoney = caculateRedPacketMoney(money,i);
            money = money - segmentRedPacketMoney;
            SegmentRedPacket segmentRedPacket = new SegmentRedPacket();
            segmentRedPacket.setParentPacketId(parentRedPacketId);
            segmentRedPacket.setAmount(segmentRedPacketMoney);
            segmentRedPacketList.add(segmentRedPacket);
        }
        redPacket.setSegmentRedPackets(segmentRedPacketList);
    }

    /**
     * 计算红包大小
     */
    private Double caculateRedPacketMoney(Double remainMoney,Integer remainCount){
        if(remainCount.equals(1)){
            return (double) (Math.round(remainMoney * 100) / 100);
        }
        Random r = new Random();
        double min = 0.01;
        double max = remainMoney / remainCount*2;
        double money = r.nextDouble() * max;
        money = Math.max(min,money);
        money = Math.floor(money * 100) / 100;
        return money;
    }

    /**
     * 找到要下发的红包
     */
    private Double findSegmentRedPacket(Long userId,RedPacket redPacket){
        Double result = 0.0;
        List<SegmentRedPacket> segmentRedPacketList = redPacket.getSegmentRedPackets();
        if(CollectionUtils.isEmpty(segmentRedPacketList)){
            throw new RuntimeException();
        }
        for(int i=0; i< segmentRedPacketList.size();i++){
            if(null == segmentRedPacketList.get(i).getUserId()){
                result = segmentRedPacketList.get(i).getAmount();
                segmentRedPacketList.get(i).setUserId(userId);
                break;
            }
        }
        return result;
    }

    //每个小时的55分清除过期红包,例如:8点55清除昨天9点到9点55的数据,这个地方粒度可以细化
    @Scheduled(cron = "0 55 * * * ?")
    public void processExpireRedPacket(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        StringBuilder sb = new StringBuilder();
        sb.append("RedPacketExpireKey").append(Integer.valueOf(simpleDateFormat.format(new Date())+1));
        Set<String> expireRedPacketSet = redisService.listExpireRedPacket(sb.toString());

        for(String redPacketId: expireRedPacketSet){
            RedPacket redPacket = redisService.getRedPacketById(redPacketId);
            Double refund = getReFund(redPacket);
            mockInterface.refundInterface(refund,redPacket);
            mockInterface.saveRedPacketToDB(redPacket);
            redisService.removeRedPacketRedis(redPacketId);
        }
    }

    /**
     * 获取要退款金额
     */
    private Double getReFund(RedPacket redPacket){
        Double result = 0.0;
        List<SegmentRedPacket> segmentRedPacketList = redPacket.getSegmentRedPackets();
        for(SegmentRedPacket segmentRedPacket : segmentRedPacketList){
            if(null != segmentRedPacket.getUserId()){
                result = result + segmentRedPacket.getAmount();
            }
        }
        return result;
    }
}
