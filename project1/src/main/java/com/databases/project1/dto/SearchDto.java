package com.databases.project1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {
    private String firstDate = "";
    private String secondDate = "";
    private String serviceRequestNumber = "";
    private BigDecimal firstLongitude = null;
    private BigDecimal secondLongitude = null;
    private BigDecimal firstLatitude = null;
    private BigDecimal secondLatitude = null;
    private String requestType = "";
    private Integer premisesBaited = 0;
    private Integer garbagePremises = 0;
    private Integer ratPremises = 0;
    private Integer zipcode = 0;
    private String streetAddress = "";
    private Integer potholes = 0;
    private Integer searchType = 0;
    private int page = 0;
    private int pageSize = 50;


    @Override
    public String toString() {

        StringBuilder str
                = new StringBuilder();

        str.append(!firstDate.isEmpty() ?"FirstDate:" + firstDate + " ": firstDate);
        str.append(!secondDate.isEmpty() ?"SecondDate:" + secondDate + " ": secondDate);
        str.append(!serviceRequestNumber.isEmpty() ?"ServiceNumber:" + serviceRequestNumber + " ": serviceRequestNumber);
        str.append(!streetAddress.isEmpty() ? "StreetAdress:" + streetAddress + " ": streetAddress);
        str.append(!requestType.isEmpty() ? "RequestType:" + requestType + " ": requestType);
        str.append(firstLongitude != null ?"First Longitude:" + firstLongitude + " ": "");
        str.append(secondLongitude != null ? "SecondLongitude:" + secondLongitude + " ": "");
        str.append(firstLatitude != null ?"FirstLatidute:" + firstLatitude + " ": "");
        str.append(secondLatitude != null ? "SecondLatitude:" + secondLatitude + " ": "");
        str.append(premisesBaited != null ? "PremisesBaited:" + premisesBaited + " ": "");
        str.append(garbagePremises != null ?"GarbagePremises:" + garbagePremises + " ": "");
        str.append(ratPremises != null ? "RatPremises:" + ratPremises + " ": "");
        str.append(zipcode != null ? "Zipcode:" + zipcode + " ": "");
        str.append(searchType != null ? "SearchType:" + searchType + " ": "");
        str.append("Page:" + page);

        return str.toString();
    }
}
