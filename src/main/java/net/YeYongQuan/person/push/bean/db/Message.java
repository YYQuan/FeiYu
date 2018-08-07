package net.YeYongQuan.person.push.bean.db;


import net.YeYongQuan.person.push.bean.api.restful_model.message.MessageModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "TB_MESSAGE")
public class Message {
    public static final int MSG_TYPE_USER = 0;
    public static final int MSG_TYPE_GROUP = 1;

    public static final int TYPE_STR = 1; // 字符串类型
    public static final int TYPE_PIC = 2; // 图片类型
    public static final int TYPE_FILE = 3; // 文件类型
    public static final int TYPE_AUDIO = 4; // 语音类型


    @Id
    @PrimaryKeyJoinColumn
//    不自动创建uuid 而是由客户端传入uuid 这样可以避免复杂的服务器的映射关系
//    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;

    @Column(updatable = false)
    private String attach ;


//    类型为text  TEXT是一个大字段类型
    @Column(updatable = false,nullable = false,columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime  createAt = LocalDateTime.now();

    @ManyToOne()
    @JoinColumn(name = "receiverGroupId")
    private Group receiverGroup;
    @Column(insertable=false, updatable=false)
    private String receiverGroupId;

    @ManyToOne()
    @JoinColumn(name = "receiverId")
    private User receiver;
    @Column(insertable=false, updatable = false)
    private String receiverId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "senderId")
    private User sender;
    @Column(insertable=false, updatable = false)
    private String senderId;

    @Column(updatable = false,nullable = false)
    private int messageType;

    @CreationTimestamp
    @Column()
    private LocalDateTime  updateAt;


    public Message(User sender, User receiver, MessageModel model) {
        this.id = model.getId();
        this.content = model.getContent();
        this.attach = model.getAttach();
        this.messageType = model.getReceiveType();

        this.sender = sender;
        this.receiver = receiver;
    }


    // 发送给群的构造函数
    public Message(User sender, Group group, MessageModel model) {
        this.id = model.getId();
        this.content = model.getContent();
        this.attach = model.getAttach();
        this.messageType = model.getReceiveType();

        this.sender = sender;
        this.receiverGroup = group;
    }

    public Message() {
    }

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

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Group getReceiverGroup() {
        return receiverGroup;
    }

    public void setReceiverGroup(Group receiverGroup) {
        this.receiverGroup = receiverGroup;
    }

    public String getReceiverGroupId() {
        return receiverGroupId;
    }

    public void setReceiverGroupId(String receiverGroupId) {
        this.receiverGroupId = receiverGroupId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
