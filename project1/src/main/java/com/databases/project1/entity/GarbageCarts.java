package com.databases.project1.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "garbage_carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GarbageCarts {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "delivered_black_carts_num")
    private int deliveredBlackCartsNum;

    @Column(name = "current_activity")
    private String currentActivity;

    @Column(name = "most_recent_action")
    private String mostRecentAction;

    @Column(name = "SSA")
    private int SSA;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="incident_id")
    Incident incident;


}
