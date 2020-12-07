create unique index district_id_uindex
	on district (community_area, police_district, ward, zip_code);

--Index between request types and incident ids
--9.345
create index request_incident_id_index
    on incident using btree (request_type, id);

create index request_type_index
    on incident using btree (request_type);