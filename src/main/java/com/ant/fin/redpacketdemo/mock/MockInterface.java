package com.ant.fin.redpacketdemo.mock;

import com.ant.fin.redpacketdemo.domain.RedPacket;

public interface MockInterface {
    /**
     * 校验红包信息是否正确,正确返回true，错误false
     */
    Boolean checkRedPacketInfo(RedPacket redPacket);

    /**
     * 校验红包是否已经到账,到账返回true,错误返回false
     */
    Boolean checkTransferRedPacket(RedPacket redPacket);

    /**
     * 将红包信息存入数据库
     */
    void saveRedPacketToDB(RedPacket redPacket);

    /**
     * 通知领取红包
     */
    Boolean noticeToReceiveRedPacket(String redPacketId);

    /**
     * 退款
     */
    void refundInterface(Double refund,RedPacket redPacket);
}
