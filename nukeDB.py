import psycopg2
import os
import shutil
try:
    currentDir = os.path.dirname(os.path.realpath(__file__))
    # In order for this to work, go to the postgres terminal and type:
    #   alter role postgres with password 'fuckmigrations';
    connection = psycopg2.connect(
        user="postgres",
        password="pass",
        host="127.0.0.1",
        port="5432",
        database="postgres")
    connection.autocommit = True
    cursor = connection.cursor()
    cursor.execute("drop database db_311ci;")
    cursor.execute("CREATE DATABASE db_311ci;")
    cursor.execute("GRANT ALL PRIVILEGES ON DATABASE db_311ci TO postgres;")
    print("Cleared folders and reacreated database. Now do the migration.")
except (Exception, psycopg2.Error) as error:
    print("Error while connecting to PostgreSQL", error)