package net.YeYongQuan.person.push.bean.db;

import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "TB_USER")
public class User implements  Principal{
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;

    @CreationTimestamp
    @Column(updatable = false,nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    @Column
    private String description;

    @CreationTimestamp
    @Column
    private LocalDateTime lastReceiveTime;


    @Column(nullable = false, length = 128, unique = true)
    private String name;


    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 64, unique = true)
    private String phoneNum;

    @Column
    private String  portrait;



    @Column
    private String pushId;

    @Column(unique =true)
    private String token;

    @Column(nullable = false)
    private int sex = 0;



    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updateTime= LocalDateTime.now();



    @JoinColumn(name = "originUserId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<UserFollow>  following =new HashSet<>();

    @JoinColumn(name = "targetUserId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<UserFollow>  followers =new HashSet<>();

    @JoinColumn(name = "ownerId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Group>  owners =new HashSet<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public LocalDateTime getLastReceiveTime() {
        return lastReceiveTime;
    }

    public void setLastReceiveTime(LocalDateTime lastReceiveTime) {
        this.lastReceiveTime = lastReceiveTime;
    }


}
