package com.databases.project1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {
    private String creationDate = "";
    private String firstDate = "";
    private String secondDate = "";
    private String completionDate = "";
    private String serviceRequestNumber = "";
    private Float firstLongitude = null;
    private Float secondLongitude = null;
    private Float firstLatitude = null;
    private Float secondLatitude = null;
    private String requestType = null;
    private Integer premisesBaited = 0;
    private Integer garbagePremises = 0;
    private Integer ratPremises = 0;
    private Integer zipcode = 0;
    private String streetAddress = "";
    private Integer potholes = 0;
    private int searchType = 0;
    private int page = 0;
    private int pageSize = 50;

    //TODO : BETTER TO STRING METHOD

    @Override
    public String toString() {
        return creationDate + " " + firstDate + " " + secondDate + " " + completionDate + " " + serviceRequestNumber
                + " " + firstLongitude + " " + firstLatitude + " " + requestType + " "  + page + " " + pageSize;
    }
}
