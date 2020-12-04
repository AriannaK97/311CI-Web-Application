package com.databases.project1.repository;

import com.databases.project1.entity.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository extends PagingAndSortingRepository<Incident, UUID> {

    Optional<Incident> findByServiceRequestNumber(String serviceRequestNumber);

    @Query(value = "select count(*) cnt, I.request_type from incident I\n" +
            "where CREATION_DATE between :secondDate and :firstDate \n" +
            "group by I.request_type;", nativeQuery = true )
    public List<Object[]> findTotalRequestsPerType(@Param("firstDate") LocalDate firstDate, @Param("secondDate") LocalDate secondDate);

    @Query(value = "select count(*),creation_date from incident where request_type = :requestType \n" +
            "                                               and creation_date <= :firstDate and creation_date >= :secondDate \n" +
            "group by creation_date limit :pagesize offset :page * :pagesize ;", nativeQuery = true)
    public List<Object[]> findTotalRequestsPerDayAndType(@Param("firstDate") LocalDate firstDate, @Param("secondDate") LocalDate secondDate,
                                                         @Param("requestType") String requestType, @Param("page") int page, @Param("pagesize") int pagesize);

    @Query(value = "select * from (\n" +
            "      select cnt, request_type, ZIP_CODE, rank() over (partition by ZIP_CODE order by cnt desc) as req_rank\n" +
            "      from (\n" +
            "               select count(*) cnt, I.request_type, d.ZIP_CODE\n" +
            "               from INCIDENT I\n" +
            "                        inner join DISTRICT D on D.ID = I.DISTRICT_ID\n" +
            "               where CREATION_DATE = :creationDate \n" +
            "               group by I.request_type, d.ZIP_CODE\n" +
            "               order by d.ZIP_CODE, I.request_type, count(*)\n" +
            "           ) tmp\n" +
            ") tmp1 where tmp1.req_rank = 1 limit :pagesize offset :page * :pagesize ;", nativeQuery = true)
    public List<Object[]> findMostCommonServiceRequestPerZipCode(@Param("creationDate") LocalDate creationDate, @Param("page") int page, @Param("pagesize") int pagesize);

    @Query(value = "select avg(completion_date - creation_date), request_type from incident\n" +
            "where creation_date <= :firstDate and creation_date >= :secondDate and completion_date is not null\n" +
            "group by request_type limit :pagesize offset :page * :pagesize ;", nativeQuery = true)
    public List<Object[]> findAvgCompletionTimePerServiceReqType(@Param("firstDate") LocalDate firstDate, @Param("secondDate") LocalDate secondDate,
                                                                 @Param("page") int page, @Param("pagesize") int pagesize);

    @Query(value = "select request_type, cnt from (\n" +
            "    select request_type, cnt, rank() over (order by cnt desc) as rnk\n" +
            "    from(\n" +
            "        select  request_type, count(*) cnt\n" +
            "        from incident\n" +
            "        where longitude <= :firstLongitude and longitude >= :secondLongitude" +
            "          and latitude <= :firstLatitude and latitude >= :secondLatitude and creation_date= :creationDate\n" +
            "        group by request_type\n" +
            "        ) tmp\n" +
            "    ) tmp1\n" +
            "where rnk=1 limit :pagesize offset :page * :pagesize ;",nativeQuery = true)
    public List<Object[]> findMostCommonReqType(@Param("firstLongitude") Float firstLongitude, @Param("secondLongitude") Float secondLongitude,
                                                @Param("firstLatitude") Float firstLatitude, @Param("secondLatitude") Float secondLatitude,
                                                @Param("creationDate") LocalDate creationDate);

    @Query(value = "select sum(c) total , ssa from (select ssa,count(*) as c from garbage_carts join incident on incident.id = garbage_carts.id\n" +
            "                              where ssa is not null and creation_date <= :firstDate and creation_date >= :secondDate\n" +
            "                              group by ssa\n" +
            "                              union\n" +
            "                              select ssa,count(*) as c from abandoned_vehicle join incident on incident.id = abandoned_vehicle.id\n" +
            "                              where ssa is not null and creation_date <= :firstDate and creation_date >= :secondDate\n" +
            "                              group by ssa\n" +
            "                              union\n" +
            "                              select ssa,count(*) as c from pot_holes_reported join incident on incident.id = pot_holes_reported.id\n" +
            "                              where ssa is not null and creation_date <= :firstDate and creation_date >= :secondDate\n" +
            "                              group by ssa\n" +
            "                              union\n" +
            "                              select ssa,count(*) as c from graffiti_removal join incident on incident.id = graffiti_removal.id\n" +
            "                              where ssa is not null and creation_date <= :firstDate and creation_date >= :secondDate\n" +
            "                              group by ssa) ssa_totals group by ssa order by total desc limit 5 ;", nativeQuery = true)
    public List<Object[]>  findTopSSAs(@Param("firstDate") LocalDate firstDate, @Param("secondDate") LocalDate secondDate);

    @Query(value = "select count(i.id),license_plate  from ABANDONED_VEHICLE a\n" +
            "        inner join incident I on a.id=I.id\n" +
            "        where I.request_type = 'Abandoned Vehicle Complaint'\n" +
            "group by license_plate\n" +
            "having count(i.id)> 1 limit :pagesize offset :page * :pagesize ;",nativeQuery = true)
    public List<Object[]> findNotoriousPlates(@Param("page") int page, @Param("pagesize") int pagesize);

    @Query(value = "select count(*), vehicle_color from abandoned_vehicle\n" +
            "group by  vehicle_color\n" +
            "order by count(*) desc limit :pagesize offset :page * :pagesize ;",nativeQuery = true)
    public List<Object[]> findSecondMostUsualVehicleColor();


    @Query(value = "select *  from incident\n" +
            "                   inner join rodent_baiting on incident.id = rodent_baiting.id\n" +
            "                   inner join district on incident.district_id = district.id\n" +
            "                   inner join extra_incident_info on incident.id = extra_incident_info.id\n" +
            "where baited_premises_num < :premisesBaited limit :pagesize offset :page * :pagesize ;", nativeQuery = true)
    public List<Incident> findRodentBaitingRequestsByBaitedPremises(@Param("premisesBaited") int premisesBaited, @Param("page") int page, @Param("pagesize") int pagesize);

    @Query(value = "select *  from incident\n" +
            "                inner join rodent_baiting on incident.id = rodent_baiting.id\n" +
            "                inner join district on incident.district_id = district.id\n" +
            "                inner join extra_incident_info on incident.id = extra_incident_info.id\n" +
            "where premises_with_garbage_num < :garbagePremises limit :pagesize offset :page * :pagesize ;", nativeQuery = true)
    public List<Incident> findRodentBaitingRequestsByGarbagePremises(@Param("garbagePremises") int garbagePremises, @Param("page") int page, @Param("pagesize") int pagesize);

    @Query(value = "select *  from incident\n" +
            "                inner join rodent_baiting on incident.id = rodent_baiting.id\n" +
            "                inner join district on incident.district_id = district.id\n" +
            "                inner join extra_incident_info on incident.id = extra_incident_info.id\n" +
            "where premises_with_rats_num < :ratPremises  limit :pagesize offset :page * :pagesize ;", nativeQuery = true)
    public List<Incident> findRodentBaitingRequestsByRatPremises(@Param("ratPremises") int ratPremises, @Param("page") int page, @Param("pagesize") int pagesize);


    @Query(value = "select distinct(police_district) from district\n" +
            "                                          inner join incident potHolesIncident on district.id = potHolesIncident.district_id\n" +
            "                                          inner join pot_holes_reported on potHolesIncident.id = pot_holes_reported.id\n" +
            "                                          inner join incident rodentsIncident on rodentsIncident.district_id = rodentsIncident.district_id\n" +
            "                                          inner join rodent_baiting on rodentsIncident.id = rodent_baiting.id\n" +
            "where filled_block_potholes_num > :potholes and potHolesIncident.completion_date = :completionDate\n" +
            "and rodentsIncident.completion_date= :completionDate and baited_premises_num > :premisesBaited limit :pagesize offset :page * :pagesize ;",nativeQuery = true)
    public List<Object[]> findBusyPoliceDistricts(@Param("completionDate") LocalDate completionDate, @Param("potholes") Integer potholes, @Param("premisesBaited") int premisesBaited,
                                                  @Param("page") int page, @Param("pagesize") int pagesize);

    @Query(value = "select * from incident inner join district on district.id = incident.district_id\n" +
            "where zip_code = :zipcode limit :pagesize offset :page * :pagesize ;", nativeQuery = true)
    public List<Incident> findByZipCode(@Param("zipcode")Integer zipcode, @Param("page") int page, @Param("pagesize")int pagesize);

    @Query(value = "select * from incident where street_address= :streetAddress limit :pagesize offset :page * :pagesize ; ", nativeQuery = true)
    public List<Incident> findByStreetAddress(@Param("streetAddress") String streetAddress, @Param("page") int page, @Param("pagesize") int pagesize);

    @Query(value = "select * from incident inner join district on district.id = incident.district_id\n" +
            "where street_address = :streetAddress and zip_code = :zipcode limit :pagesize offset :page * :pagesize ;", nativeQuery = true)
    public List<Incident> findByStreetAddressAndZipCode(@Param("zipcode") Integer zipcode, @Param("streetAddress") String streetAddress, @Param("page") int page, @Param("pagesize") int pagesize);

}

