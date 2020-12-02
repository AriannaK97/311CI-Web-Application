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
    private String creation_date = null;
    private String completion_date = null;
    private String serviceRequestNumber = null;
    private Float longitude = null;
    private Float latitude = null;
    private String requestType = null;
    private int searchType = 0;

}
