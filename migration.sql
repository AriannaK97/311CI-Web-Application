-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
create unique index district_id_uindex
	on district (community_area, police_district, ward, zip_code);

--1) Insert Abandoned vehicle


INSERT INTO request_type (request_type_id, name)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.type_of_service_request::VARCHAR from
            (SELECT distinct type_of_service_request
            FROM import._311_service_requests_abandoned_vehicles
            ) tmp;

INSERT INTO district (id, zip_code, ward, police_district, community_area)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from
            (SELECT distinct
                    case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code,
                    case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area,
                    case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district,
                    case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward
            FROM import._311_service_requests_abandoned_vehicles
            ) tmp
        where not exists(select * from district d where d.zip_code=tmp.zip_code::INTEGER and d.ward=tmp.ward::INTEGER
                        and d.police_district = tmp.police_district::INTEGER and d.community_area=tmp.community_area::INTEGER);

ALTER TABLE import._311_service_requests_abandoned_vehicles
ADD incident_id uuid;

update import._311_service_requests_abandoned_vehicles set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring);

INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)
select tmp.incident_id::uuid, tmp.historical_wards_2003_2015::INTEGER, tmp.community_areas::INTEGER,
       tmp.census_tracts::INTEGER, tmp.zip_codes::INTEGER, tmp.wards::INTEGER from
            (SELECT incident_id,
                    case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0')  end wards,
                    case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0')  end zip_codes,
                    case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0')  end community_areas,
                    case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0')  end historical_wards_2003_2015,
                    case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0')  end census_tracts
            FROM import._311_service_requests_abandoned_vehicles
            ) tmp
;

INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,
                      x_coordinate, y_coordinate, request_type, status_type, district_id)
                      select tmp.incident_id::uuid,
                             tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,
                             tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,
                             tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,
                             tmp.status::VARCHAR, tmp.d_id::uuid from
                                                      (SELECT v.incident_id,
                                                              case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,
                                                              case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,
                                                              street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,
                                                              case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,
                                                              case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,
                                                              case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,
                                                              type_of_service_request, status, d.id d_id
                                                      FROM import._311_service_requests_abandoned_vehicles v
                                                      inner join district d on
                                                            (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area
                                                            and
                                                            (case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  =d.zip_code
                                                            and
                                                            (case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district
                                                            and
                                                            (case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER =d.ward) as tmp;

INSERT INTO abandoned_vehicle (id, ssa, license_plate, vehicle_make_model, vehicle_color, current_activity,
                               most_recent_action, days_reported_parked)
                      select tmp.incident_id::uuid,
                             tmp.ssa::INTEGER , tmp.license_plate::VARCHAR, tmp.vehicle_make_model::VARCHAR,
                             tmp.vehicle_color::VARCHAR, tmp.current_activity::VARCHAR, tmp.most_recent_action::VARCHAR,
                             tmp.days_reported_parked::FLOAT from
                                                      (SELECT incident_id,
                                                              case when coalesce(ssa, '0') = '' then NULL else coalesce(ssa, '0')  end ssa,
                                                              license_plate, vehicle_make_model, vehicle_color, current_activity, most_recent_action,
                                                              case when coalesce(how_many_days_has_the_vehicle_been_reported_as_parked, '0') = '' then NULL else coalesce(how_many_days_has_the_vehicle_been_reported_as_parked, 'NULL')  end days_reported_parked
                                                      FROM import._311_service_requests_abandoned_vehicles) as tmp;

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--2) Insert AlleyLights

INSERT INTO request_type (request_type_id, name)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.type_of_service_request::VARCHAR from
            (SELECT distinct type_of_service_request
            FROM import._311_service_requests_alley_lights_out
            ) tmp;

INSERT INTO district (id, zip_code, ward, police_district, community_area)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from
            (SELECT distinct
                    case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code,
                    case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area,
                    case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district,
                    case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward
            FROM import._311_service_requests_alley_lights_out
            ) tmp
        where not exists(select * from district d where d.zip_code=tmp.zip_code::INTEGER and d.ward=tmp.ward::INTEGER
                        and d.police_district = tmp.police_district::INTEGER and d.community_area=tmp.community_area::INTEGER);

ALTER TABLE import._311_service_requests_alley_lights_out
ADD incident_id uuid;

update import._311_service_requests_alley_lights_out set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring);

INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)
select tmp.incident_id::uuid, tmp.historical_wards_2003_2015::INTEGER, tmp.community_areas::INTEGER,
       tmp.census_tracts::INTEGER, tmp.zip_codes::INTEGER, tmp.wards::INTEGER from
            (SELECT incident_id,
                    case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0')  end wards,
                    case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0')  end zip_codes,
                    case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0')  end community_areas,
                    case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0')  end historical_wards_2003_2015,
                    case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0')  end census_tracts
            FROM import._311_service_requests_alley_lights_out
            ) tmp
;


INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude, x_coordinate, y_coordinate, request_type, status_type, district_id)
select tmp.incident_id,
     tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,
     tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,
     tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,
     tmp.status::VARCHAR, tmp.d_id::uuid from
      (SELECT v.incident_id,
          case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,
          case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,
          street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,
          case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,
          case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,
          case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,
          type_of_service_request, status, d.id d_id
      FROM import._311_service_requests_alley_lights_out v
      inner join district d on
        (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area
        and
        (case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  =d.zip_code
        and
        (case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district
        and
        (case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER =d.ward) as tmp
        WHERE TMP.incident_id='9c4fed9c-a4ed-5ff2-3b8f-087506515bcb'
              --tmp.incident_id NOT IN (select id from incident)
;

INSERT INTO alley_lights_out (ID) SELECT incident_id FROM import._311_service_requests_alley_lights_out;

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--3) Insert Garbage Carts

INSERT INTO request_type (request_type_id, name)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.type_of_service_request::VARCHAR from
            (SELECT distinct type_of_service_request
            FROM import._311_service_requests_garbage_carts
            ) tmp;

INSERT INTO district (id, zip_code, ward, police_district, community_area)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from
            (SELECT distinct
                    case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code,
                    case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area,
                    case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district,
                    case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward
            FROM import._311_service_requests_garbage_carts
            ) tmp
        where not exists(select * from district d where d.zip_code=tmp.zip_code::INTEGER and d.ward=tmp.ward::INTEGER
                        and d.police_district = tmp.police_district::INTEGER and d.community_area=tmp.community_area::INTEGER);

ALTER TABLE import._311_service_requests_garbage_carts
ADD incident_id uuid;

update import._311_service_requests_garbage_carts set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring);

INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)
select tmp.incident_id::uuid, tmp.historical_wards_2003_2015::INTEGER, tmp.community_areas::INTEGER,
       tmp.census_tracts::INTEGER, tmp.zip_codes::INTEGER, tmp.wards::INTEGER from
            (SELECT incident_id,
                    case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0')  end wards,
                    case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0')  end zip_codes,
                    case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0')  end community_areas,
                    case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0')  end historical_wards_2003_2015,
                    case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0')  end census_tracts
            FROM import._311_service_requests_garbage_carts
            ) tmp
;

INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,
                      x_coordinate, y_coordinate, request_type, status_type, district_id)
                      select tmp.incident_id::uuid,
                             tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,
                             tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,
                             tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,
                             tmp.status::VARCHAR, tmp.d_id::uuid from
                                                      (SELECT v.incident_id,
                                                              case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,
                                                              case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,
                                                              street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,
                                                              case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,
                                                              case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,
                                                              case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,
                                                              type_of_service_request, status, d.id d_id
                                                      FROM import._311_service_requests_garbage_carts v
                                                      inner join district d on
                                                            (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area
                                                            and
                                                            (case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  =d.zip_code
                                                            and
                                                            (case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district
                                                            and
                                                            (case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER =d.ward) as tmp;

INSERT INTO garbage_carts (id, ssa, current_activity, delivered_black_carts_num, most_recent_action)
                      select tmp.incident_id::uuid,
                             tmp.ssa::INTEGER , tmp.current_activity::VARCHAR, tmp.number_of_black_carts_delivered::FLOAT, tmp.most_recent_action::VARCHAR
                             from (SELECT incident_id,
                                          case when coalesce(ssa, '0') = '' then NULL else coalesce(ssa, '0')  end ssa,
                                          current_activity,
                                          case when coalesce(number_of_black_carts_delivered, '0') = '' then NULL else coalesce(number_of_black_carts_delivered, '0')  end number_of_black_carts_delivered,
                                          most_recent_action
                                  FROM import._311_service_requests_garbage_carts) as tmp;

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--4) Insert Graffiti Removal

INSERT INTO request_type (request_type_id, name)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.type_of_service_request::VARCHAR from
            (SELECT distinct type_of_service_request
            FROM import._311_service_requests_graffiti_removal
            ) tmp;

INSERT INTO district (id, zip_code, ward, police_district, community_area)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from
            (SELECT distinct
                    case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code,
                    case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area,
                    case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district,
                    case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward
            FROM import._311_service_requests_graffiti_removal
            ) tmp
        where not exists(select * from district d where d.zip_code=tmp.zip_code::INTEGER and d.ward=tmp.ward::INTEGER
                        and d.police_district = tmp.police_district::INTEGER and d.community_area=tmp.community_area::INTEGER);

ALTER TABLE import._311_service_requests_graffiti_removal
ADD incident_id uuid;

update import._311_service_requests_graffiti_removal set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring);

INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)
select tmp.incident_id::uuid, tmp.historical_wards_2003_2015::INTEGER, tmp.community_areas::INTEGER,
       tmp.census_tracts::INTEGER, tmp.zip_codes::INTEGER, tmp.wards::INTEGER from
            (SELECT incident_id,
                    case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0')  end wards,
                    case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0')  end zip_codes,
                    case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0')  end community_areas,
                    case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0')  end historical_wards_2003_2015,
                    case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0')  end census_tracts
            FROM import._311_service_requests_graffiti_removal
            ) tmp
;

INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,
                      x_coordinate, y_coordinate, request_type, status_type, district_id)
                      select tmp.incident_id::uuid,
                             tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,
                             tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,
                             tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,
                             tmp.status::VARCHAR, tmp.d_id::uuid from
                                                      (SELECT v.incident_id,
                                                              case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,
                                                              case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,
                                                              street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,
                                                              case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,
                                                              case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,
                                                              case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,
                                                              type_of_service_request, status, d.id d_id
                                                      FROM import._311_service_requests_graffiti_removal v
                                                      inner join district d on
                                                            (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area
                                                            and
                                                            (case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  =d.zip_code
                                                            and
                                                            (case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district
                                                            and
                                                            (case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER =d.ward) as tmp;

INSERT INTO graffiti_removal (id, ssa, graffiti_location, surface_type)
                      select tmp.incident_id::uuid,
                             tmp.ssa::INTEGER , tmp.where_is_the_graffiti_located::VARCHAR, tmp.what_type_of_surface_is_the_graffiti_on::VARCHAR
                             from (SELECT incident_id,
                                          case when coalesce(ssa, 'NULL') = '' then NULL else coalesce(ssa, 'NULL')  end ssa,
                                          where_is_the_graffiti_located,
                                          case when coalesce(what_type_of_surface_is_the_graffiti_on, 'NULL') = '' then NULL else coalesce(what_type_of_surface_is_the_graffiti_on, 'NULL')  end what_type_of_surface_is_the_graffiti_on
                                  FROM import._311_service_requests_graffiti_removal) as tmp;

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--5) Insert Pot Holes

INSERT INTO request_type (request_type_id, name)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.type_of_service_request::VARCHAR from
            (SELECT distinct type_of_service_request
            FROM import._311_service_requests_pot_holes_reported
            ) tmp;

INSERT INTO district (id, zip_code, ward, police_district, community_area)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from
            (SELECT distinct
                    case when coalesce(zip, '0') = '' then '0' else coalesce(zip, '0')  end zip_code,
                    case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area,
                    case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district,
                    case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward
            FROM import._311_service_requests_pot_holes_reported
            ) tmp
        where not exists(select * from district d where d.zip_code=tmp.zip_code::INTEGER and d.ward=tmp.ward::INTEGER
                        and d.police_district = tmp.police_district::INTEGER and d.community_area=tmp.community_area::INTEGER);

ALTER TABLE import._311_service_requests_pot_holes_reported
ADD incident_id uuid;

update import._311_service_requests_pot_holes_reported set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring);

INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)
select tmp.incident_id::uuid, tmp.historical_wards_2003_2015::INTEGER, tmp.community_areas::INTEGER,
       tmp.census_tracts::INTEGER, tmp.zip_codes::INTEGER, tmp.wards::INTEGER from
            (SELECT incident_id,
                    case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0')  end wards,
                    case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0')  end zip_codes,
                    case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0')  end community_areas,
                    case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0')  end historical_wards_2003_2015,
                    case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0')  end census_tracts
            FROM import._311_service_requests_pot_holes_reported
            ) tmp
;

INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,
                      x_coordinate, y_coordinate, request_type, status_type, district_id)
                      select tmp.incident_id::uuid,
                             tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,
                             tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,
                             tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,
                             tmp.status::VARCHAR, tmp.d_id::uuid from
                                                      (SELECT v.incident_id,
                                                              case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,
                                                              case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,
                                                              street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,
                                                              case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,
                                                              case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,
                                                              case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,
                                                              type_of_service_request, status, d.id d_id
                                                      FROM import._311_service_requests_pot_holes_reported v
                                                      inner join district d on
                                                            (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area
                                                            and
                                                            (case when coalesce(v.zip, '0') = '' then '0' else coalesce(v.zip, '0')  END)::INTEGER  =d.zip_code
                                                            and
                                                            (case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district
                                                            and
                                                            (case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER =d.ward) as tmp;

INSERT INTO pot_holes_reported (id, ssa, current_activity, filled_block_potholes_num, most_recent_action)
                      select tmp.incident_id::uuid,
                             tmp.ssa::INTEGER , tmp.current_activity::VARCHAR, tmp.number_of_potholes_filled_on_block::FLOAT, tmp.most_recent_action::VARCHAR
                             from (SELECT incident_id,
                                          case when coalesce(ssa, 'NULL') = '' then NULL else coalesce(ssa, 'NULL')  end ssa,
                                          current_activity,
                                          case when coalesce(number_of_potholes_filled_on_block, 'NULL') = '' then NULL else coalesce(number_of_potholes_filled_on_block, 'NULL')  end number_of_potholes_filled_on_block,
                                          most_recent_action
                                  FROM import._311_service_requests_pot_holes_reported) as tmp;

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--6) Insert Rodent Baiting

INSERT INTO request_type (request_type_id, name)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.type_of_service_request::VARCHAR from
            (SELECT distinct type_of_service_request
            FROM import._311_service_requests_rodent_baiting
            ) tmp;

INSERT INTO district (id, zip_code, ward, police_district, community_area)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from
            (SELECT distinct
                    case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code,
                    case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area,
                    case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district,
                    case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward
            FROM import._311_service_requests_rodent_baiting
            ) tmp
        where not exists(select * from district d where d.zip_code=tmp.zip_code::INTEGER and d.ward=tmp.ward::INTEGER
                        and d.police_district = tmp.police_district::INTEGER and d.community_area=tmp.community_area::INTEGER);

ALTER TABLE import._311_service_requests_rodent_baiting
ADD incident_id uuid;

update import._311_service_requests_rodent_baiting set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring);

INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)
select tmp.incident_id::uuid, tmp.historical_wards_2003_2015::INTEGER, tmp.community_areas::INTEGER,
       tmp.census_tracts::INTEGER, tmp.zip_codes::INTEGER, tmp.wards::INTEGER from
            (SELECT incident_id,
                    case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0')  end wards,
                    case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0')  end zip_codes,
                    case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0')  end community_areas,
                    case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0')  end historical_wards_2003_2015,
                    case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0')  end census_tracts
            FROM import._311_service_requests_rodent_baiting
            ) tmp
