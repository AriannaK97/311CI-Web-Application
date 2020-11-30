package com.databases.project1.service;

import java.time.LocalDate;
import java.util.List;

public interface SearchRequestService {

    public List<Object[]> findtotalRequestsPerType(LocalDate firstDate, LocalDate secondDate);

}
