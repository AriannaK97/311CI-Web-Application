package com.databases.project1.service;

import com.databases.project1.dto.SearchDto;
import com.databases.project1.entity.Incident;
import com.databases.project1.entity.RegisteredUser;
import com.databases.project1.entity.UserActionLog;
import com.databases.project1.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface SearchService {

    public UserActionLog initializeUserActionLog(RegisteredUser user);
    public List<Object[]> findTotalRequestsPerType(SearchDto searchDto, RegisteredUser user);
    public List<Object[]> findTotalRequestsPerDayAndType(SearchDto searchDto, RegisteredUser user);
    public List<Object[]> findMostCommonServiceRequestPerZipCode(SearchDto searchDto, RegisteredUser user);
    public List<Object[]> findAvgCompletionTimePerServiceReqType(SearchDto searchDto, RegisteredUser user);
    public List<Object[]> findMostCommonReqType(SearchDto searchDto, RegisteredUser user);
    public List<Object[]> findTopSSAs(SearchDto searchDto, RegisteredUser user);
    public List<Object[]> findNotoriousPlates(SearchDto searchDto, RegisteredUser user);
    public List<Object[]> findSecondMostUsualVehicleColor(SearchDto searchDto, RegisteredUser user);
    public List<Incident> findRodentBaitingRequestsByBaitedPremises(SearchDto searchDto, RegisteredUser user);
    public List<Incident> findRodentBaitingRequestsByGarbagePremises(SearchDto searchDto, RegisteredUser user);
    public List<Incident> findRodentBaitingRequestsByRatPremises(SearchDto searchDto, RegisteredUser user);
    public List<Object[]> findBusyPoliceDistricts(SearchDto searchDto, RegisteredUser user);
    public List<Incident> findByZipCode(SearchDto searchDto, RegisteredUser user);
    public List<Incident> findByStreetAddress(SearchDto searchDto, RegisteredUser user);
    public List<Incident> findByStreetAddressAndZipCode(SearchDto searchDto, RegisteredUser user);

}
