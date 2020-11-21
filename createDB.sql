CREATE DATABASE db_311ci;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE USER_ACTION_LOG(
    USER_ACTION_LOG_ID UUID PRIMARY KEY,
    LOGIN_STAMP TIMESTAMP
);

CREATE TABLE REGISTERED_USER(
    USER_ID UUID PRIMARY KEY,
    USERNAME VARCHAR(50) UNIQUE,
    PASSWORD VARCHAR(50),
    CREATED_ON TIMESTAMP,
    USER_ACTION_LOG_ID UUID,
    FOREIGN KEY (USER_ACTION_LOG_ID)
        REFERENCES USER_ACTION_LOG(USER_ACTION_LOG_ID)
);

CREATE TABLE REQUEST_TYPE(
    REQUEST_TYPE_ID UUID PRIMARY KEY,
    NAME VARCHAR(42) UNIQUE
);

CREATE TABLE EXTRA_INCIDENT_INFO(
    EXTRA_INCIDENT_INFO_ID UUID PRIMARY KEY,
    HISTORICAL_WARDS_2003_2015 INTEGER,
    ZIP_CODES VARCHAR(10),
    COMMUNITY_AREAS INTEGER,
    CENSUS_TRACKS INTEGER,
    WARDS INTEGER
);

CREATE TABLE DISTRICT(
    DISTRICT_ID UUID PRIMARY KEY,
    ZIP_CODE VARCHAR(10),
    WARD INTEGER,
    POLICE_DISTRICT INTEGER,
    COMMUNITY_AREA INTEGER
);

CREATE TABLE STATUS_TYPE(
    STATUS_TYPE_ID UUID PRIMARY KEY,
    STATUS_TYPE_NAME VARCHAR(42)
);

CREATE TABLE INCIDENT(
    INCIDENT_ID UUID PRIMARY KEY,
    USER_ID UUID,
    CREATION_DATE DATE,
    COMPLETION_DATE DATE,
    SERVICE_REQUEST_NUMBER INTEGER UNIQUE,
    STREET_ADDRESS VARCHAR(42),
    LONGTITUDE FLOAT,
    LATITUDE FLOAT,
    X_COORDINATE FLOAT,
    Y_COORDINATE FLOAT,
    REQUEST_TYPE_ID UUID,
    EXTRA_INCIDENT_INFO_ID UUID,
    STATUS_TYPE_ID UUID,
    DISTRICT_ID UUID,
    FOREIGN KEY (USER_ID)
        REFERENCES REGISTERED_USER(USER_ID),
    FOREIGN KEY (REQUEST_TYPE_ID)
        REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID),
    FOREIGN KEY (EXTRA_INCIDENT_INFO_ID)
        REFERENCES EXTRA_INCIDENT_INFO(EXTRA_INCIDENT_INFO_ID),
    FOREIGN KEY (STATUS_TYPE_ID)
        REFERENCES STATUS_TYPE(STATUS_TYPE_ID),
    FOREIGN KEY (DISTRICT_ID)
        REFERENCES DISTRICT(DISTRICT_ID)

);

CREATE TABLE SSA(
    SSA_ID UUID PRIMARY KEY,
    SSA INTEGER
);

CREATE TABLE ABANDONED_VEHICLE(
    ID UUID PRIMARY KEY,
    REQUEST_ID UUID,
    SSA_ID UUID,
    LICENSE_PLATE VARCHAR(42),
    VEHICLE_MAKE_MODEL VARCHAR(42),
    VEHICLE_COLOR VARCHAR(42),
    CURRENT_ACTIVITY VARCHAR(120),
    MOST_RECENT_ACTION VARCHAR(120),
    DAYS_REPORTED_PARKED INTEGER,
    FOREIGN KEY (REQUEST_ID)
        REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID),
    FOREIGN KEY (SSA_ID)
        REFERENCES SSA(SSA_ID)
);

CREATE TABLE ALLEY_LIGHTS_OUT(
    ID UUID PRIMARY KEY,
    REQUEST_ID UUID,
    FOREIGN KEY (REQUEST_ID)
        REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID)
);

CREATE TABLE GARBAGE_CARTS(
    ID UUID PRIMARY KEY,
    REQUEST_ID UUID,
    DELIVERED_BLACK_CARTS_NUM INTEGER,
    CURRENT_ACTIVITY VARCHAR(120),
    MOST_RECENT_ACTION VARCHAR(120),
    SSA_ID UUID,
    FOREIGN KEY (REQUEST_ID)
      REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID),
    FOREIGN KEY (SSA_ID)
        REFERENCES SSA(SSA_ID)
);

CREATE TABLE GRAFFITI_REMOVAL(
    ID UUID PRIMARY KEY,
    REQUEST_ID UUID,
    SURFACE_TYPE VARCHAR(120),
    GRAFFITI_LOCATION VARCHAR(120),
    SSA_ID UUID,
    FOREIGN KEY (REQUEST_ID)
        REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID),
    FOREIGN KEY (SSA_ID)
        REFERENCES SSA(SSA_ID)
);

CREATE TABLE POT_HOLES_REPORTED(
    ID UUID PRIMARY KEY,
    REQUEST_ID UUID,
    CURRENT_ACTIVITY VARCHAR(120),
    MOST_RECENT_ACTION VARCHAR(120),
    FILLED_BLOCK_POTHOLES_NUM INTEGER,
    SSA_ID UUID,
    FOREIGN KEY (REQUEST_ID)
       REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID),
    FOREIGN KEY (SSA_ID)
        REFERENCES SSA(SSA_ID)
);

CREATE TABLE RODENT_BATING(
    ID UUID PRIMARY KEY,
    REQUEST_ID UUID,
    BAITED_PREMISES_NUM INTEGER,
    PREMISES_WITH_GARBAGE_NUM INTEGER,
    PREMISES_WITH_RATS_NUM INTEGER,
    CURRENT_ACTIVITY VARCHAR(120),
    MOST_RECENT_ACTION VARCHAR(120),
    FOREIGN KEY (REQUEST_ID)
      REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID)
);

CREATE TABLE SANITATION_CODE_COMPLAINTS(
    ID UUID PRIMARY KEY,
    REQUEST_ID UUID,
    VIOLATION_NATURE VARCHAR(120),
    FOREIGN KEY (REQUEST_ID)
       REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID)
);

CREATE TABLE STREET_LIGHTS_ALL_OUT(
    ID UUID PRIMARY KEY,
    REQUEST_ID UUID,
    FOREIGN KEY (REQUEST_ID)
      REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID)
);

CREATE TABLE STREET_LIGHTS_ONE_OUT(
    ID UUID PRIMARY KEY,
    REQUEST_ID UUID,
    FOREIGN KEY (REQUEST_ID)
      REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID)
);

CREATE TABLE TREE_DEBRIS(
    ID UUID PRIMARY KEY,
    REQUEST_ID UUID,
    DEBRIS_LOCATION VARCHAR(120),
    CURRENT_ACTIVITY VARCHAR(120),
    MOST_RECENT_ACTION VARCHAR(120),
    FOREIGN KEY (REQUEST_ID)
        REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID)
);

CREATE TABLE TREE_TRIMS(
    ID UUID PRIMARY KEY,
    REQUEST_ID UUID,
    TREE_LOCATION VARCHAR(120),
    FOREIGN KEY (REQUEST_ID)
       REFERENCES REQUEST_TYPE(REQUEST_TYPE_ID)
);


