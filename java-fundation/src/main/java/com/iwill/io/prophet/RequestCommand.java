package com.iwill.io.prophet;

import com.google.gson.Gson;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class RequestCommand implements Protocol {

    private static final SerializeType serializeType = SerializeType.JSON;

    private int code;

    private String remark;

    private transient byte[] body;

    public RequestCommand(int code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public ByteBuffer encode() {
        //4字节全局长度+4字节header长度+header+body
        int length = 4;

        byte[] headerData = new Gson().toJson(this).getBytes(Charset.forName("UTF-8"));

        length += headerData.length;

        if (this.body != null) {
            length += body.length;
        }

        ByteBuffer result = ByteBuffer.allocate(4 + length);

        //全局长度
        result.putInt(length);

        //留出一个字节放序列化类型,剩余3个字节存放header长度已足够
        result.put(markSerializeType(headerData.length, serializeType));

        result.put(headerData);

        if (this.body != null) {
            result.put(body);
        }

        result.flip();

        return result;
    }

    private byte[] markSerializeType(int source, SerializeType st) {
        byte[] result = new byte[4];
        result[0] = st.getCode();
        result[1] = (byte) ((source >> 16) & 0xFF);
        result[2] = (byte) ((source >> 8) & 0xFF);
        result[3] = (byte) (source & 0xFF);
        return result;
    }

    public static RequestCommand decode(final ByteBuffer byteBuffer) {
        int length = byteBuffer.limit();
        //全局长度
        int oriHeaderLen = byteBuffer.getInt();
        //header长度
        int headerLength = oriHeaderLen & 0xFFFFFF;
        byte[] headerData = new byte[headerLength];
        byteBuffer.get(headerData);
        String json = new String(headerData, Charset.forName("UTF-8"));
        RequestCommand cmd = new Gson().fromJson(json, RequestCommand.class);

        int bodyLength = length - 4 - headerLength;
        byte[] bodyData = null;
        if (bodyLength > 0) {
            bodyData = new byte[bodyLength];
            byteBuffer.get(bodyData);
        }
        cmd.setBody(bodyData);
        return cmd;
    }
}
