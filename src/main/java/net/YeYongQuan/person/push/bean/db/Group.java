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

    @Column()
    private String description;

    @Column(updatable = false,nullable = false)
    private String pic;

    @ManyToOne( optional = false)
    @JoinColumn(name = "ownerId")
    private User  owner;
    @Column(updatable = false,nullable = false)
    private String ownerId;

    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch =FetchType.LAZY,cascade = CascadeType.ALL)
    private  Set<GroupMember>   members  =new HashSet<>();

    @CreationTimestamp
    @Column(nullable = false,updatable = false,insertable = false)
    private LocalTime createAr = LocalTime.now();



}
