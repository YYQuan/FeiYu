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

    @CreationTimestamp
    @Column
    private LocalDateTime receiveAt ;

    @CreationTimestamp
    @Column(nullable =  false ,insertable = false,updatable = false)
    private LocalDateTime createAt =  LocalDateTime.now();

//    BLOB是比TEXT更多的一个大字段类型
    @Column( columnDefinition ="BLOB" )
    private String entity;

    @Column
    private int entityType;

    @Column(nullable = false)
    private String receiverId;

    @Column
    private String receiverPushId;

    @Column
    private String  senderId;


    @CreationTimestamp
    @Column()
    private LocalDateTime updateAt ;


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
}
