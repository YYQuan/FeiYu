package net.YeYongQuan.person.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "TB_USER_FOLLOW")
public class UserFollow {


    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(nullable = false,updatable = false)
    private String id;

    @Column
    private String alias;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalTime createAt =LocalTime.now();



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


    @CreationTimestamp
    @Column(nullable = false)
    private LocalTime updateAt =LocalTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LocalTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalTime createAt) {
        this.createAt = createAt;
    }

    public User getOriginUser() {
        return originUser;
    }

    public void setOriginUser(User originUser) {
        this.originUser = originUser;
    }

    public String getOriginUserId() {
        return originUserId;
    }

    public void setOriginUserId(String originUserId) {
        this.originUserId = originUserId;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public LocalTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalTime updateAt) {
        this.updateAt = updateAt;
    }
}
