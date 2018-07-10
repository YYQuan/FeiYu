package net.YeYongQuan.person.push.bean.db;


import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;
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

    @CreationTimestamp
    @Column(nullable = false,updatable = false,insertable = false)
    private LocalTime createAt = LocalTime.now();

    @Column()
    private String description;

    @Column(nullable = false)
    private String name;


    @ManyToOne( optional = false)
    @JoinColumn(name = "ownerId")
    private User  owner;
    @Column(updatable = false,nullable = false)
    private String ownerId;

    @Column(updatable = false,nullable = false)
    private String pic;

    @CreationTimestamp
    @Column(nullable = false,updatable = false,insertable = false)
    private LocalTime updateAt = LocalTime.now();

    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch =FetchType.LAZY,cascade = CascadeType.ALL)
    private  Set<GroupMember>   members  =new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalTime createAt) {
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

    public LocalTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalTime updateAt) {
        this.updateAt = updateAt;
    }

    public Set<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(Set<GroupMember> members) {
        this.members = members;
    }
}
