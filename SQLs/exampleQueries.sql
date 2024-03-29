--Query 1
-- 1.125ms
--970ms
select count(*) cnt, I.request_type from incident I
where CREATION_DATE between '1920-09-10' and '2020-03-18'
group by I.request_type;

--Query 2
--643 ms
--125 ms
select count(*),creation_date from incident where request_type = 'Pothole in Street'
                                               and creation_date <= '2020-03-07' and creation_date >= '1920-03-03'
group by creation_date;

--Query 3
--462ms
--397ms
select * from (
    select cnt, request_type, ZIP_CODE, rank() over (partition by ZIP_CODE order by cnt desc) as req_rank
    from (
           select count(*) cnt, I.request_type, d.ZIP_CODE
           from INCIDENT I
                    inner join DISTRICT D on D.ID = I.DISTRICT_ID
           where CREATION_DATE = '2017-01-18'
           group by I.request_type, d.ZIP_CODE
           order by d.ZIP_CODE, I.request_type, count(*)
       ) tmp
) tmp1 where tmp1.req_rank = 1;

--Query 4
--603ms
--232ms
select avg(completion_date - creation_date), request_type from incident
where creation_date <= '2020-09-07' and creation_date >= '2012-03-03' and completion_date is not null
group by request_type;


--Query 5
--933ms
--480ms
select request_type, cnt from (
    select request_type, cnt, rank() over (order by cnt desc) as rnk
    from(
          select  request_type, count(*) cnt
          from incident
          where longitude <= -87.62371063232422 and longitude >= -87.70037841796875
            and latitude <= 41.80963897705078 and latitude >= 41.80550003051758 and creation_date='2017-01-18'
          group by request_type
      ) tmp
) tmp1
where rnk=1;

--Query 6
--17.878ms
--3.711ms
select sum(c) total , n from (select ssa as n,count(*) as c from garbage_carts join incident on incident.id = garbage_carts.id
                              where ssa is not null and creation_date <= '2020-09-07' and creation_date >= '1920-03-03'
                              group by ssa
                              union
                              select ssa,count(*) as n from abandoned_vehicle join incident on incident.id = abandoned_vehicle.id
                              where ssa is not null and creation_date <= '2020-09-07' and creation_date >= '1920-03-03'
                              group by ssa
                              union
                              select ssa,count(*) as n from pot_holes_reported join incident on incident.id = pot_holes_reported.id
                              where ssa is not null and creation_date <= '2020-09-07' and creation_date >= '1920-03-03'
                              group by ssa
                              union
                              select ssa,count(*) as n from graffiti_removal join incident on incident.id = graffiti_removal.id
                              where ssa is not null and creation_date <= '2020-09-07' and creation_date >= '1920-03-03'
                              group by ssa) ssa_totals group by n order by total desc limit 5 ;

--Query 7
--1.205ms
--510 s
select count(i.id),license_plate  from ABANDONED_VEHICLE a inner join incident I on a.id=I.id
where I.request_type = 'Abandoned Vehicle Complaint'
group by license_plate
having count(i.id)> 1;

--Query 8
--99ms
--68ms
select count(*), vehicle_color from abandoned_vehicle
group by  vehicle_color
order by count(*) desc offset 1 limit 1;

--Query 9
--690ms
--127
select *  from incident
                   inner join rodent_baiting on incident.id = rodent_baiting.id
                   inner join district on incident.district_id = district.id
                   inner join extra_incident_info on incident.id = extra_incident_info.id
where baited_premises_num < 2;

--Query 10
--269ms
--92ms
select *  from incident
                inner join rodent_baiting on incident.id = rodent_baiting.id
                inner join district on incident.district_id = district.id
                inner join extra_incident_info on incident.id = extra_incident_info.id
where premises_with_garbage_num < 2

--Query 11
--135ms
--95ms
select *  from incident
                inner join rodent_baiting on incident.id = rodent_baiting.id
                inner join district on incident.district_id = district.id
                inner join extra_incident_info on incident.id = extra_incident_info.id
where premises_with_rats_num < 2;

--Query 12
--13.642ms
--808ms
select distinct(police_district) from district
                                          inner join incident potHolesIncident on district.id = potHolesIncident.district_id
                                          inner join pot_holes_reported on potHolesIncident.id = pot_holes_reported.id
                                          inner join incident rodentsIncident on rodentsIncident.district_id = rodentsIncident.district_id
                                          inner join rodent_baiting on rodentsIncident.id = rodent_baiting.id
where filled_block_potholes_num > 1 and potHolesIncident.completion_date = '2018-03-07'
  and rodentsIncident.completion_date= '2018-03-07' and baited_premises_num > 1;



