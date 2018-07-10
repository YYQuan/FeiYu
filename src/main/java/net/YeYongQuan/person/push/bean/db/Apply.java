package net.YeYongQuan.person.push.bean.db;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

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

    @Column(updatable = false,nullable = false)
    private String description;

    @Column(updatable = false,nullable = false)
    private String applicater;

    @Column(updatable = false,nullable = false)
    private int type;

    @Column(updatable = false,nullable = false)
    private String targetId;


    @Column()
    private String attach;

    @CreationTimestamp
    @Column(updatable = false,nullable = false)
    private LocalTime createAt =  LocalTime.now();
}
