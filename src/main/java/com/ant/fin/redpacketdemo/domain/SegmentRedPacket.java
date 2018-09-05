package com.ant.fin.redpacketdemo.domain;

import java.io.Serializable;

public class SegmentRedPacket implements Serializable {
    private Long id;

    private String parentPacketId;

    private Long userId;

    private Double amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentPacketId() {
        return parentPacketId;
    }

    public void setParentPacketId(String parentPacketId) {
        this.parentPacketId = parentPacketId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "SegmentRedPacket:[id:"+id+"parentPacketId:"+parentPacketId+"userId:"+userId+"amount:"+amount+"]";
    }
}
