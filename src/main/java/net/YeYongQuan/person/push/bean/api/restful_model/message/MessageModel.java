package net.YeYongQuan.person.push.bean.api.restful_model.message;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

import net.YeYongQuan.person.push.bean.db.Message;

public class MessageModel {

    @Expose
    private String id;

    @Expose
    private String attach ;

    //    类型为text  TEXT是一个大字段类型
    @Expose
    private String content;

    // 消息类型
    @Expose
    private int type = Message.TYPE_STR;

    @Expose
    private String receiverId;

//    发给人的还是还给 group的
    @Expose
    private int receiverType = Message.MSG_TYPE_GROUP;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getReceiveType() {
        return receiverType;
    }

    public void setReceiveType(int receiveType) {
        this.receiverType = receiveType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public static boolean check(MessageModel model) {
        return model != null
                && !(Strings.isNullOrEmpty(model.id)
                || Strings.isNullOrEmpty(model.content)
                || Strings.isNullOrEmpty(model.receiverId))

                && (model.receiverType == Message.MSG_TYPE_USER
                || model.receiverType == Message.MSG_TYPE_GROUP)

                && (model.type == Message.TYPE_STR
                || model.type == Message.TYPE_AUDIO
                || model.type == Message.TYPE_FILE
                || model.type == Message.TYPE_PIC);
    }

}
