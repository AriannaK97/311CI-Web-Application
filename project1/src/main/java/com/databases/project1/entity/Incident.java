package com.databases.project1.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;
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
    @Column(name = "incident_id", unique = true, nullable = false)
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
    private float longitude;

    @Column(name="latitude")
    private float latitude;

    @Column(name="y_coordinate")
    private float y;

    @Column(name="x_coordinate")
    private float x;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="id")
    RegisteredUser user;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="request_type_id")
    RequestType requestType;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="STATUS_TYPE_ID")
    StatusType statusType;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="district_id")
    District district;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="extra_incident_info_id")
    ExtraIncidentInfo extraIncidentInfo;


}
