package com.databases.project1.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "request_type")
@Getter
@Setter
public class RequestType {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "request_type_id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID requestTypeId;


    @Column(name = "name")
    private String name;


}
