package com.ant.fin.redpacketdemo.mock.impl;

import com.ant.fin.redpacketdemo.domain.RedPacket;
import com.ant.fin.redpacketdemo.mock.MockInterface;
import org.springframework.stereotype.Service;

@Service("mockInerface")
public class MockInerfaceImpl implements MockInterface {
    public Boolean checkRedPacketInfo(RedPacket redPacket) {
        return null;
    }

    public Boolean checkTransferRedPacket(RedPacket redPacket) {
        return null;
    }

    public void saveRedPacketToDB(RedPacket redPacket) {

    }

    public Boolean noticeToReceiveRedPacket(String redPacketId) {
        return null;
    }

    public void refundInterface(Double refund, RedPacket redPacket) {

    }

}
