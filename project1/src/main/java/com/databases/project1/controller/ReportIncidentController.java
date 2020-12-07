package com.databases.project1.controller;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportIncidentController {

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


    @GetMapping("/showInsert")
    public String showInsert(Model theModel) {
        Incident incident = new Incident();
        theModel.addAttribute("incident",incident);
        theModel.addAttribute("abandonedVehicle", new AbandonedVehicle());
        theModel.addAttribute("alleyLightsOut", new AlleyLightsOut());
        theModel.addAttribute("garbageCarts", new GarbageCarts());
        theModel.addAttribute("potHolesReported", new PotHolesReported());
        theModel.addAttribute("rodentBaiting", new RodentBaiting());
        theModel.addAttribute("sanitationCodeComplaints", new SanitationCodeComplaints());
        theModel.addAttribute("district", new District());
        theModel.addAttribute("treeDebris", new TreeDebris());
        theModel.addAttribute("treeTrims", new TreeTrims());
        theModel.addAttribute("extraIncidentInfo", new ExtraIncidentInfo());
        return "IncidentReport";
    }


    @PostMapping("/insert")
    public String insertIncident(@ModelAttribute("incident") Incident incident,
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
                                         Model theModel, HttpServletRequest request) {

        RegisteredUser user = (RegisteredUser) request.getSession().getAttribute("user");
        List<String> requestTypes = requestTypeService.findAllNames();
        String requestType = incident.getRequestType();

        if (requestType == null || requestTypes.stream().noneMatch(type -> type.equals(requestType))) {
            theModel.addAttribute("error","No such request type. Insertion or update aborted");
            return "redirect:/report/showInsert";
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        incident.setServiceRequestNumber(dateFormat.format(date) + incident.getRequestType().length());
        incident.setCreationDate(new java.sql.Date(date.getTime()));

        /*insert the district if the district combination does not already exist in table district*/
        if (district != null){
            District districtFromTable = districtService.findIfDistrictExists(district.getCommunityArea(),
                    district.getPoliceDistrict(), district.getWard(), district.getZipcode());
            if(districtFromTable == null){
                incident.setDistrict(districtService.saveDistrict(district));
            }
            else {
                incident.setDistrict(districtFromTable);
            }
        }

        incidentService.saveIncident(incident);

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

        UserActionLog log = new UserActionLog();
        log.setRegUser(user);
        log.setUserName(user.getUserName());
        log.setActionTimeStamp(new Timestamp(System.currentTimeMillis()));
        log.setUserAction("Import operation by user " + log.getUserName() +
                ". Request type: " + incident.getRequestType() + ", Request number: " + incident.getServiceRequestNumber());
        logService.logAction(log);
        return "redirect:/report/showInsert";

    }

}
