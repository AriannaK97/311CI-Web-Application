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
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "historical_wards_2003_2015")
    private Integer historical_wards_2003_2015;

    @Column(name = "zip_codes")
    private Integer zipcode;

    @Column(name = "wards")
    private Integer ward;

    @Column(name = "community_areas")
    private Integer communityArea;

    @Column(name = "cencus_tracts")
    private Integer cencusTracts;


}
