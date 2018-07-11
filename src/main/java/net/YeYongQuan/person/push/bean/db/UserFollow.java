package net.YeYongQuan.person.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "TB_USER_FOLLOW")
public class UserFollow {


    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(nullable = false,updatable = false)
    private String id;


    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt =LocalDateTime.now();

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt =LocalDateTime.now();



    @ManyToOne(optional = false)   //不可选，必须存储
    @JoinColumn(name = "originUserId")
    private  User originUser;
    @Column(nullable = false,insertable = false,updatable = false)
    private String originUserId;


    @ManyToOne(optional = false)   //不可选，必须存储
    @JoinColumn(name = "targetUserId")
    private  User targetUser;
    @Column(nullable = false,insertable = false,updatable = false)
    private String targetUserId;

    @Column
    private String alias;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
