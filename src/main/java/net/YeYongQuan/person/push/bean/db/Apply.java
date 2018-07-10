package net.YeYongQuan.person.push.bean.db;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "TB_APPLY")
public class Apply {
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;



    @ManyToOne(optional = false)
    @JoinColumn(name = "applicaterId")
    private User applicater;
    @Column(updatable = false) //申请人可以为null ，  系统消息
    private String applicaterId;

    @Column()
    private String attach;


    @CreationTimestamp
    @Column(updatable = false,nullable = false)
    private LocalTime createAt =  LocalTime.now();

    @Column(updatable = false,nullable = false)
    private String description;

    @Column(updatable = false,nullable = false)
    private String targetId;

    @Column(updatable = false,nullable = false)
    private int type;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalTime updateAt =  LocalTime.now();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getApplicater() {
        return applicater;
    }

    public void setApplicater(User applicater) {
        this.applicater = applicater;
    }

    public String getApplicaterId() {
        return applicaterId;
    }

    public void setApplicaterId(String applicaterId) {
        this.applicaterId = applicaterId;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
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

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalTime updateAt) {
        this.updateAt = updateAt;
    }
}
