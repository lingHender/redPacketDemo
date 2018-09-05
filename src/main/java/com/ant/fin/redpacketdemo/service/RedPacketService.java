package com.ant.fin.redpacketdemo.service;

import com.ant.fin.redpacketdemo.domain.Message;
import com.ant.fin.redpacketdemo.domain.RedPacket;

public interface RedPacketService {

    /**
     * 创建红包,成功返回红包ID，失败null
     */
    String createRedPacket(RedPacket redPacket);

    /**
     * 准备红包
     */
    Message prepareRedPacket(String redPacketId);

    /**
     * 抢红包
     */
    Message grabRedPacket(Long userId,String redPacketId);
}
