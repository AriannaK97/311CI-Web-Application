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


    @GetMapping("/showInsert")
    public String showInsert(Model theModel) {
        Incident incident = new Incident();
        incident.setAbandonedVehicle(new AbandonedVehicle());
        incident.setAlleyLightsOut(new AlleyLightsOut());
        incident.setGarbageCarts(new GarbageCarts());
        incident.setPotHolesReported(new PotHolesReported());
        incident.setRodentBaiting(new RodentBaiting());
        incident.setSanitationCodeComplaints(new SanitationCodeComplaints());
        incident.setDistrict(new District());
        incident.setTreeDebris(new TreeDebris());
        incident.setTreeTrims(new TreeTrims());
        theModel.addAttribute("incident",incident);
        return "report";
    }


    @PostMapping("/insertOrUpdate")
    public String insertOrUpdateIncident(@ModelAttribute("incident") Incident incident, Model theModel, HttpServletRequest request) {
        RegisteredUser user = (RegisteredUser) request.getSession().getAttribute("user");
        List<String> requestTypes = requestTypeService.findAllNames();
        String requestType = incident.getRequestType();

        if (requestType == null || requestTypes.stream().noneMatch(type -> type.equals(requestType))) {
            theModel.addAttribute("error","No such request type. Insertion or update aborted");
            return "/report/showInsert";
        }

        if (incidentService.findByRequestNumber(incident.getServiceRequestNumber() )!= null) {
            System.out.println("Update operation");
            //TODO : Update operation

        }

        //TODO : Insert Operation.

        Date date = new Date();
        incident.setCreationDate(new java.sql.Date(date.getTime()));

        if (requestType.equals("Abandoned Vehicle Complaint")) {
            abandonedVehicleService.saveVehicle(incident.getAbandonedVehicle());
        }
        else if (requestType.equals("Alley Light Out")) {
            alleyLightsService.saveAlleyLights(incident.getAlleyLightsOut());
        }
        else if (requestType.equals("Garbage Cart Black Maintenance/Replacement")) {
            garbageCartsService.saveGarbageCarts(incident.getGarbageCarts());
        }

        else if (requestType.equals("Graffiti Removal")) {
            graffityRemovalService.saveGraffityRemoval(incident.getGraffitiRemoval());
        }

        else if (requestType.equals("Pothole in Street")) {
            potHolesService.savePotHoles(incident.getPotHolesReported());
        }

        else if (requestType.equals("Rodent Baiting/Rat Complaint")) {
            rodentBaitingService.saveRodentBaiting(incident.getRodentBaiting());
        }

        else if (requestType.equals("Sanitation Code Violation")) {
            sanitationComplaintService.saveSanitationComplaint(incident.getSanitationCodeComplaints());
        }

        else if (requestType.equals("Tree Trim")) {
            treeTrimService.saveTreeTrim(incident.getTreeTrims());
        }

        else if (requestType.equals("Tree Debris")) {
            treeDebrisService.saveTreeDebris(incident.getTreeDebris());
        }

        else if (requestType.equals("Street Lights - All/Out")) {
            alleyLightsService.saveAlleyLights(incident.getAlleyLightsOut());
        }

        else if (requestType.equals("Street Light Out")) {
            alleyLightsService.saveAlleyLights(incident.getAlleyLightsOut());
        }

        else if (requestType.equals("Street Lights - 1/Out")) {
            alleyLightsService.saveAlleyLights(incident.getAlleyLightsOut());
        }

        incidentService.saveIncident(incident);

        return "redirect:/report/showInsert";

    }

}
