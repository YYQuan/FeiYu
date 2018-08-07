package net.YeYongQuan.person.push.bean.db;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "TB_PUSH_HISTORY")
public class PushHistory {

    public static final  int  PUSH_STATUS_SENDING = 0;
    public static final  int  PUSH_STATUS_RECEIVED = 1;
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;

    //    BLOB是比TEXT更多的一个大字段类型
    @Lob
    @Column( columnDefinition ="BLOB" )
    private String entity;

    @Column
    private int entityType;




    // 接收者
    // 接收者不允许为空
    // 一个接收者可以接收很多推送消息
    // FetchType.EAGER：加载一条推送消息的时候之间加载用户信息
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "receiverId")// 默认是：receiver_id
    private User receiver;
    @Column(nullable = false, updatable = false, insertable = false)
    private String receiverId;


    // 发送者
    // 发送者可为空，因为可能是系统消息
    // 一个发送者可以发送很多推送消息
    // FetchType.EAGER：加载一条推送消息的时候之间加载用户信息
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "senderId")
    private User sender;
    @Column(updatable = false, insertable = false)
    private String senderId;

    @Column
    private String receiverPushId;



    @Column
    private LocalDateTime receiveAt ;

    @CreationTimestamp
    @Column(nullable =  false )
    private LocalDateTime createAt =  LocalDateTime.now();



    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt  = LocalDateTime.now();


    public static int getPushStatusSending() {
        return PUSH_STATUS_SENDING;
    }

    public static int getPushStatusReceived() {
        return PUSH_STATUS_RECEIVED;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getReceiveAt() {
        return receiveAt;
    }

    public void setReceiveAt(LocalDateTime receiveAt) {
        this.receiveAt = receiveAt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverPushId() {
        return receiverPushId;
    }

    public void setReceiverPushId(String receiverPushId) {
        this.receiverPushId = receiverPushId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
