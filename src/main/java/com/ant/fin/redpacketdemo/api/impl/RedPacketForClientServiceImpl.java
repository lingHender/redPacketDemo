package com.ant.fin.redpacketdemo.api.impl;

import com.ant.fin.redpacketdemo.api.RedPacketForClientService;
import com.ant.fin.redpacketdemo.domain.Message;
import com.ant.fin.redpacketdemo.domain.RedPacket;
import com.ant.fin.redpacketdemo.mock.MockInterface;
import com.ant.fin.redpacketdemo.service.RedPacketService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service("redPacketForClientService")
public class RedPacketForClientServiceImpl implements RedPacketForClientService {

    private final static Logger logger = Logger.getLogger(RedPacketForClientServiceImpl.class);

    @Resource
    private MockInterface mockInterface;

    @Resource
    private RedPacketService redPacketService;

    public Message createRedPacket(RedPacket redPacket) {
        Message msg = null;
        try{
            //1.校验传入参数是否正确,此处调用mock接口
            if(!mockInterface.checkRedPacketInfo(redPacket)){
                msg = Message.failure();
                msg.setData("红包信息错误");
                return msg;
            }
            //2.查询红包的支付情况，此处调用mock接口
            if(!mockInterface.checkTransferRedPacket(redPacket)){
                msg = Message.failure();
                msg.setData("红包未到账");
                return msg;
            }
            //3.调用核心类中创建红包接口
            String redPacketId = redPacketService.createRedPacket(redPacket);
            if(null != redPacketId && redPacketId.length() > 0){
                msg = Message.success();
                msg.setData(redPacketId);
                return msg;
            }else{
                msg = Message.failure();
                msg.setData("创建红包失败");
                return msg;
            }
        }catch (Exception e){
            logger.info("RedPacketForClientServiceImpl createRedPacket error,param is" + redPacket.toString());
            logger.info("RedPacketForClientServiceImpl createRedPacket is error:",e);
            msg = Message.failure();
            msg.setData("系统异常");
            return msg;
        }
    }

    public Message sendRedPacket(String redPacketId) {
        Message msg = null;
        try{
            Message prepareMsg = redPacketService.prepareRedPacket(redPacketId);
            if(!Message.isSuccess(prepareMsg)){
                msg = Message.failure();
                msg.setData("红包准备失败");
                return msg;
            }
            if(mockInterface.noticeToReceiveRedPacket(redPacketId)){
                msg = Message.success();
                return msg;
            }else{
                msg = Message.failure();
                msg.setData("通知领取失败");
                return msg;
            }
        }catch (Exception e){
            logger.info("RedPacketForClientServiceImpl sendRedPacket is error,redPacketId:"+redPacketId,e);
            msg = Message.failure();
            msg.setData("系统异常");
            return msg;
        }
    }

    public Message grabRedPacket(Long userId, String redPacketId) {
        return redPacketService.grabRedPacket(userId,redPacketId);
    }

    public RedPacket getRedPacket(String redPacketId) {
        return null;
    }
}
