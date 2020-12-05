package com.databases.project1.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "incident")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Incident {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(name="creation_date")
    private Date creationDate;

    @Column(name="completion_date")
    private Date completionDate;

    @Column(name="service_request_number")
    private String serviceRequestNumber;

    @Column(name="street_address")
    private String streetAdress;

    @Column(name="longitude")
    private Float longitude;

    @Column(name="latitude")
    private Float latitude;

    @Column(name="y_coordinate")
    private Float y;

    @Column(name="x_coordinate")
    private Float x;

    @JoinColumn(name="request_type")
    String requestType;

    @JoinColumn(name="status_type")
    String statusType;

    @ManyToOne(cascade= { CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="district_id")
    District district;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id")
    ExtraIncidentInfo extraIncidentInfo;

    @OneToOne(mappedBy = "incident")
    AbandonedVehicle abandonedVehicle;

    @OneToOne(mappedBy = "incident")
    AlleyLightsOut alleyLightsOut;

    @OneToOne(mappedBy = "incident")
    GarbageCarts garbageCarts;

    @OneToOne(mappedBy = "incident")
    PotHolesReported potHolesReported;

    @OneToOne(mappedBy = "incident")
    RodentBaiting rodentBaiting;

    @OneToOne(mappedBy = "incident")
    SanitationCodeComplaints sanitationCodeComplaints;

    @OneToOne(mappedBy = "incident")
    TreeDebris treeDebris;

    @OneToOne(mappedBy = "incident")
    TreeTrims treeTrims;

    @OneToOne(mappedBy = "incident")
    GraffitiRemoval graffitiRemoval;


}
