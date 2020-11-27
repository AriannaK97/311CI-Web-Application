import glob
import os
import subprocess
import sys
import uuid
import numpy as np
import psycopg2.extras as extras
from psycopg2.extensions import register_adapter
import psycopg2
import pandas as pd


class MigrateDb:
    def __init__(self):
        psycopg2.extensions.register_adapter(np.int64, psycopg2._psycopg.AsIs)
        self.path = '/mnt/5a5abb6b-4b3d-4ab8-bfea-9c7fc2ce23e6/linuxuser/Public/University/MSc/DataBases/Project1/archive/'
        self.conn = self._connect_()
        self.cur = self.conn.cursor()
        # self.admin_user_id = self._insert_admin_user_()
        self.number_of_missed_entries = 0
        psycopg2.extras.register_uuid()

    @staticmethod
    def _connect_():
        try:
            # connect to the PostgresQl server
            print("Connecting to db_311ci...")
            conn = psycopg2.connect("host=localhost dbname=db_311ci user=postgres password=root")
        except(Exception, psycopg2.DatabaseError) as error:
            print(error)
            sys.exit(1)
        return conn

    def _insert_admin_user_(self):
        psycopg2.extras.register_uuid()
        self.cur.execute("INSERT INTO registered_user (username, password, first_name, last_name, email) "
                         "VALUES (%s, %s, %s, %s, %s) RETURNING id", ('admin', 'pass', 'admin',
                                                                      'admin', 'admin@mail.com'))
        self.conn.commit()
        _ret = self.cur.fetchone()[0]
        return _ret

    def read(self):
        """
        Read a resource from the file specified
        using the appropriate reader for this format
        """
        all_files = glob.glob(os.path.join(self.path, "*.csv"))

        iter_ = 0
        print(all_files)
        for file in all_files:
            iter_ += 1
            command = "./pgfutter --db \"db_311ci\" --port \"5432\" --user \"postgres\" --pw \"root\" csv " + file
            os.system(command)
            table = self.get_table_name_reference(file)
            #self.call_table_insert(table)
            # if iter_ == 3:
            #     break
        print(len(all_files))
        self.cur.close()

    def migrate_abandoned_vehicles(self):

        request_type_query = "INSERT INTO request_type (request_type_id, name) " \
                       "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), " \
                       "tmp.type_of_service_request::VARCHAR from " \
                           "(SELECT distinct type_of_service_request " \
                           "FROM import._311_service_requests_abandoned_vehicles" \
                           ") tmp"
        self.cur.execute(request_type_query)
        self.conn.commit()

        # insert district entries
        district_query = "INSERT INTO district (id, zip_code, ward, police_district, community_area)" \
                         "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)," \
                         "tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from" \
                         "(SELECT distinct " \
                         "case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code," \
                         "case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area," \
                         "case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district," \
                         "case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward " \
                         "FROM import._311_service_requests_abandoned_vehicles) tmp"
        self.cur.execute(district_query)
        self.conn.commit()

        # add uuids
        insert_incident_uuid_column = "ALTER TABLE import._311_service_requests_abandoned_vehicles ADD incident_id uuid;"
        self.cur.execute(insert_incident_uuid_column)
        self.conn.commit()
        create_uuids_query = "update import._311_service_requests_abandoned_vehicles set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)"
        self.cur.execute(create_uuids_query)
        self.conn.commit()

        # insert extra incident info
        extra_incident_info_query = "INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)" \
                                    "select tmp.incident_id::uuid, tmp.zip_codes::INTEGER, tmp.historical_wards_2003_2015::INTEGER," \
                                    "tmp.community_areas::INTEGER, tmp.census_tracts::INTEGER, tmp.wards::INTEGER from "\
                                    "(SELECT incident_id, case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0') end wards,"\
                                    "case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0') end zip_codes," \
                                    "case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0') end community_areas," \
                                    "case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0') end historical_wards_2003_2015," \
                                    "case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0') end census_tracts " \
                                    "FROM import._311_service_requests_abandoned_vehicles" \
                                    ") tmp"
        self.cur.execute(extra_incident_info_query)
        self.conn.commit()

        # insert incident
        incident_query = "INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,"\
                          "x_coordinate, y_coordinate, request_type, status_type, district_id)"\
                          "select tmp.incident_id::uuid,"\
                                 "tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,"\
                                 "tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,"\
                                 "tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,"\
                                 "tmp.status::VARCHAR, tmp.d_id::uuid from"\
                                                          "(SELECT v.incident_id, " \
                                                                  "case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,"\
                                                                  "case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,"\
                                                                  "street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,"\
                                                                  "case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,"\
                                                                  "case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,"\
                                                                  "case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,"\
                                                                  "type_of_service_request, status, d.id d_id " \
                         "FROM import._311_service_requests_abandoned_vehicles v " \
                         "inner join district d on (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area " \
                         "and"\
                         "(case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  = d.zip_code "\
                         "and"\
                         "(case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district "\
                         "and"\
                         "(case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER = d.ward) as tmp;"
        self.cur.execute(incident_query)
        self.conn.commit()

        # insert to abandoned vehicle
        abandoned_vehicle_query = "INSERT INTO abandoned_vehicle (id, ssa, license_plate, vehicle_make_model, vehicle_color, " \
                                  "current_activity, most_recent_action, days_reported_parked)"\
                                  "select tmp.incident_id::uuid,"\
                                         "tmp.ssa::INTEGER , tmp.license_plate::VARCHAR, tmp.vehicle_make_model::VARCHAR,"\
                                         "tmp.vehicle_color::VARCHAR, tmp.current_activity::VARCHAR, tmp.most_recent_action::VARCHAR,"\
                                         "tmp.days_reported_parked::FLOAT from"\
                                                                  "(SELECT incident_id,"\
                                                                          "case when coalesce(ssa, '0') = '' then NULL else coalesce(ssa, '0')  end ssa,"\
                                                                          "license_plate, vehicle_make_model, vehicle_color, current_activity, most_recent_action,"\
                                                                          "case when coalesce(how_many_days_has_the_vehicle_been_reported_as_parked, '0') = '' then NULL else coalesce(how_many_days_has_the_vehicle_been_reported_as_parked, '0')  end days_reported_parked "\
                                                                  "FROM import._311_service_requests_abandoned_vehicles) as tmp;"
        self.cur.execute(abandoned_vehicle_query)
        self.conn.commit()

        #drop import table
        # drop = "drop table if exists import._311_service_requests_abandoned_vehicles cascade;"
        # self.cur.execute(drop)
        # self.conn.commit()

    def migrate_alley_lights_out(self):
        request_type_query = "INSERT INTO request_type (request_type_id, name) " \
                       "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), " \
                       "tmp.type_of_service_request::VARCHAR from " \
                           "(SELECT distinct type_of_service_request " \
                           "FROM import._311_service_requests_alley_lights_out" \
                           ") tmp"
        self.cur.execute(request_type_query)
        self.conn.commit()

        # insert district entries
        district_query = "INSERT INTO district (id, zip_code, ward, police_district, community_area)" \
                         "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)," \
                         "tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from" \
                         "(SELECT distinct " \
                         "case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code," \
                         "case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area," \
                         "case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district," \
                         "case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward " \
                         "FROM import._311_service_requests_alley_lights_out) tmp"
        self.cur.execute(district_query)
        self.conn.commit()

        # add uuids
        insert_incident_uuid_column = "ALTER TABLE import._311_service_requests_alley_lights_out ADD incident_id uuid;"
        self.cur.execute(insert_incident_uuid_column)
        self.conn.commit()
        create_uuids_query = "update import._311_service_requests_alley_lights_out set incident_id=uuid_in(md5(" \
                             "random()::text || clock_timestamp()::text)::cstring) "
        self.cur.execute(create_uuids_query)
        self.conn.commit()

        # insert extra incident info
        extra_incident_info_query = "INSERT INTO extra_incident_info (id, historical_wards_2003_2015, " \
                                    "community_areas, cencus_tracts, zip_codes, wards)" \
                                    "select tmp.incident_id::uuid, tmp.zip_codes::INTEGER, tmp.historical_wards_2003_2015::INTEGER," \
                                    "tmp.community_areas::INTEGER, tmp.census_tracts::INTEGER, tmp.wards::INTEGER from "\
                                    "(SELECT incident_id, case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0') end wards,"\
                                    "case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0') end zip_codes," \
                                    "case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0') end community_areas," \
                                    "case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0') end historical_wards_2003_2015," \
                                    "case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0') end census_tracts " \
                                    "FROM import._311_service_requests_alley_lights_out" \
                                    ") tmp"
        self.cur.execute(extra_incident_info_query)
        self.conn.commit()

        # insert incident
        incident_query = "INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,"\
                          "x_coordinate, y_coordinate, request_type, status_type, district_id)"\
                          "select tmp.incident_id::uuid,"\
                                 "tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,"\
                                 "tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,"\
                                 "tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,"\
                                 "tmp.status::VARCHAR, tmp.d_id::uuid from"\
                                                          "(SELECT v.incident_id, " \
                                                                  "case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,"\
                                                                  "case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,"\
                                                                  "street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,"\
                                                                  "case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,"\
                                                                  "case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,"\
                                                                  "case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,"\
                                                                  "type_of_service_request, status, d.id d_id " \
                         "FROM import._311_service_requests_alley_lights_out v " \
                         "inner join district d on (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area " \
                         "and"\
                         "(case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  = d.zip_code "\
                         "and"\
                         "(case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district "\
                         "and"\
                         "(case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER = d.ward) as tmp;"
        self.cur.execute(incident_query)
        self.conn.commit()

        #insert in alley_lights_out
        alley_lights_out_query="INSERT INTO alley_lights_out (ID) " \
                               "SELECT incident_id " \
                               "FROM import._311_service_requests_alley_lights_out"
        self.cur.execute(alley_lights_out_query)
        self.conn.commit()

        #drop import table
        drop = "drop table if exists import._311_service_requests_alley_lights_out cascade;"
        self.cur.execute(drop)
        self.conn.commit()

    def migrate_garbage_carts(self):
        request_type_query = "INSERT INTO request_type (request_type_id, name) " \
                       "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), " \
                       "tmp.type_of_service_request::VARCHAR from " \
                           "(SELECT distinct type_of_service_request " \
                           "FROM import._311_service_requests_garbage_carts" \
                           ") tmp"
        self.cur.execute(request_type_query)
        self.conn.commit()

        # insert district entries
        district_query = "INSERT INTO district (id, zip_code, ward, police_district, community_area)" \
                         "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)," \
                         "tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from" \
                         "(SELECT distinct " \
                         "case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code," \
                         "case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area," \
                         "case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district," \
                         "case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward " \
                         "FROM import._311_service_requests_garbage_carts) tmp"
        self.cur.execute(district_query)
        self.conn.commit()

        # add uuids
        insert_incident_uuid_column = "ALTER TABLE import._311_service_requests_garbage_carts ADD incident_id uuid;"
        self.cur.execute(insert_incident_uuid_column)
        self.conn.commit()
        create_uuids_query = "update import._311_service_requests_garbage_carts set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)"
        self.cur.execute(create_uuids_query)
        self.conn.commit()

        # insert extra incident info
        extra_incident_info_query = "INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)" \
                                    "select tmp.incident_id::uuid, tmp.zip_codes::INTEGER, tmp.historical_wards_2003_2015::INTEGER," \
                                    "tmp.community_areas::INTEGER, tmp.census_tracts::INTEGER, tmp.wards::INTEGER from "\
                                    "(SELECT incident_id, case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0') end wards,"\
                                    "case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0') end zip_codes," \
                                    "case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0') end community_areas," \
                                    "case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0') end historical_wards_2003_2015," \
                                    "case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0') end census_tracts " \
                                    "FROM import._311_service_requests_garbage_carts" \
                                    ") tmp"
        self.cur.execute(extra_incident_info_query)
        self.conn.commit()

        # insert incident
        incident_query = "INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,"\
                          "x_coordinate, y_coordinate, request_type, status_type, district_id)"\
                          "select tmp.incident_id::uuid,"\
                                 "tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,"\
                                 "tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,"\
                                 "tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,"\
                                 "tmp.status::VARCHAR, tmp.d_id::uuid from"\
                                                          "(SELECT v.incident_id, " \
                                                                  "case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,"\
                                                                  "case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,"\
                                                                  "street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,"\
                                                                  "case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,"\
                                                                  "case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,"\
                                                                  "case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,"\
                                                                  "type_of_service_request, status, d.id d_id " \
                         "FROM import._311_service_requests_garbage_carts v " \
                         "inner join district d on (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area " \
                         "and"\
                         "(case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  = d.zip_code "\
                         "and"\
                         "(case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district "\
                         "and"\
                         "(case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER = d.ward) as tmp;"
        self.cur.execute(incident_query)
        self.conn.commit()

        #insert garbage carts table
        garbage_carts_query="INSERT INTO garbage_carts (id, ssa, current_activity, delivered_black_carts_num, most_recent_action) "\
                              "select tmp.incident_id::uuid,"\
                                     "tmp.ssa::INTEGER , tmp.current_activity::VARCHAR, tmp.number_of_black_carts_delivered::INTEGER, tmp.most_recent_action::VARCHAR "\
                                     "from (SELECT incident_id,"\
                                                  "case when coalesce(ssa, '0') = '' then NULL else coalesce(ssa, '0')  end ssa,"\
                                                  "current_activity,"\
                                                  "case when coalesce(number_of_black_carts_delivered, '0') = '' then NULL else coalesce(number_of_black_carts_delivered, '0')  end number_of_black_carts_delivered, " \
                            "most_recent_action "\
                                          "FROM import._311_service_requests_garbage_carts) as tmp;"
        self.cur.execute(garbage_carts_query)
        self.conn.commit()

        #drop import table
        # drop = "drop table if exists import._311_service_requests_garbage_carts cascade;"
        # self.cur.execute(drop)
        # self.conn.commit()

    def migrate_graffiti_removal(self):
        request_type_query = "INSERT INTO request_type (request_type_id, name) " \
                       "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), " \
                       "tmp.type_of_service_request::VARCHAR from " \
                           "(SELECT distinct type_of_service_request " \
                           "FROM import._311_service_requests_graffiti_removal" \
                           ") tmp"
        self.cur.execute(request_type_query)
        self.conn.commit()

        # insert district entries
        district_query = "INSERT INTO district (id, zip_code, ward, police_district, community_area)" \
                         "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)," \
                         "tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from" \
                         "(SELECT distinct " \
                         "case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code," \
                         "case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area," \
                         "case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district," \
                         "case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward " \
                         "FROM import._311_service_requests_graffiti_removal) tmp"
        self.cur.execute(district_query)
        self.conn.commit()

        # add uuids
        insert_incident_uuid_column = "ALTER TABLE import._311_service_requests_graffiti_removal ADD incident_id uuid;"
        self.cur.execute(insert_incident_uuid_column)
        self.conn.commit()
        create_uuids_query = "update import._311_service_requests_graffiti_removal set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)"
        self.cur.execute(create_uuids_query)
        self.conn.commit()

        # insert extra incident info
        extra_incident_info_query = "INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)" \
                                    "select tmp.incident_id::uuid, tmp.zip_codes::INTEGER, tmp.historical_wards_2003_2015::INTEGER," \
                                    "tmp.community_areas::INTEGER, tmp.census_tracts::INTEGER, tmp.wards::INTEGER from "\
                                    "(SELECT incident_id, case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0') end wards,"\
                                    "case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0') end zip_codes," \
                                    "case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0') end community_areas," \
                                    "case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0') end historical_wards_2003_2015," \
                                    "case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0') end census_tracts " \
                                    "FROM import._311_service_requests_graffiti_removal" \
                                    ") tmp"
        self.cur.execute(extra_incident_info_query)
        self.conn.commit()

        # insert incident
        incident_query = "INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,"\
                          "x_coordinate, y_coordinate, request_type, status_type, district_id)"\
                          "select tmp.incident_id::uuid,"\
                                 "tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,"\
                                 "tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,"\
                                 "tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,"\
                                 "tmp.status::VARCHAR, tmp.d_id::uuid from"\
                                                          "(SELECT v.incident_id, " \
                                                                  "case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,"\
                                                                  "case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,"\
                                                                  "street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,"\
                                                                  "case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,"\
                                                                  "case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,"\
                                                                  "case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,"\
                                                                  "type_of_service_request, status, d.id d_id " \
                         "FROM import._311_service_requests_graffiti_removal v " \
                         "inner join district d on (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area " \
                         "and"\
                         "(case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  = d.zip_code "\
                         "and"\
                         "(case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district "\
                         "and"\
                         "(case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER = d.ward) as tmp;"
        self.cur.execute(incident_query)
        self.conn.commit()

        #insert graffiti removal
        graffiti_removal_query = "INSERT INTO graffiti_removal (id, ssa, graffiti_location, surface_type) "\
                                  "select tmp.incident_id::uuid,"\
                                         "tmp.ssa::INTEGER , tmp.where_is_the_graffiti_located::VARCHAR, tmp.what_type_of_surface_is_the_graffiti_on::INTEGER " \
                                 "from (SELECT incident_id,"\
                                                      "case when coalesce(ssa, '0') = '' then NULL else coalesce(ssa, '0')  end ssa,"\
                                                      "where_is_the_graffiti_located,"\
                                                      "case when coalesce(what_type_of_surface_is_the_graffiti_on, '0') = '' then NULL else coalesce(what_type_of_surface_is_the_graffiti_on, '0')  end what_type_of_surface_is_the_graffiti_on "\
                                              "FROM import._311_service_requests_graffiti_removal) as tmp;"
        self.cur.execute(graffiti_removal_query)
        self.conn.commit()

        #drop import table
        # drop = "drop table if exists import._311_service_requests_graffiti_removal cascade;"
        # self.cur.execute(drop)
        # self.conn.commit()

    def migrate_request_pot_holes(self):
        request_type_query = "INSERT INTO request_type (request_type_id, name) " \
                       "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), " \
                       "tmp.type_of_service_request::VARCHAR from " \
                           "(SELECT distinct type_of_service_request " \
                           "FROM import._311_service_requests_pot_holes_reported" \
                           ") tmp"
        self.cur.execute(request_type_query)
        self.conn.commit()

        # insert district entries
        district_query = "INSERT INTO district (id, zip_code, ward, police_district, community_area)" \
                         "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)," \
                         "tmp.zip::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from" \
                         "(SELECT distinct " \
                         "case when coalesce(zip, '0') = '' then '0' else coalesce(zip, '0')  end zip," \
                         "case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area," \
                         "case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district," \
                         "case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward " \
                         "FROM import._311_service_requests_pot_holes_reported) tmp"
        self.cur.execute(district_query)
        self.conn.commit()

        # add uuids
        insert_incident_uuid_column = "ALTER TABLE import._311_service_requests_pot_holes_reported ADD incident_id uuid;"
        self.cur.execute(insert_incident_uuid_column)
        self.conn.commit()
        create_uuids_query = "update import._311_service_requests_pot_holes_reported set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)"
        self.cur.execute(create_uuids_query)
        self.conn.commit()

        # insert extra incident info
        extra_incident_info_query = "INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)" \
                                    "select tmp.incident_id::uuid, tmp.zip_codes::INTEGER, tmp.historical_wards_2003_2015::INTEGER," \
                                    "tmp.community_areas::INTEGER, tmp.census_tracts::INTEGER, tmp.wards::INTEGER from "\
                                    "(SELECT incident_id, case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0') end wards,"\
                                    "case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0') end zip_codes," \
                                    "case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0') end community_areas," \
                                    "case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0') end historical_wards_2003_2015," \
                                    "case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0') end census_tracts " \
                                    "FROM import._311_service_requests_pot_holes_reported" \
                                    ") tmp"
        self.cur.execute(extra_incident_info_query)
        self.conn.commit()

        # insert incident
        incident_query = "INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,"\
                          "x_coordinate, y_coordinate, request_type, status_type, district_id)"\
                          "select tmp.incident_id::uuid,"\
                                 "tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,"\
                                 "tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,"\
                                 "tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,"\
                                 "tmp.status::VARCHAR, tmp.d_id::uuid from"\
                                                          "(SELECT v.incident_id, " \
                                                                  "case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,"\
                                                                  "case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,"\
                                                                  "street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,"\
                                                                  "case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,"\
                                                                  "case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,"\
                                                                  "case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,"\
                                                                  "type_of_service_request, status, d.id d_id " \
                         "FROM import._311_service_requests_pot_holes_reported v " \
                         "inner join district d on (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area " \
                         "and"\
                         "(case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  = d.zip_code "\
                         "and"\
                         "(case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district "\
                         "and"\
                         "(case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER = d.ward) as tmp;"
        self.cur.execute(incident_query)
        self.conn.commit()

        #insert pot holes requests
        pot_holes_query = "INSERT INTO pot_holes_reported (id, ssa, current_activity, filled_block_potholes_num, most_recent_action) "\
                          "select tmp.incident_id::uuid,"\
                                 "tmp.ssa::INTEGER , tmp.current_activity::VARCHAR, tmp.number_of_potholes_filled_on_block::INTEGER, tmp.most_recent_action::VARCHAR "\
                                 "from (SELECT incident_id,"\
                                              "case when coalesce(ssa, '0') = '' then NULL else coalesce(ssa, '0')  end ssa,"\
                                              "current_activity,"\
                                              "case when coalesce(number_of_potholes_filled_on_block, '0') = '' then NULL else coalesce(number_of_potholes_filled_on_block, '0')  end number_of_potholes_filled_on_block, " \
                          "most_recent_action " \
                          "FROM import._311_service_requests_pot_holes_reported) as tmp;"
        self.cur.execute(pot_holes_query)
        self.conn.commit()

        #drop import table
        # drop = "drop table if exists import._311_service_requests_pot_holes_reported cascade;"
        # self.cur.execute(drop)
        # self.conn.commit()

    def migrate_rodent_bating(self):
        request_type_query = "INSERT INTO request_type (request_type_id, name) " \
                       "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), " \
                       "tmp.type_of_service_request::VARCHAR from " \
                           "(SELECT distinct type_of_service_request " \
                           "FROM import._311_service_requests_rodent_baiting" \
                           ") tmp"
        self.cur.execute(request_type_query)
        self.conn.commit()

        # insert district entries
        district_query = "INSERT INTO district (id, zip_code, ward, police_district, community_area)" \
                         "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)," \
                         "tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from" \
                         "(SELECT distinct " \
                         "case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code," \
                         "case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area," \
                         "case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district," \
                         "case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward " \
                         "FROM import._311_service_requests_rodent_baiting) tmp"
        self.cur.execute(district_query)
        self.conn.commit()

        # add uuids
        insert_incident_uuid_column = "ALTER TABLE import._311_service_requests_rodent_baiting ADD incident_id uuid;"
        self.cur.execute(insert_incident_uuid_column)
        self.conn.commit()
        create_uuids_query = "update import._311_service_requests_rodent_baiting set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)"
        self.cur.execute(create_uuids_query)
        self.conn.commit()

        # insert extra incident info
        extra_incident_info_query = "INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)" \
                                    "select tmp.incident_id::uuid, tmp.zip_codes::INTEGER, tmp.historical_wards_2003_2015::INTEGER," \
                                    "tmp.community_areas::INTEGER, tmp.census_tracts::INTEGER, tmp.wards::INTEGER from "\
                                    "(SELECT incident_id, case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0') end wards,"\
                                    "case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0') end zip_codes," \
                                    "case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0') end community_areas," \
                                    "case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0') end historical_wards_2003_2015," \
                                    "case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0') end census_tracts " \
                                    "FROM import._311_service_requests_rodent_baiting" \
                                    ") tmp"
        self.cur.execute(extra_incident_info_query)
        self.conn.commit()

        # insert incident
        incident_query = "INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,"\
                          "x_coordinate, y_coordinate, request_type, status_type, district_id)"\
                          "select tmp.incident_id::uuid,"\
                                 "tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,"\
                                 "tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,"\
                                 "tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,"\
                                 "tmp.status::VARCHAR, tmp.d_id::uuid from"\
                                                          "(SELECT v.incident_id, " \
                                                                  "case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,"\
                                                                  "case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,"\
                                                                  "street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,"\
                                                                  "case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,"\
                                                                  "case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,"\
                                                                  "case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,"\
                                                                  "type_of_service_request, status, d.id d_id " \
                         "FROM import._311_service_requests_rodent_baiting v " \
                         "inner join district d on (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area " \
                         "and"\
                         "(case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  = d.zip_code "\
                         "and"\
                         "(case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district "\
                         "and"\
                         "(case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER = d.ward) as tmp;"
        self.cur.execute(incident_query)
        self.conn.commit()

        #insert rodent baiting
        rodent_baiting_query = "INSERT INTO rodent_baiting (id, baited_premises_num, current_activity, most_recent_action, premises_with_garbage_num, premises_with_rats_num) " \
                               "select tmp.incident_id::uuid," \
                               "tmp.number_of_premises_baited::INTEGER , tmp.current_activity::VARCHAR, tmp.most_recent_action::VARCHAR," \
                               "tmp.number_of_premises_with_garbage::INTEGER, tmp.number_of_premises_with_rats::INTEGER " \
                               "from (SELECT incident_id," \
                               "case when coalesce(number_of_premises_baited, '0') = '' then NULL else coalesce(number_of_premises_baited, '0')  end number_of_premises_baited," \
                               "current_activity, most_recent_action," \
                               "case when coalesce(number_of_premises_with_garbage, '0') = '' then NULL else coalesce(number_of_premises_with_garbage, '0')  end number_of_premises_with_garbage," \
                               "case when coalesce(number_of_premises_with_rats, '0') = '' then NULL else coalesce(number_of_premises_with_rats, '0')  end number_of_premises_with_rats " \
                               "FROM import._311_service_requests_rodent_baiting) as tmp;"
        self.cur.execute(rodent_baiting_query)
        self.conn.commit()

        #drop import table
        # drop = "drop table if exists import._311_service_requests_rodent_baiting cascade;"
        # self.cur.execute(drop)
        # self.conn.commit()

    def migrate_sanitation_code_complaint(self):
        request_type_query = "INSERT INTO request_type (request_type_id, name) " \
                       "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), " \
                       "tmp.type_of_service_request::VARCHAR from " \
                           "(SELECT distinct type_of_service_request " \
                           "FROM import._311_service_requests_sanitation_code_complaints" \
                           ") tmp"
        self.cur.execute(request_type_query)
        self.conn.commit()

        # insert district entries
        district_query = "INSERT INTO district (id, zip_code, ward, police_district, community_area)" \
                         "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)," \
                         "tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from" \
                         "(SELECT distinct " \
                         "case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code," \
                         "case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area," \
                         "case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district," \
                         "case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward " \
                         "FROM import._311_service_requests_sanitation_code_complaints) tmp"
        self.cur.execute(district_query)
        self.conn.commit()

        # add uuids
        insert_incident_uuid_column = "ALTER TABLE import._311_service_requests_sanitation_code_complaints ADD incident_id uuid;"
        self.cur.execute(insert_incident_uuid_column)
        self.conn.commit()
        create_uuids_query = "update import._311_service_requests_sanitation_code_complaints set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)"
        self.cur.execute(create_uuids_query)
        self.conn.commit()

        # insert extra incident info
        extra_incident_info_query = "INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)" \
                                    "select tmp.incident_id::uuid, tmp.zip_codes::INTEGER, tmp.historical_wards_2003_2015::INTEGER," \
                                    "tmp.community_areas::INTEGER, tmp.census_tracts::INTEGER, tmp.wards::INTEGER from "\
                                    "(SELECT incident_id, case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0') end wards,"\
                                    "case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0') end zip_codes," \
                                    "case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0') end community_areas," \
                                    "case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0') end historical_wards_2003_2015," \
                                    "case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0') end census_tracts " \
                                    "FROM import._311_service_requests_sanitation_code_complaints" \
                                    ") tmp"
        self.cur.execute(extra_incident_info_query)
        self.conn.commit()

        # insert incident
        incident_query = "INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,"\
                          "x_coordinate, y_coordinate, request_type, status_type, district_id)"\
                          "select tmp.incident_id::uuid,"\
                                 "tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,"\
                                 "tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,"\
                                 "tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,"\
                                 "tmp.status::VARCHAR, tmp.d_id::uuid from"\
                                                          "(SELECT v.incident_id, " \
                                                                  "case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,"\
                                                                  "case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,"\
                                                                  "street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,"\
                                                                  "case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,"\
                                                                  "case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,"\
                                                                  "case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,"\
                                                                  "type_of_service_request, status, d.id d_id " \
                         "FROM import._311_service_requests_sanitation_code_complaints v " \
                         "inner join district d on (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area " \
                         "and"\
                         "(case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  = d.zip_code "\
                         "and"\
                         "(case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district "\
                         "and"\
                         "(case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER = d.ward) as tmp;"
        self.cur.execute(incident_query)
        self.conn.commit()

        #insert sanitation code complaints
        sanitation_code_complaints = "INSERT INTO sanitation_code_complaints (id, violation_nature) "\
                                     "select tmp.incident_id::uuid, tmp.current_activity::VARCHAR "\
                                     "from (SELECT incident_id, what_is_the_nature_of_this_code_violation "\
                                          "FROM import._311_service_requests_sanitation_code_complaints) as tmp;"
        self.cur.execute(sanitation_code_complaints)
        self.conn.commit()

        #drop import table
        # drop = "drop table if exists import._311_service_requests_sanitation_code_complaints cascade;"
        # self.cur.execute(drop)
        # self.conn.commit()

    def migrate_tree_trims(self):
        request_type_query = "INSERT INTO request_type (request_type_id, name) " \
                       "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), " \
                       "tmp.type_of_service_request::VARCHAR from " \
                           "(SELECT distinct type_of_service_request " \
                           "FROM import._311_service_requests_tree_trims" \
                           ") tmp"
        self.cur.execute(request_type_query)
        self.conn.commit()

        # insert district entries
        district_query = "INSERT INTO district (id, zip_code, ward, police_district, community_area)" \
                         "select uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)," \
                         "tmp.zip_code::INTEGER, tmp.ward::INTEGER, tmp.police_district::INTEGER, tmp.community_area::INTEGER from" \
                         "(SELECT distinct " \
                         "case when coalesce(zip_code, '0') = '' then '0' else coalesce(zip_code, '0')  end zip_code," \
                         "case when coalesce(community_area, '0') = '' then '0' else coalesce(community_area, '0')  end community_area," \
                         "case when coalesce(police_district, '0') = '' then '0' else coalesce(police_district, '0')  end police_district," \
                         "case when coalesce(ward, '0') = '' then '0' else coalesce(ward, '0')  end ward " \
                         "FROM import._311_service_requests_tree_trims) tmp"
        self.cur.execute(district_query)
        self.conn.commit()

        # add uuids
        insert_incident_uuid_column = "ALTER TABLE import._311_service_requests_tree_trims ADD incident_id uuid;"
        self.cur.execute(insert_incident_uuid_column)
        self.conn.commit()
        create_uuids_query = "update import._311_service_requests_tree_trims set incident_id=uuid_in(md5(random()::text || clock_timestamp()::text)::cstring)"
        self.cur.execute(create_uuids_query)
        self.conn.commit()

        # insert extra incident info
        extra_incident_info_query = "INSERT INTO extra_incident_info (id, historical_wards_2003_2015, community_areas, cencus_tracts, zip_codes, wards)" \
                                    "select tmp.incident_id::uuid, tmp.zip_codes::INTEGER, tmp.historical_wards_2003_2015::INTEGER," \
                                    "tmp.community_areas::INTEGER, tmp.census_tracts::INTEGER, tmp.wards::INTEGER from "\
                                    "(SELECT incident_id, case when coalesce(wards, '0') = '' then '0' when wards = E'\r' then '0' else coalesce(wards, '0') end wards,"\
                                    "case when coalesce(zip_codes, '0') = '' then '0' when zip_codes = E'\r' then '0' else coalesce(zip_codes, '0') end zip_codes," \
                                    "case when coalesce(community_areas, '0') = '' then '0' when community_areas = E'\r' then '0' else coalesce(community_areas, '0') end community_areas," \
                                    "case when coalesce(historical_wards_2003_2015, '0') = '' then '0' when historical_wards_2003_2015 = E'\r' then '0' else coalesce(historical_wards_2003_2015, '0') end historical_wards_2003_2015," \
                                    "case when coalesce(census_tracts, '0') = '' then '0' when census_tracts = E'\r' then '0' else coalesce(census_tracts, '0') end census_tracts " \
                                    "FROM import._311_service_requests_tree_trims" \
                                    ") tmp"
        self.cur.execute(extra_incident_info_query)
        self.conn.commit()

        # insert incident
        incident_query = "INSERT INTO incident (id, creation_date, completion_date, service_request_number, street_address, longitude, latitude,"\
                          "x_coordinate, y_coordinate, request_type, status_type, district_id)"\
                          "select tmp.incident_id::uuid,"\
                                 "tmp.creation_date::DATE , tmp.completion_date::DATE, tmp.service_request_number::VARCHAR,"\
                                 "tmp.street_address::VARCHAR, tmp.longitude::FLOAT, tmp.latitude::FLOAT,"\
                                 "tmp.x_coordinate::FLOAT, tmp.y_coordinate::FLOAT, tmp.type_of_service_request::VARCHAR,"\
                                 "tmp.status::VARCHAR, tmp.d_id::uuid from"\
                                                          "(SELECT v.incident_id, " \
                                                                  "case when coalesce(creation_date, 'NULL') = '' then NULL else coalesce(creation_date, 'NULL')  end creation_date,"\
                                                                  "case when coalesce(completion_date, 'NULL') = '' then NULL else coalesce(completion_date, 'NULL')  end completion_date, service_request_number,"\
                                                                  "street_address, case when coalesce(longitude, '0') = '' then '0' else coalesce(longitude, '0')  end longitude,"\
                                                                  "case when coalesce(latitude, '0') = '' then '0' else coalesce(latitude, '0')  end latitude,"\
                                                                  "case when coalesce(x_coordinate, '0') = '' then '0' else coalesce(x_coordinate, '0') end x_coordinate,"\
                                                                  "case when coalesce(y_coordinate, '0') = '' then '0' else coalesce(y_coordinate, '0') end y_coordinate,"\
                                                                  "type_of_service_request, status, d.id d_id " \
                         "FROM import._311_service_requests_tree_trims v " \
                         "inner join district d on (case when coalesce(v.community_area, '0') = '' then '0' else coalesce(v.community_area, '0')  end)::INTEGER = d.community_area " \
                         "and"\
                         "(case when coalesce(v.zip_code, '0') = '' then '0' else coalesce(v.zip_code, '0')  END)::INTEGER  = d.zip_code "\
                         "and"\
                         "(case when coalesce(v.police_district, '0') = '' then '0' else coalesce(v.police_district, '0')  end)::INTEGER = d.police_district "\
                         "and"\
                         "(case when coalesce(v.ward, '0') = '' then '0' else coalesce(v.ward, '0')  end)::INTEGER = d.ward) as tmp;"
        self.cur.execute(incident_query)
        self.conn.commit()

        #insert tree trims
        tree_trims_query = "INSERT INTO tree_trims (id, tree_location) "\
                          "select tmp.incident_id::uuid, tmp.location_of_trees::VARCHAR "\
                                 "from (SELECT incident_id, location_of_trees "\
                                      "FROM import._311_service_requests_tree_trims) as tmp;"
        self.cur.execute(tree_trims_query)
        self.conn.commit()

        #drop import table
        # drop = "drop table if exists import._311_service_requests_tree_trims cascade;"
        # self.cur.execute(drop)
        # self.conn.commit()


    @staticmethod
    def get_table_name_reference(file):
        if "abandoned-vehicles" in file:
            return "abandoned_vehicles"
        elif "alley-lights-out" in file:
            return "alley_lights_out"
        elif "garbage-carts" in file:
            return "garbage_carts"
        elif "graffiti-removal" in file:
            return "graffiti_removal"
        elif "pot-holes-reported" in file:
            return "pot_holes_reported"
        elif "rodent-baiting" in file:
            return "rodent_baiting"
        elif "sanitation-code-complaints" in file:
            return "sanitation_code_complaints"
        elif "street-lights-all-out" in file:
            return "street_lights_all_out"
        elif "street-lights-one-out" in file:
            return "street_lights_one_out"
        elif "tree-debris" in file:
            return "tree_debris"
        elif "tree-trims" in file:
            return "tree_trims"

    def call_table_insert(self, table):
        if table == 'abandoned_vehicles':
            self.migrate_abandoned_vehicles()
            return
        elif "alley_lights_out" == table:
            return self.migrate_alley_lights_out()
        elif "garbage_carts" == table:
            return self.migrate_garbage_carts()
        elif "graffiti_removal" == table:
            return self.migrate_graffiti_removal()
        elif "pot_holes_reported" == table:
            return self.migrate_request_pot_holes()
        elif "rodent_baiting" == table:
            return self.migrate_rodent_bating()
        elif "sanitation_code_complaints" == table:
            return self.migrate_sanitation_code_complaint()
        elif "street_lights_all_out" == table:
            return
        elif "street_lights_one_out" == table:
            return
        elif "tree_debris" in table:
            return
        elif "tree_trims" in table:
            return self.migrate_tree_trims()


if __name__ == "__main__":
    migration = MigrateDb()
    migration.read()
