import glob
import os
import sys
import numpy as np
import psycopg2.extras as extras
from psycopg2.extensions import register_adapter
import psycopg2


class MigrateDb:
    def __init__(self):
        psycopg2.extensions.register_adapter(np.int64, psycopg2._psycopg.AsIs)
        self.path = '/mnt/5a5abb6b-4b3d-4ab8-bfea-9c7fc2ce23e6/linuxuser/Public/University/MSc/DataBases/Project1/archive/'
        self.conn = self._connect_()
        self.cur = self.conn.cursor()
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

    def read(self):
        """
        Read a resource from the file specified
        using the appropriate reader for this format
        """
        all_files = glob.glob(os.path.join(self.path, "*.csv"))
        print(all_files)
        for file in all_files:
            command = "./pgfutter --db \"db_311ci\" --port \"5432\" --user \"postgres\" --pw \"root\" csv " + file
            os.system(command)
        self.cur.close()
        print("All CSVs imported in schema import.\nNow execute migration queries.")


if __name__ == "__main__":
    migration = MigrateDb()
    migration.read()
