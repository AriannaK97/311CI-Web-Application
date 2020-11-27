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
@Table(name = "rodent_baiting")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RodentBaiting {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "baited_premises_num")
    private float baitedPremisesNum;

    @Column(name = "premises_with_garbage_num")
    private float premisesWithGarbageNum;

    @Column(name = "premises_with_rats_num")
    private float premisesWithRatsNum;

    @Column(name = "current_activity")
    private String currentActivity;

    @Column(name = "most_recent_action")
    private String mostRecentAction;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id")
    Incident incident;


}
