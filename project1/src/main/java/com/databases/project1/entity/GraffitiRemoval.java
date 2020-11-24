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
@Table(name = "graffiti_removal")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraffitiRemoval {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "surface_type")
    private int surfaceΤype;

    @Column(name = "graffiti_location")
    private String graffitiLocation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="incident_id")
    Incident incident;



}
