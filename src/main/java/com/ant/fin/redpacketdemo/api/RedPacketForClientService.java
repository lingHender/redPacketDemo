package com.ant.fin.redpacketdemo.api;

import com.ant.fin.redpacketdemo.domain.Message;
import com.ant.fin.redpacketdemo.domain.RedPacket;


//客户端调用红包接口类
public interface RedPacketForClientService {


    /**
     * 创建红包接口
     * @param redPacket
     * @return
     */
    Message createRedPacket(RedPacket redPacket);


    /**
     * 发送红包接口
     * @param redPacketId
     * @return
     */
    Message sendRedPacket(String redPacketId);


    /**
     * 抢红包接口
     * @param userId
     * @param redPacketId
     * @return
     */
    Message grabRedPacket(Long userId,String redPacketId);


    /**
     * 获取红包信息接口
     * @param redPacketId
     * @return
     */
    RedPacket getRedPacket(String redPacketId);
}
