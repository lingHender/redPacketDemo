package com.ant.fin.redpacketdemo.domain;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RedPacket implements Serializable {
    private String id;

    private Long groupId;

    private Long initiatorId;

    private Double allMoney;

    private Integer count;

    private String time;

    private Integer themeId;

    private String blessing;

    private String streamNum;

    private List<SegmentRedPacket> segmentRedPackets;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(Long initiatorId) {
        this.initiatorId = initiatorId;
    }

    public Double getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(Double allMoney) {
        this.allMoney = allMoney;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public String getBlessing() {
        return blessing;
    }

    public void setBlessing(String blessing) {
        this.blessing = blessing;
    }

    public List<SegmentRedPacket> getSegmentRedPackets() {
        return segmentRedPackets;
    }

    public void setSegmentRedPackets(List<SegmentRedPacket> segmentRedPackets) {
        this.segmentRedPackets = segmentRedPackets;
    }

    public String getStreamNum() {
        return streamNum;
    }

    public void setStreamNum(String streamNum) {
        this.streamNum = streamNum;
    }

    @Override
    public String toString() {
        return "RedPacket[id:"+id+"groupId:"+groupId+"initiatorId:"+initiatorId+"allMoney:"+allMoney+"count:"+count+"time:"+time+"" +
                "themeId:"+themeId+"blessing:"+blessing+"streamNum:"+streamNum+"segmentRedPackets:"+segmentRedPackets.toString()+"]";
    }
}
