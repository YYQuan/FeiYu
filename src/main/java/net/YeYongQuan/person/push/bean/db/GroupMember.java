package net.YeYongQuan.person.push.bean.db;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "TB_GROUP_MEMBER")
public class GroupMember {
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;


    @JoinColumn(name = "groupId")
    @ManyToOne(optional = false)
    private Group  group;
    @Column(nullable = false ,updatable = false,insertable = false)
    private String groupId;

    @Column(nullable = false ,updatable = false,insertable = false)
    private String targetId;

    private LocalTime createAt = LocalTime.now();
}
