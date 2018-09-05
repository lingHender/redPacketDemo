package com.ant.fin.redpacketdemo.domain;

public class Message {

    private String code;

    private String data;

    private final static  String SUCCESS_CODE = "200";

    private final static String FAILURE_CODE = "300";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Message(String code) {
        this.code = code;
    }

    public static Message success(){
        return new Message(SUCCESS_CODE);
    }

    public static Message failure(){
        return new Message(FAILURE_CODE);
    }

    public static Boolean isSuccess(Message msg){
        if(null != msg && null != msg.getCode() && "" != msg.getCode() && SUCCESS_CODE.equals(msg.getCode())){
            return true;
        }
        return false;
    }
}