;

INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,
                      x_coordinate, y_coordinate, request_type, status_type, district_id)
                      select tmp.incident_id::uuid,
                             tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,
                             tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,
                             tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,
                             tmp.status::VARCHAR, tmp.d_id::uuid from
                                                      (SELECT v.incident_id,
                                                              case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,
                                                              case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,
                                                              street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,
                                                              case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,
                                                              case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,
                                                              case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,
                                                              type_of_service_request, status, d.id d_id
                                                      FROM import._311_service_requests_rodent_baiting v
                                                      inner join district d on
                                                            (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area
                                                            and
                                                            (case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  =d.zip_code
                                                            and
                                                            (case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district
                                                            and
                                                            (case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER =d.ward) as tmp;

INSERT INTO rodent_baiting (id, baited_premises_num, current_activity, most_recent_action, premises_with_garbage_num, premises_with_rats_num)
                      select tmp.incident_id::uuid,
                             tmp.number_of_premises_baited::FLOAT , tmp.current_activity::VARCHAR, tmp.most_recent_action::VARCHAR,
                             tmp.number_of_premises_with_garbage::FLOAT, tmp.number_of_premises_with_rats::FLOAT
                             from (SELECT incident_id,
                                          case when coalesce(number_of_premises_baited, '0') = '' then NULL else coalesce(number_of_premises_baited, '0')  end number_of_premises_baited,
                                          current_activity, most_recent_action,
                                          case when coalesce(number_of_premises_with_garbage, '0') = '' then NULL else coalesce(number_of_premises_with_garbage, '0')  end number_of_premises_with_garbage,
                                          case when coalesce(number_of_premises_with_rats, '0') = '' then NULL else coalesce(number_of_premises_with_rats, '0')  end number_of_premises_with_rats
                                  FROM import._311_service_requests_rodent_baiting) as tmp;

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--7) Insert Sanitation code complaints

INSERT INTO request_type (request_type_id, name)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.type_of_service_request::VARCHAR from
            (SELECT distinct type_of_service_request
            FROM import._311_service_requests_sanitation_code_complaints
            ) tmp;

INSERT INTO district (id, zip_code, ward, police_district, community_area)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from
            (SELECT distinct
                    case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code,
                    case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area,
                    case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district,
                    case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward
            FROM import._311_service_requests_sanitation_code_complaints
            ) tmp
        where not exists(select * from district d where d.zip_code=tmp.zip_code::INTEGER and d.ward=tmp.ward::INTEGER
                        and d.police_district = tmp.police_district::INTEGER and d.community_area=tmp.community_area::INTEGER);

ALTER TABLE import._311_service_requests_sanitation_code_complaints
ADD incident_id uuid;

update import._311_service_requests_sanitation_code_complaints set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring);

INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)
select tmp.incident_id::uuid, tmp.historical_wards_2003_2015::INTEGER, tmp.community_areas::INTEGER,
       tmp.census_tracts::INTEGER, tmp.zip_codes::INTEGER, tmp.wards::INTEGER from
            (SELECT incident_id,
                    case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0')  end wards,
                    case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0')  end zip_codes,
                    case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0')  end community_areas,
                    case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0')  end historical_wards_2003_2015,
                    case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0')  end census_tracts
            FROM import._311_service_requests_sanitation_code_complaints
            ) tmp
;

INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,
                      x_coordinate, y_coordinate, request_type, status_type, district_id)
                      select tmp.incident_id::uuid,
                             tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,
                             tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,
                             tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,
                             tmp.status::VARCHAR, tmp.d_id::uuid from
                                                      (SELECT v.incident_id,
                                                              case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,
                                                              case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,
                                                              street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,
                                                              case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,
                                                              case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,
                                                              case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,
                                                              type_of_service_request, status, d.id d_id
                                                      FROM import._311_service_requests_sanitation_code_complaints v
                                                      inner join district d on
                                                            (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area
                                                            and
                                                            (case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  =d.zip_code
                                                            and
                                                            (case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district
                                                            and
                                                            (case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER =d.ward) as tmp;

INSERT INTO sanitation_code_complaints (id, violation_nature)
                      select tmp.incident_id::uuid, tmp.what_is_the_nature_of_this_code_violation::VARCHAR
                             from (SELECT incident_id, what_is_the_nature_of_this_code_violation
                                  FROM import._311_service_requests_sanitation_code_complaints) as tmp;


-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--8) Insert Tree trims

INSERT INTO request_type (request_type_id, name)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.type_of_service_request::VARCHAR from
            (SELECT distinct type_of_service_request
            FROM import._311_service_requests_tree_trims
            ) tmp;

INSERT INTO district (id, zip_code, ward, police_district, community_area)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from
            (SELECT distinct
                    case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code,
                    case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area,
                    case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district,
                    case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward
            FROM import._311_service_requests_tree_trims
            ) tmp
        where not exists(select * from district d where d.zip_code=tmp.zip_code::INTEGER and d.ward=tmp.ward::INTEGER
                        and d.police_district = tmp.police_district::INTEGER and d.community_area=tmp.community_area::INTEGER);

ALTER TABLE import._311_service_requests_tree_trims
ADD incident_id uuid;

update import._311_service_requests_tree_trims set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring);

INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)
select tmp.incident_id::uuid, tmp.historical_wards_2003_2015::INTEGER, tmp.community_areas::INTEGER,
       tmp.census_tracts::INTEGER, tmp.zip_codes::INTEGER, tmp.wards::INTEGER from
            (SELECT incident_id,
                    case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0')  end wards,
                    case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0')  end zip_codes,
                    case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0')  end community_areas,
                    case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0')  end historical_wards_2003_2015,
                    case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0')  end census_tracts
            FROM import._311_service_requests_tree_trims
            ) tmp
;

INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,
                      x_coordinate, y_coordinate, request_type, status_type, district_id)
                      select tmp.incident_id::uuid,
                             tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,
                             tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,
                             tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,
                             tmp.status::VARCHAR, tmp.d_id::uuid from
                                                      (SELECT v.incident_id,
                                                              case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,
                                                              case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,
                                                              street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,
                                                              case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,
                                                              case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,
                                                              case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,
                                                              type_of_service_request, status, d.id d_id
                                                      FROM import._311_service_requests_tree_trims v
                                                      inner join district d on
                                                            (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area
                                                            and
                                                            (case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  =d.zip_code
                                                            and
                                                            (case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district
                                                            and
                                                            (case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER =d.ward) as tmp;

INSERT INTO tree_trims (id, tree_location)
                      select tmp.incident_id::uuid, tmp.location_of_trees::VARCHAR
                             from (SELECT incident_id, location_of_trees
                                  FROM import._311_service_requests_tree_trims) as tmp;

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--9) Insert StreetLightsAllOut

INSERT INTO request_type (request_type_id, name)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.type_of_service_request::VARCHAR from
            (SELECT distinct type_of_service_request
            FROM import._311_service_requests_street_lights_all_out
            ) tmp;

INSERT INTO district (id, zip_code, ward, police_district, community_area)
select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
        tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from
            (SELECT distinct
                    case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code,
                    case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area,
                    case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district,
                    case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward
            FROM import._311_service_requests_street_lights_all_out
            ) tmp
        where not exists(select * from district d where d.zip_code=tmp.zip_code::INTEGER and d.ward=tmp.ward::INTEGER
                        and d.police_district = tmp.police_district::INTEGER and d.community_area=tmp.community_area::INTEGER);

ALTER TABLE import._311_service_requests_street_lights_all_out
ADD incident_id uuid;

update import._311_service_requests_street_lights_all_out set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring);

INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)
select tmp.incident_id::uuid, tmp.historical_wards_2003_2015::INTEGER, tmp.community_areas::INTEGER,
       tmp.census_tracts::INTEGER, tmp.zip_codes::INTEGER, tmp.wards::INTEGER from
            (SELECT incident_id,
                    case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0')  end wards,
                    case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0')  end zip_codes,
                    case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0')  end community_areas,
                    case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0')  end historical_wards_2003_2015,
                    case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0')  end census_tracts
            FROM import._311_service_requests_street_lights_all_out
            ) tmp
;


INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude, x_coordinate, y_coordinate, request_type, status_type, district_id)
select tmp.incident_id,
     tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,
     tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,
     tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,
     tmp.status::VARCHAR, tmp.d_id::uuid from
      (SELECT v.incident_id,
          case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,
          case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,
          street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,
          case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,
          case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,
          case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,
          type_of_service_request, status, d.id d_id
      FROM import._311_service_requests_street_lights_all_out v
      inner join district d on
        (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area
        and
        (case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  =d.zip_code
        and
        (case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district
        and
        (case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER =d.ward) as tmp
;

INSERT INTO alley_lights_out (ID) SELECT incident_id FROM import._311_service_requests_street_lights_all_out;
