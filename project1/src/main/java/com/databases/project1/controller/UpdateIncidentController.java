package com.databases.project1.controller;


import com.databases.project1.dto.UpdateDto;
import com.databases.project1.entity.*;
import com.databases.project1.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Controller
public class UpdateIncidentController {

    @Autowired
    RequestTypeService requestTypeService;

    @Autowired
    IncidentService incidentService;

    @Autowired
    AbandonedVehicleService abandonedVehicleService;

    @Autowired
    AlleyLightsService alleyLightsService;

    @Autowired
    GarbageCartsService garbageCartsService;

    @Autowired
    GraffityRemovalService graffityRemovalService;

    @Autowired
    PotHolesService potHolesService;

    @Autowired
    RodentBaitingService rodentBaitingService;

    @Autowired
    SanitationComplaintService sanitationComplaintService;

    @Autowired
    TreeDebrisService treeDebrisService;

    @Autowired
    TreeTrimService treeTrimService;

    @Autowired
    ExtraIncidentInfoService extraIncidentInfoService;

    @Autowired
    DistrictService districtService;

    @Autowired
    LoggingService logService;

    @GetMapping("/update")
    public String update(Model theModel){
        Incident incident = new Incident();

        incident.setExtraIncidentInfo(new ExtraIncidentInfo());
        incident.setDistrict(new District());
        incident.setAbandonedVehicle(new AbandonedVehicle());
        incident.setAlleyLightsOut(new AlleyLightsOut());
        incident.setGarbageCarts(new GarbageCarts());
        incident.setGraffitiRemoval(new GraffitiRemoval());
        incident.setPotHolesReported(new PotHolesReported());
        incident.setTreeDebris(new TreeDebris());
        incident.setTreeTrims(new TreeTrims());
        incident.setRodentBaiting(new RodentBaiting());
        incident.setSanitationCodeComplaints(new SanitationCodeComplaints());

        theModel.addAttribute("updater",new UpdateDto());

        theModel.addAttribute("updateIncident",incident);

        theModel.addAttribute("abandonedVehicle", incident.getAbandonedVehicle());
        theModel.addAttribute("alleyLightsOut", incident.getAlleyLightsOut());
        theModel.addAttribute("garbageCarts", incident.getGarbageCarts());
        theModel.addAttribute("potHolesReported", incident.getPotHolesReported());
        theModel.addAttribute("rodentBaiting", incident.getRodentBaiting());
        theModel.addAttribute("sanitationCodeComplaints", incident.getSanitationCodeComplaints());
        theModel.addAttribute("district", incident.getDistrict());
        theModel.addAttribute("treeDebris", incident.getTreeDebris());
        theModel.addAttribute("treeTrims", incident.getTreeTrims());
        theModel.addAttribute("extraIncidentInfo", incident.getExtraIncidentInfo());

        return "Update";
    }

    @PostMapping("/showFormUpdate")
    public String showFormUpdate(@ModelAttribute("updater") UpdateDto updateDto, @ModelAttribute("updateIncident") Incident incident,Model theModel, HttpServletRequest request){

        RegisteredUser user = (RegisteredUser) request.getSession().getAttribute("user");

        updateDto.setIncident(incidentService.findByRequestNumber(updateDto.getServiceRequestNumber()));
        if (updateDto.getIncident() != null) {
            System.out.println("Update operation");
            updateDto.distributeData();
            incident = updateDto.getIncident();
            theModel.addAttribute("updater", updateDto);
            theModel.addAttribute("updateIncident",incident);

        }else {
            theModel.addAttribute("error", "no result list is available");
        }
        return "updateIncident";
    }


