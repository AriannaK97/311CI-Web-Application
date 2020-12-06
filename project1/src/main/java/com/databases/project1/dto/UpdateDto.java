package com.databases.project1.dto;

import com.databases.project1.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {

    private Incident incident = null;
    private String serviceRequestNumber = "";
    private String incident_id = "";

    public void distributeData(){
        incident_id = incident.getId().toString();
        if(incident.getAbandonedVehicle() == null){
            incident.setAbandonedVehicle(new AbandonedVehicle());
            incident.getAbandonedVehicle().setId(null);
            incident.getAbandonedVehicle().setCurrentActivity("");
            incident.getAbandonedVehicle().setMostRecentAction("");
            incident.getAbandonedVehicle().setDaysReportedParked(null);
            incident.getAbandonedVehicle().setLicensePlate("");
            incident.getAbandonedVehicle().setSSA(0);
            incident.getAbandonedVehicle().setVehicleColor("");
            incident.getAbandonedVehicle().setVehicleMakeModel("");
        }
        if(incident.getAlleyLightsOut() == null) {
            incident.setAlleyLightsOut(new AlleyLightsOut());
            incident.getAlleyLightsOut().setId(null);
        }

        if(incident.getExtraIncidentInfo() == null) {
            incident.setExtraIncidentInfo(new ExtraIncidentInfo());
            incident.getExtraIncidentInfo().setCencusTracts(0);
            incident.getExtraIncidentInfo().setCommunityAreas(0);
            incident.getExtraIncidentInfo().setHistorical_wards_2003_2015(0);
            incident.getExtraIncidentInfo().setZipcodes(0);
            incident.getExtraIncidentInfo().setId(null);
        }

        if(incident.getDistrict() == null) {
            incident.setDistrict(new District());
            incident.getDistrict().setCommunityArea(0);
            incident.getDistrict().setPoliceDistrict(0);
            incident.getDistrict().setWard(0);
            incident.getDistrict().setZipcode(0);
            incident.getDistrict().setId(null);
        }

        if(incident.getGarbageCarts() == null) {
            incident.setGarbageCarts(new GarbageCarts());
            incident.getGarbageCarts().setCurrentActivity("");
            incident.getGarbageCarts().setDeliveredBlackCartsNum(null);
            incident.getGarbageCarts().setMostRecentAction("");
            incident.getGarbageCarts().setSSA(0);
            incident.getGarbageCarts().setId(null);
        }

        if(incident.getGraffitiRemoval() == null) {
            incident.setGraffitiRemoval(new GraffitiRemoval());
            incident.getGraffitiRemoval().setGraffitiLocation("");
            incident.getGraffitiRemoval().setSSA(0);
            incident.getGraffitiRemoval().setSurfaceType("");
            incident.getGraffitiRemoval().setId(null);
        }

        if(incident.getSanitationCodeComplaints() == null) {
            incident.setSanitationCodeComplaints(new SanitationCodeComplaints());
            incident.getSanitationCodeComplaints().setViolationNature("");
            incident.getSanitationCodeComplaints().setId(null);
        }

        if(incident.getRodentBaiting() == null) {
            incident.setRodentBaiting(new RodentBaiting());
            incident.getRodentBaiting().setBaitedPremisesNum(null);
            incident.getRodentBaiting().setCurrentActivity("");
            incident.getRodentBaiting().setMostRecentAction("");
            incident.getRodentBaiting().setPremisesWithGarbageNum(null);
            incident.getRodentBaiting().setPremisesWithRatsNum(null);
            incident.getRodentBaiting().setId(null);
        }

        if(incident.getTreeDebris() == null) {
            incident.setTreeDebris(new TreeDebris());
            incident.getTreeDebris().setCurrentActivity("");
            incident.getTreeDebris().setDebrisLocation("");
            incident.getTreeDebris().setMostRecentAction("");
            incident.getTreeDebris().setId(null);
        }

        if(incident.getTreeTrims() == null) {
            incident.setTreeTrims(new TreeTrims());
            incident.getTreeTrims().setTreeLocation("");
            incident.getTreeTrims().setId(null);
        }

        if(incident.getPotHolesReported() == null) {
            incident.setPotHolesReported(new PotHolesReported());
            incident.getPotHolesReported().setCurrentActivity("");
            incident.getPotHolesReported().setFilledBlockPotholesNum(null);
            incident.getPotHolesReported().setMostRecentAction("");
            incident.getPotHolesReported().setSSA(0);
            incident.getPotHolesReported().setId(null);
        }
    }

}
