package net.YeYongQuan.person.push.bean.db;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Locale;

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

    @Column
    private String pushId;

    @Column(nullable = false)
    private String targetId;


    @Column(nullable = false)
    private int status;

    @CreationTimestamp
    @Column(nullable =  false ,insertable = false,updatable = false)
    private LocalTime createAt =  LocalTime.now();

    @CreationTimestamp
    @Column
    private LocalTime receiveAt ;
}
