package net.YeYongQuan.person.push.bean.db;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "TB_MESSAGE")
public class Message {
    private static final int MSG_TYPE_USER = 0;
    private static final int MSG_TYPE_GROUP = 1;


    @Id
    @PrimaryKeyJoinColumn
//    不自动创建uuid 而是由客户端传入uuid 这样可以避免复杂的服务器的映射关系
//    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;

    @Column(updatable = false,nullable = false)
    private int messageType;

    @Column(updatable = false,nullable = false)
    private String content;

    @Column(updatable = false)
    private String attch ;


    @ManyToOne(optional = false)
    @JoinColumn(name = "senderId")
    private User sender;
    @Column(insertable=false, updatable = false)
    private String senderId;


    @ManyToOne(optional = false)
    @JoinColumn(name = "receiverId")
    private User receiver;
    @Column(insertable=false, updatable = false)
    private String receiverId;


    @ManyToOne(optional = false)
    @JoinColumn(name = "receiverGroupId")
    private Group receiverGroup;
    @Column(insertable=false, updatable = false)
    private String receiverGroupId;



    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @CreationTimestamp
    @Column()
    private LocalDateTime  receiveAt;

}
