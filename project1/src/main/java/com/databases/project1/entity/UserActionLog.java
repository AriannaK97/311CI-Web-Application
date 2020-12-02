package com.databases.project1.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "user_action_log")
@Getter
@Setter
public class UserActionLog {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "USER_ACTION_LOG_ID", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "username")
    private String userName;

    @Column(name="action_time_stamp")
    private Timestamp actionTimeStamp;

    @Column(name="action")
    private String userAction;

    @ManyToOne
    @JoinColumn(name = "registered_user_id")
    private RegisteredUser regUser;

}
