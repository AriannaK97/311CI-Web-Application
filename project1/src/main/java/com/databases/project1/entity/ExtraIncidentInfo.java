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
@Table(name = "EXTRA_INCIDENT_INFO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExtraIncidentInfo {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "EXTRA_INCIDENT_INFO_ID", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "zip_codes")
    private int zipcode;

    @Column(name = "wards")
    private int ward;

    @Column(name = "police_districts")
    private int policeDistrict;

    @Column(name = "community_areas")
    private int communityArea;

    @Column(name = "cencus_tracts")
    private int cencusTracts;


}