    @PostMapping("/processUpdate")
    public String UpdateIncident(@ModelAttribute("updater") UpdateDto updateDto, @ModelAttribute("updateIncident") Incident incident,
                                 @ModelAttribute("abandonedVehicle") AbandonedVehicle abandonedVehicle,
                                 @ModelAttribute("alleyLightsOut") AlleyLightsOut alleyLightsOut,
                                 @ModelAttribute("garbageCarts") GarbageCarts garbageCarts,
                                 @ModelAttribute("potHolesReported") PotHolesReported potHolesReported,
                                 @ModelAttribute("graffitiRemoval") GraffitiRemoval graffitiRemoval,
                                 @ModelAttribute("rodentBaiting") RodentBaiting rodentBaiting,
                                 @ModelAttribute("sanitationCodeComplaints") SanitationCodeComplaints sanitationCodeComplaints,
                                 @ModelAttribute("district") District district,
                                 @ModelAttribute("treeDebris") TreeDebris treeDebris,
                                 @ModelAttribute("treeTrims") TreeTrims treeTrims,
                                 @ModelAttribute("extraIncidentInfo") ExtraIncidentInfo extraIncidentInfo,
                     Model theModel, HttpServletRequest request){

        RegisteredUser user = (RegisteredUser) request.getSession().getAttribute("user");

        updateDto.setIncident(incidentService.findByRequestNumber(updateDto.getServiceRequestNumber()));
        if (incident.getServiceRequestNumber().equals(updateDto.getIncident().getServiceRequestNumber())) {
            System.out.println("Update operation");
            String requestType = updateDto.getIncident().getRequestType();
            incident.setId(updateDto.getIncident().getId());

            if (district != null){
                District districtFromTable = districtService.findIfDistrictExists(district.getCommunityArea(),
                        district.getPoliceDistrict(), district.getWard(), district.getZipcode());
                if(districtFromTable == null){
                    districtService.saveDistrict(district);
                }
                incident.setDistrict(district);
            }

            incident = incidentService.saveIncident(incident);

            /*insert the extra incident info if there are any*/
            if(extraIncidentInfo != null){
                extraIncidentInfo.setId(incident.getId());
                extraIncidentInfoService.saveExtraIncidentInfo(extraIncidentInfo);
            }

            if (requestType.equals("Abandoned Vehicle Complaint")) {
                abandonedVehicle.setId(incident.getId());
                abandonedVehicleService.saveVehicle(abandonedVehicle);
            }
            else if (requestType.equals("Alley Light Out")) {
                alleyLightsOut.setId(incident.getId());
                alleyLightsService.saveAlleyLights(alleyLightsOut);
            }
            else if (requestType.equals("Garbage Cart Black Maintenance/Replacement")) {
                garbageCarts.setId(incident.getId());
                garbageCartsService.saveGarbageCarts(garbageCarts);
            }
            else if (requestType.equals("Graffiti Removal")) {
                graffitiRemoval.setId(incident.getId());
                graffityRemovalService.saveGraffityRemoval(graffitiRemoval);
            }
            else if (requestType.equals("Pothole in Street")) {
                potHolesReported.setId(incident.getId());
                potHolesService.savePotHoles(potHolesReported);
            }
            else if (requestType.equals("Rodent Baiting/Rat Complaint")) {
                rodentBaiting.setId(incident.getId());
                rodentBaitingService.saveRodentBaiting(rodentBaiting);
            }
            else if (requestType.equals("Sanitation Code Violation")) {
                sanitationCodeComplaints.setId(incident.getId());
                sanitationComplaintService.saveSanitationComplaint(sanitationCodeComplaints);
            }
            else if (requestType.equals("Tree Trim")) {
                treeTrims.setId(incident.getId());
                treeTrimService.saveTreeTrim(treeTrims);
            }
            else if (requestType.equals("Tree Debris")) {
                treeDebris.setId(incident.getId());
                treeDebrisService.saveTreeDebris(treeDebris);
            }
            else if (requestType.equals("Street Lights - All/Out")) {
                alleyLightsOut.setId(incident.getId());
                alleyLightsService.saveAlleyLights(alleyLightsOut);
            }
            else if (requestType.equals("Street Light Out")) {
                alleyLightsOut.setId(incident.getId());
                alleyLightsService.saveAlleyLights(alleyLightsOut);
            }
            else if (requestType.equals("Street Lights - 1/Out")) {
                alleyLightsOut.setId(incident.getId());
                alleyLightsService.saveAlleyLights(alleyLightsOut);
            }

        }else {
            theModel.addAttribute("error", "This type of incident does not exist");
        }

        UserActionLog log = new UserActionLog();
        log.setRegUser(user);
        log.setUserName(user.getUserName());
        log.setActionTimeStamp(new Timestamp(System.currentTimeMillis()));
        log.setUserAction("Update operation by user " + log.getUserName() +
                ". Request type: " + incident.getRequestType() + ", Request number: " + updateDto.getServiceRequestNumber());
        logService.logAction(log);
        return "redirect:/update";
    }


}
