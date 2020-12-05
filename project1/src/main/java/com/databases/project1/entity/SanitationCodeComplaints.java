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
@Table(name = "sanitation_code_complaints")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanitationCodeComplaints {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "violation_nature")
    private String violationNature;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id")
    Incident incident;



}
