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
@Table(name = "tree_trims")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TreeTrims {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "tree_location")
    private String treeLocation;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="incident_id")
    Incident incident;


}
