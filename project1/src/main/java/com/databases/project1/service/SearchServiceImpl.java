package com.databases.project1.service;

import com.databases.project1.dto.SearchDto;
import com.databases.project1.entity.Incident;
import com.databases.project1.entity.RegisteredUser;
import com.databases.project1.entity.UserActionLog;
import com.databases.project1.repository.IncidentRepository;
import com.databases.project1.repository.UserActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

    @Autowired
    IncidentRepository incidentRepository;

    @Autowired
    UserActionLogRepository logRepository;

    public UserActionLog initializeUserActionLog(RegisteredUser user) {
        UserActionLog log = new UserActionLog();
        log.setActionTimeStamp(new Timestamp(System.currentTimeMillis()));
        log.setRegUser(user);
        log.setUserName(user.getUserName());
        return log;
    }


    public List<Object[]> findTotalRequestsPerType(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for requests per type with arguments: " + searchDto.toString());
        logRepository.save(log);
        return incidentRepository.findTotalRequestsPerType(LocalDate.parse(searchDto.getFirstDate(), formatter), LocalDate.parse(searchDto.getSecondDate(), formatter));
    }


    public List<Object[]> findTotalRequestsPerDayAndType(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for a request type count per day with arguments: " + searchDto.toString() );
        logRepository.save(log);
        return incidentRepository.findTotalRequestsPerDayAndType(LocalDate.parse(searchDto.getFirstDate(), formatter),
                LocalDate.parse(searchDto.getSecondDate()), searchDto.getRequestType(), searchDto.getPage(), searchDto.getPageSize());
    }


    public List<Object[]> findMostCommonServiceRequestPerZipCode(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for most common service request type per zip code with arguments: " + searchDto.toString());
        logRepository.save(log);
        return incidentRepository.findMostCommonServiceRequestPerZipCode(LocalDate.parse(searchDto.getCreationDate(), formatter),
                searchDto.getPage(), searchDto.getPageSize());
    }


    public List<Object[]> findAvgCompletionTimePerServiceReqType(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for average completion time per service request with arguments: " + searchDto.toString());
        logRepository.save(log);
        return incidentRepository.findAvgCompletionTimePerServiceReqType(LocalDate.parse(searchDto.getFirstDate(), formatter),
                LocalDate.parse(searchDto.getSecondDate()), searchDto.getPage(), searchDto.getPageSize());
    }


    public List<Object[]> findMostCommonReqType(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for most common request type in a bounding box with arguments: " + searchDto.toString());
        logRepository.save(log);
        return incidentRepository.findMostCommonReqType(searchDto.getFirstLongitude(), searchDto.getSecondLongitude(),
                searchDto.getFirstLatitude(), searchDto.getSecondLatitude(), LocalDate.parse(searchDto.getCreationDate(), formatter));
    }

    public List<Object[]> findTopSSAs(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Seach for top SSAs with arguments: " + searchDto.toString());
        logRepository.save(log);
        return incidentRepository.findTopSSAs(LocalDate.parse(searchDto.getFirstDate(), formatter),
                LocalDate.parse(searchDto.getSecondDate()), searchDto.getPage(), searchDto.getPageSize());
    }


    public List<Object[]> findNotoriousPlates(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for notorious plates");
        logRepository.save(log);
        return incidentRepository.findNotoriousPlates(searchDto.getPage(), searchDto.getPageSize());
    }

    public List<Object[]> findSecondMostUsualVehicleColor(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for second most usual vehicle color");
        logRepository.save(log);
        return  incidentRepository.findSecondMostUsualVehicleColor();
    }


    public List<Incident> findRodentBaitingRequestsByBaitedPremises(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for baited premises with arguments: " + searchDto.toString() );
        logRepository.save(log);
        return incidentRepository.findRodentBaitingRequestsByBaitedPremises(searchDto.getPremisesBaited(),searchDto.getPage(), searchDto.getPageSize());

    }

    public List<Incident> findRodentBaitingRequestsByGarbagePremises(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for premises with garbage  with arguments: " + searchDto.toString());
        logRepository.save(log);
        return incidentRepository.findRodentBaitingRequestsByGarbagePremises(searchDto.getGarbagePremises(),searchDto.getPage(), searchDto.getPageSize());
    }

    public List<Incident> findRodentBaitingRequestsByRatPremises(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for premises with rats with arguments: " + searchDto.toString() );
        logRepository.save(log);
        return incidentRepository.findRodentBaitingRequestsByRatPremises(searchDto.getRatPremises(), searchDto.getPage(), searchDto.getPageSize());
    }

    public List<Object[]> findBusyPoliceDistricts(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for busy police districts with arguments: " + searchDto.toString());
        logRepository.save(log);
        return incidentRepository.findBusyPoliceDistricts(LocalDate.parse(searchDto.getCompletionDate(), formatter), searchDto.getPotholes(),
                searchDto.getPremisesBaited(),searchDto.getPage(), searchDto.getPageSize());
    }

    public List<Incident> findByZipCode(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for requests by zipcode with arguments: " + searchDto.toString() );
        logRepository.save(log);
        return incidentRepository.findByZipCode(searchDto.getZipcode(), searchDto.getPage(), searchDto.getPageSize());
    }

    public List<Incident> findByStreetAddress(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for requests by street address with arguments: " + searchDto.toString() );
        logRepository.save(log);
        return incidentRepository.findByStreetAddress(searchDto.getStreetAddress(), searchDto.getPage(), searchDto.getPageSize());
    }

    public List<Incident> findByStreetAddressAndZipCode(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for requests by street address and zip code with arguments: " + searchDto.toString() );
        logRepository.save(log);
        return incidentRepository.findByStreetAddressAndZipCode(searchDto.getZipcode(), searchDto.getStreetAddress(), searchDto.getPage(), searchDto.getPageSize());
    }

    public List<Incident> findAll(SearchDto searchDto, RegisteredUser user) {
        UserActionLog log = initializeUserActionLog(user);
        log.setUserAction("Search for requests by street address and zip code with arguments: " + searchDto.toString() );
        logRepository.save(log);
        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getPageSize());
        return incidentRepository.findAll(pageable).toList();
    }













}
