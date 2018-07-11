package net.YeYongQuan.person.push.bean.api.ModelCard;

import com.google.gson.annotations.Expose;
import net.YeYongQuan.person.push.bean.db.Group;
import net.YeYongQuan.person.push.bean.db.User;
import net.YeYongQuan.person.push.bean.db.UserFollow;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class UserCard {
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String phoneNum;
    @Expose
    private String desc;
    @Expose
    private String  portrait;
    @Expose
    private int sex;
    @Expose
    private  boolean  isFollow;


//    他的关注的人的数量
    @Expose
    private int  following;

//    他的粉丝数量
    @Expose
    private int follwers;

    // 用户信息最后的更新时间
    @Expose
    private LocalDateTime modifyAt;

    public  UserCard(User user){
        this(user ,false);
    }

    public UserCard(User user ,boolean isFollow){
        this.isFollow = isFollow;

        this.id = user.getId();
        this.name = user.getName();
        this.phoneNum = user.getPhoneNum();
        this.portrait = user.getPortrait();
        this.desc = user.getDescription();
        this.sex = user.getSex();
        this.modifyAt = user.getUpdateTime();

        //TODO  关注人以及粉丝的数目
    }

    public static UserCard  build(User user){
        return   new UserCard(user);
    }

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

    public String getDescription() {
        return desc;
    }

    public void setDescription(String description) {
        this.desc = description;
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

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollwers() {
        return follwers;
    }

    public void setFollwers(int follwers) {
        this.follwers = follwers;
    }
}
