import glob
import os
import sys
import uuid
from tqdm import tqdm
import time
import numpy as np
import psycopg2.extras as extras
from psycopg2.extensions import register_adapter, AsIs
import psycopg2
import pandas as pd


class MigrateDb:

    def __init__(self):
        psycopg2.extensions.register_adapter(np.int64, psycopg2._psycopg.AsIs)
        self.path = '/mnt/5a5abb6b-4b3d-4ab8-bfea-9c7fc2ce23e6/linuxuser/Public/University/MSc/DataBases/Project1/archive/'
        self.conn = self._connect_()
        self.cur = self.conn.cursor()
        self.admin_user_id = self._insert_admin_user_()
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
        self.cur.execute("INSERT INTO registered_user (user_id, username, password, firstname, lastname, email) "
                         "VALUES (%s, %s, %s, %s, %s, %s) RETURNING user_id", (uuid.uuid4(), 'admin', 'pass', 'admin',
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
        for file in all_files:
            print(file)
            iter_ += 1
            iter2 = 0
            chunksize = 100000
            #df = pd.read_csv(file, index_col=None, header=0, nrows=1000)
            for df in pd.read_csv(file, index_col=None, header=0, chunksize=chunksize):
                iter2 += 1
                table = self.get_table_name_reference(file)
                print("Migrating table:" + table)
                df = df.applymap(lambda s: s.lower() if type(s) == str else s)
                df.columns = df.columns.str.lower()
                if table == 'pot_holes_reported':
                    df.rename(columns={'zip': 'zip code'}, inplace=True)
                self.insert_ssa(df, table)
                self.insert_request_type(df)
                self.insert_status_type(df)
                self.insert_district(df)
                self.insert_incident(df, table)
        ##comment here to reach all csv entries##
                if iter2 == 2:
                    break
            if iter_ == 11:
                break
        print(len(all_files))
        self.cur.close()

    def execute_batch(self, query, df, page_size=100):
        """
        Using psycopg2.extras.execute_batch() to insert the dataframe
        """
        # Create a list of tuples from the dataframe values
        tuples = [tuple(x) for x in df.to_numpy()]

        try:
            extras.execute_batch(self.cur, query, tuples, page_size)
            self.conn.commit()
        except (Exception, psycopg2.DatabaseError) as error:
            print("Error: %s" % error)
            self.conn.rollback()
            self.cur.close()
            return exit(1)
        print("execute_batch() done")

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

    def call_table_insert(self, table, row, request_type_id):
        if table == 'abandoned_vehicles':
            self.insert_abandoned_vehicle(row, request_type_id)
            return
        elif "alley_lights_out" == table:
            self.insert_alley_lights(request_type_id)
            return
        elif "garbage_carts" == table:
            self.insert_garbage_carts(row, request_type_id)
            return
        elif "graffiti_removal" == table:
            self.insert_graffiti_removal(row, request_type_id)
            return
        elif "pot_holes_reported" == table:
            self.insert_pot_holes_reported(row, request_type_id)
            return
        elif "rodent_baiting" == table:
            self.insert_rodent_bating(row, request_type_id)
            return
        elif "sanitation_code_complaints" == table:
            self.insert_sanitation_code_complaints(row, request_type_id)
            return
        elif "street_lights_all_out" == table:
            self.insert_street_lights_all_out(request_type_id)
            return
        elif "street_lights_one_out" == table:
            self.insert_street_lights_one_out(request_type_id)
            return
        elif "tree_debris" in table:
            self.insert_tree_debris(row, request_type_id)
            return
        elif "tree_trims" in table:
            self.insert_tree_trims(row, request_type_id)
            return

    # Todo: check for service_request_number position in incident -  migh need to move it on request type
    def insert_incident(self, df, table):
        # SQL quert to execute
        for index, row in tqdm(df.iterrows(), desc="Migrating...", ascii=False, ncols=75):
            psycopg2.extras.register_uuid()
            new_uuid = uuid.uuid4()

            extra_incident_info_id = None
            if table != 'street_lights_one_out':
                incident_inf_df = row[
                    ['historical wards 2003-2015', 'zip codes', 'community areas', 'census tracts', 'wards']]
                extra_incident_info_id = self.insert_extra_incident_info(incident_inf_df)

            self.cur.execute("SELECT request_type_id FROM request_type where name = %s",
                             (row['type of service request'],))
            request_type_id = self.cur.fetchone()

            self.cur.execute("SELECT district_id FROM district where zip_code = %s and ward = %s and "
                             "police_district = %s and community_area = %s", (row["zip code"], row["ward"],
                                                                              row["police district"],
                                                                              row["community area"],))
            district_id = self.cur.fetchone()

            self.cur.execute("SELECT status_type_id FROM status_type where status_type_name = %s", (row["status"],))
            status_type_id = self.cur.fetchone()
            try:
                query = "INSERT INTO incident (incident_id, user_id, creation_date, completion_date, " \
                        "service_request_number, street_address, longitude, latitude, x_coordinate, y_coordinate, " \
                        "request_type_id, extra_incident_info_id, status_type_id, district_id) VALUES(%s, %s, %s, %s, %s, " \
                        "%s, %s, %s, %s, %s, %s, %s, %s, %s)"

                self.call_table_insert(table, row, request_type_id)
                row = row.fillna('NULL')
                row[["longitude", "latitude", "x coordinate", "y coordinate"]] = \
                    row[["longitude", "latitude", "x coordinate", "y coordinate"]].replace('NULL', None)
                row["completion date"] = row["completion date"].replace('open', '')
                row[["creation date", "completion date", "service request number"]] = \
                    row[["creation date", "completion date", "service request number"]].replace('NULL', )

                self.cur.execute(query, (new_uuid, self.admin_user_id, row['creation date'], row['completion date'],
                                         row['service request number'], row['street address'], row['longitude'],
                                         row['latitude'], row['x coordinate'], row['y coordinate'],
                                         request_type_id, extra_incident_info_id, status_type_id, district_id))
                self.conn.commit()

            except (Exception, psycopg2.DatabaseError) as error:
                self.number_of_missed_entries += 1
                print("Error: %s \n" % (error,))
                self.conn.rollback()
                # if index == 1000:
                #     return
                continue

            # if index == 1000:
            #     return

    def insert_request_type(self, df):
        data = df['type of service request'].unique()
        for d in data:
            psycopg2.extras.register_uuid()
            q1 = "SELECT name FROM request_type WHERE name = %s"
            self.cur.execute(q1, (d, ))
            self.conn.commit()
            if self.cur.fetchone() is None:
                query = "INSERT INTO request_type (request_type_id, name) VALUES (%s, %s)"
                self.cur.execute(query, (uuid.uuid4(), d))
                self.conn.commit()

    def insert_status_type(self, df):
        data = df['status'].unique()
        for d in data:
            psycopg2.extras.register_uuid()
            q1 = "SELECT * FROM status_type WHERE status_type_name = %s"
            self.cur.execute(q1, (d, ))
            self.conn.commit()
            if self.cur.fetchone() is None:
                query = "INSERT INTO status_type (status_type_id, status_type_name) VALUES (%s, %s)"
                self.cur.execute(query, (uuid.uuid4(), d))
                self.conn.commit()

    def insert_extra_incident_info(self, row):
        row = pd.DataFrame(row).T
        row = row.fillna(-1)
        row = row.astype({'historical wards 2003-2015': int, 'zip codes': int, 'community areas': int,
                          'census tracts': int, 'wards': int})
        row = row.replace(-1, None)

        psycopg2.extras.register_uuid()
        query = "INSERT INTO extra_incident_info (extra_incident_info_id, historical_wards_2003_2015, zip_codes," \
                " community_areas, census_tracts, wards) VALUES (%s, %s, %s, %s, %s, %s) " \
                "RETURNING extra_incident_info_id"

        self.cur.execute(query, (uuid.uuid4(), row['historical wards 2003-2015'].values[0], row['zip codes'].values[0],
                                 row['community areas'].values[0], row['census tracts'].values[0],
                                 row['wards'].values[0]))
        self.conn.commit()
        return self.cur.fetchone()[0]

    def insert_district(self, df):
        district_df = pd.DataFrame(df[['zip code', 'ward', 'police district', 'community area']])
        district_df.sort_values('police district', inplace=True)
        district_df.drop_duplicates(subset=['zip code', 'police district', 'community area', 'ward'], inplace=True)
        district_df = district_df.dropna(how='all')
        district_df = district_df.reset_index(drop=True)
        district_df = district_df.fillna(-1)
        district_df = district_df.astype({'zip code': int, 'police district': int, 'community area': int, 'ward': int})
        district_df = district_df.replace(-1, None)

        for index, row in district_df.iterrows():
            psycopg2.extras.register_uuid()
            q1 = "SELECT * FROM district WHERE zip_code= %s AND ward= %s AND " \
                 "police_district = %s AND community_area = %s"
            self.cur.execute(q1, (row["zip code"], row["ward"], row["police district"],
                                         row["community area"]))
            self.conn.commit()
            if self.cur.fetchone() is None:
                query = "INSERT INTO district (district_id, zip_code, ward, police_district, community_area) " \
                        "VALUES (%s, %s, %s, %s, %s)"
                self.cur.execute(query, (uuid.uuid4(), row["zip code"], row["ward"], row["police district"],
                                         row["community area"]))
                self.conn.commit()

    def insert_ssa(self, df, table):
        if table == "abandoned_vehicles" or table == "garbage-carts" or table == "graffiti-removal" or table == "pot-holes-reported":
            s_df = pd.DataFrame(df['ssa'])
            s_df.drop_duplicates(subset=['ssa'], inplace=True)
            s_df = s_df.dropna(how='all')
            s_df = s_df.reset_index(drop=True)
            data = s_df['ssa']
            psycopg2.extras.register_uuid()
            for d in data:
                q1 = "SELECT ssa.ssa FROM ssa WHERE ssa.ssa = %s"
                self.cur.execute(q1, (d,))
                self.conn.commit()
                if self.cur.fetchone() is None:
                    query = "INSERT INTO ssa (ssa_id, ssa)  SELECT %s, %s"
                    self.cur.execute(query, (uuid.uuid4(), d))
                    self.conn.commit()

    def insert_abandoned_vehicle(self, row, request_type_id):
        i_df = pd.DataFrame(row).T
        i_df = pd.DataFrame(i_df[['license plate', 'vehicle make/model', 'vehicle color', 'current activity',
                                  'most recent action', 'how many days has the vehicle been reported as parked?',
                                  'ssa']])
        i_df[['license plate', 'vehicle make/model', 'vehicle color', 'current activity', 'most recent action']] = \
            i_df[['license plate', 'vehicle make/model', 'vehicle color', 'current activity',
                  'most recent action']].fillna('NULL')
        i_df[['how many days has the vehicle been reported as parked?', 'ssa']] = \
            i_df[['how many days has the vehicle been reported as parked?', 'ssa']].fillna(-1)
        i_df[['how many days has the vehicle been reported as parked?', 'ssa']] = \
            i_df[['how many days has the vehicle been reported as parked?', 'ssa']].replace(np.nan, None)
        i_df[['license plate', 'vehicle make/model', 'vehicle color', 'current activity', 'most recent action']] = \
            i_df[['license plate', 'vehicle make/model', 'vehicle color', 'current activity',
                  'most recent action']].replace(np.nan, )

        self.cur.execute("SELECT ssa_id FROM ssa WHERE ssa.ssa = %s", (i_df['ssa'].values[0],))
        ssa_id = self.cur.fetchone()

        psycopg2.extras.register_uuid()
        query = "INSERT INTO abandoned_vehicle (id, request_id, ssa_id, license_plate, vehicle_make_model, " \
                "vehicle_color, current_activity, most_recent_action, days_reported_parked) " \
                "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s) "
        self.cur.execute(query, (uuid.uuid4(), request_type_id, ssa_id, i_df['license plate'].values[0],
                                 i_df['vehicle make/model'].values[0], i_df['vehicle color'].values[0],
                                 i_df['current activity'].values[0], i_df['most recent action'].values[0],
                                 i_df['how many days has the vehicle been reported as parked?'].values[0]))
        self.conn.commit()

    def insert_alley_lights(self, request_type_id):
        psycopg2.extras.register_uuid()
        query = "INSERT INTO alley_lights_out (id, request_id) VALUES (%s, %s)"
        self.cur.execute(query, (uuid.uuid4(), request_type_id))
        self.conn.commit()

    def insert_garbage_carts(self, row, request_type_id):
        i_df = pd.DataFrame(row).T
        i_df = pd.DataFrame(i_df[['number of black carts delivered', 'current activity', 'most recent action', 'ssa']])
        i_df[['number of black carts delivered', 'ssa']] = i_df[['number of black carts delivered', 'ssa']].fillna(-1)
        i_df[['number of black carts delivered', 'ssa']] = i_df[['number of black carts delivered', 'ssa']].replace(
            np.nan, None)
        i_df[['current activity', 'most recent action']] = \
            i_df[['current activity', 'most recent action']].fillna('NULL')
        i_df[['current activity', 'most recent action']] = \
            i_df[['current activity', 'most recent action']].replace(np.nan, )

        self.cur.execute("SELECT ssa_id FROM ssa WHERE ssa.ssa = %s", (i_df['ssa'].values[0],))
        ssa_id = self.cur.fetchone()

        psycopg2.extras.register_uuid()
        query = "INSERT INTO garbage_carts (id, request_id, delivered_black_carts_num, current_activity, " \
                "most_recent_action, ssa_id) VALUES (%s, %s, %s, %s, %s, %s) "
        self.cur.execute(query, (uuid.uuid4(), request_type_id, i_df['number of black carts delivered'].values[0],
                                 i_df['current activity'].values[0], i_df['most recent action'].values[0], ssa_id))
        self.conn.commit()

    def insert_graffiti_removal(self, row, request_type_id):
        i_df = pd.DataFrame(row).T
        i_df = pd.DataFrame(i_df[['what type of surface is the graffiti on?', 'where is the graffiti located?', 'ssa']])
        i_df[['what type of surface is the graffiti on?', 'where is the graffiti located?']] = \
            i_df[['what type of surface is the graffiti on?', 'where is the graffiti located?']].fillna('NULL')
        i_df[['ssa']] = i_df[['ssa']].fillna(-1)
        i_df[['ssa']] = i_df[['ssa']].replace(np.nan, None)
        i_df[['what type of surface is the graffiti on?', 'where is the graffiti located?']] = \
            i_df[['what type of surface is the graffiti on?', 'where is the graffiti located?']].replace(np.nan, )

        self.cur.execute("SELECT ssa_id FROM ssa WHERE ssa.ssa = %s", (i_df['ssa'].values[0],))
        ssa_id = self.cur.fetchone()

        psycopg2.extras.register_uuid()
        query = "INSERT INTO graffiti_removal (id, request_id, surface_type, graffiti_location, ssa_id) " \
                "VALUES (%s, %s, %s, %s, %s) "
        self.cur.execute(query,
                         (uuid.uuid4(), request_type_id, i_df['what type of surface is the graffiti on?'].values[0],
                          i_df['where is the graffiti located?'].values[0], ssa_id))
        self.conn.commit()

    def insert_pot_holes_reported(self, row, request_type_id):
        i_df = pd.DataFrame(row).T
        i_df = pd.DataFrame(
            i_df[['number of potholes filled on block', 'current activity', 'most recent action', 'ssa']])

        i_df[['current activity', 'most recent action']] = i_df[['current activity', 'most recent action']].fillna(
            'NULL')

        i_df[['number of potholes filled on block', 'ssa']] = i_df[
            ['number of potholes filled on block', 'ssa']].fillna(-1)

        i_df[['number of potholes filled on block', 'ssa']] = \
            i_df[['number of potholes filled on block', 'ssa']].replace(np.nan, None)

        i_df[['current activity', 'most recent action']] = \
            i_df[['current activity', 'most recent action']].replace(np.nan, )

        self.cur.execute("SELECT ssa_id FROM ssa WHERE ssa.ssa = %s", (i_df['ssa'].values[0],))
        ssa_id = self.cur.fetchone()

        psycopg2.extras.register_uuid()
        query = "INSERT INTO pot_holes_reported (id, request_id, current_activity, most_recent_action, " \
                "filled_block_potholes_num, ssa_id) VALUES (%s, %s, %s, %s, %s, %s) "
        self.cur.execute(query, (uuid.uuid4(), request_type_id, i_df['current activity'].values[0],
                                 i_df['most recent action'].values[0],
                                 i_df['number of potholes filled on block'].values[0], ssa_id))
        self.conn.commit()

    def insert_rodent_bating(self, row, request_type_id):
        i_df = pd.DataFrame(row).T
        i_df = pd.DataFrame(i_df[['number of premises baited', 'number of premises with garbage',
                                  'number of premises with rats', 'current activity', 'most recent action']])
        i_df[['current activity', 'most recent action']] = i_df[['current activity', 'most recent action']].fillna(
            'NULL')
        i_df[['number of premises baited', 'number of premises with garbage', 'number of premises with rats']] = \
            i_df[['number of premises baited', 'number of premises with garbage',
                  'number of premises with rats']].fillna(-1)
        i_df[['number of premises baited', 'number of premises with garbage', 'number of premises with rats']] = \
            i_df[['number of premises baited', 'number of premises with garbage',
                  'number of premises with rats']].replace(np.nan, None)
        i_df[['current activity', 'most recent action']] = i_df[['current activity', 'most recent action']].replace(
            np.nan, )

        psycopg2.extras.register_uuid()
        query = "INSERT INTO rodent_bating (id, request_id, baited_premises_num, premises_with_garbage_num, " \
                "premises_with_rats_num, current_activity, most_recent_action) " \
                "VALUES (%s, %s, %s, %s, %s, %s, %s) "
        self.cur.execute(query, (uuid.uuid4(), request_type_id, i_df['number of premises baited'].values[0],
                                 i_df['number of premises with garbage'].values[0],
                                 i_df['number of premises with rats'].values[0],
                                 i_df['current activity'].values[0], i_df['most recent action'].values[0]))
        self.conn.commit()

    def insert_sanitation_code_complaints(self, row, request_type_id):
        i_df = pd.DataFrame(row).T
        i_df = pd.DataFrame(i_df['what is the nature of this code violation?'])
        i_df['what is the nature of this code violation?'] = i_df['what is the nature of this code violation?'].fillna(
            'NULL')
        i_df['what is the nature of this code violation?'] = i_df['what is the nature of this code violation?'].replace(
            np.nan, )

        psycopg2.extras.register_uuid()
        query = "INSERT INTO sanitation_code_complaints (id, request_id, violation_nature) VALUES (%s, %s, %s)"
        self.cur.execute(query,
                         (uuid.uuid4(), request_type_id, i_df['what is the nature of this code violation?'].values[0]))
        self.conn.commit()

    def insert_street_lights_all_out(self, request_type_id):
        psycopg2.extras.register_uuid()
        query = "INSERT INTO street_lights_all_out (id, request_id) VALUES (%s, %s)"
        self.cur.execute(query, (uuid.uuid4(), request_type_id))
        self.conn.commit()

    def insert_street_lights_one_out(self, request_type_id):
        psycopg2.extras.register_uuid()
        query = "INSERT INTO street_lights_one_out (id, request_id) VALUES (%s, %s)"
        self.cur.execute(query, (uuid.uuid4(), request_type_id))
        self.conn.commit()

    def insert_tree_debris(self, row, request_type_id):
        i_df = pd.DataFrame(row).T
        i_df = pd.DataFrame(i_df[['if yes, where is the debris located?', 'current activity', 'most recent action']])
        i_df[['if yes, where is the debris located?', 'current activity', 'most recent action']] \
            = i_df[['if yes, where is the debris located?', 'current activity', 'most recent action']].fillna('NULL')
        i_df[['if yes, where is the debris located?', 'current activity', 'most recent action']] \
            = i_df[['if yes, where is the debris located?', 'current activity', 'most recent action']].replace(np.nan, )

        psycopg2.extras.register_uuid()
        query = "INSERT INTO tree_debris (id, request_id, debris_location, current_activity, most_recent_action) " \
                "VALUES (%s, %s, %s, %s, %s)"
        self.cur.execute(query, (uuid.uuid4(), request_type_id, i_df['if yes, where is the debris located?'].values[0],
                                 i_df['current activity'].values[0], i_df['most recent action'].values[0]))
        self.conn.commit()

    def insert_tree_trims(self, row, request_type_id):
        i_df = pd.DataFrame(row).T
        i_df = pd.DataFrame(i_df['location of trees'])
        i_df['location of trees'] = i_df['location of trees'].fillna('NULL')
        i_df['location of trees'] = i_df['location of trees'].replace(np.nan, )

        psycopg2.extras.register_uuid()
        query = "INSERT INTO tree_trims (id, request_id, tree_location) VALUES (%s, %s, %s)"
        self.cur.execute(query, (uuid.uuid4(), request_type_id, i_df['location of trees'].values[0]))
        self.conn.commit()


if __name__ == "__main__":
    migration = MigrateDb()
    migration.read()
