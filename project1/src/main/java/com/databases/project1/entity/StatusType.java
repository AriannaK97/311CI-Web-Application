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
@Table(name = "status_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusType {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "STATUS_TYPE_ID", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(name = "name")
    private int name;

}
