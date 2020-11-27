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
@Table(name = "abandoned_vehicle")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbandonedVehicle {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "license_plate", columnDefinition = "TEXT")
    private String licensePlate;

    @Column(name = "vehicle_make_model")
    private String vehicleMakeModel;

    @Column(name = "vehicle_color")
    private String vehicleColor;

    @Column(name = "current_activity")
    private String currentActivity;

    @Column(name = "most_recent_action")
    private String mostRecentAction;

    @Column(name = "days_reported_parked")
    private float daysReportedParked;

    @Column(name = "SSA")
    private int SSA;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id")
    Incident incident;


}
