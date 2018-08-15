package net.YeYongQuan.person.push.bean.db;


import net.YeYongQuan.person.push.bean.api.restful_model.group.GroupCreateModel;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_GROUP")
public class Group {
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();



    @Column()
    private String description;


    @Column(nullable = false)
    private String name;


    @ManyToOne( optional = false)
    @JoinColumn(name = "ownerId")
    private User  owner;
    @Column(insertable=false, updatable = false)
    private String ownerId;

    @Column(nullable = false)
    private String pic;

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();


    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch =FetchType.LAZY,cascade = CascadeType.ALL)
    private  Set<GroupMember>   members  =new HashSet<>();




    public Group() {
    }

    public Group(User owner,GroupCreateModel model) {
        this.owner = owner;
        this.name = model.getName();
        this.description = model.getDesc();
        this.pic = model.getPicture();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Set<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(Set<GroupMember> members) {
        this.members = members;
    }
}
