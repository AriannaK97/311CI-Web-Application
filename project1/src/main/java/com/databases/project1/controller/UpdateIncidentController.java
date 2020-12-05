package com.databases.project1.controller;


import com.databases.project1.entity.*;
import com.databases.project1.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("/update")
    public String showUpdate(Model theModel){
        theModel.addAttribute("updateIncident",new Incident());
        theModel.addAttribute("abandonedVehicle", new AbandonedVehicle());
        theModel.addAttribute("alleyLightsOut", new AlleyLightsOut());
        theModel.addAttribute("garbageCarts", new GarbageCarts());
        theModel.addAttribute("potHolesReported", new PotHolesReported());
        theModel.addAttribute("rodentBaiting", new RodentBaiting());
        theModel.addAttribute("sanitationCodeComplaints", new SanitationCodeComplaints());
        theModel.addAttribute("district", new District());
        theModel.addAttribute("treeDebris", new TreeDebris());
        theModel.addAttribute("treeTrims", new TreeTrims());
        return "updateIncident";
    }

    @PostMapping("/searchUpdate")
    public String UpdateIncident(@ModelAttribute("updateIncident") Incident incident,
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

        if (incidentService.findByRequestNumber(incident.getServiceRequestNumber() )!= null) {
            System.out.println("Update operation");
            String requestType =

            //TODO : Update operation

        }
        return "redirect:/update";
    }


}
