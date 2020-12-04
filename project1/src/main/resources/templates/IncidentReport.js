$(function () {
    $("#request_type").change(function () {
        if ($(this).val() === "Other") {
            $("#ssa").hide();
            $("#currentActivity").hide();
            $("#daysParked").hide();
            $("#licensePlate").hide();
            $("#mostRecentAction").hide();
            $("#color").hide();
            $("#make_model").hide();
            $("#delivered_black_carts_num").hide();
            $("#graffiti_location").hide();
            $("#surface_type").hide();
            $("#filled_pot_holes").hide();
            $("#baited_premises").hide();
            $("#garbage_premises_num").hide();
            $("#rat_premises_num").hide();
            $("#violation_nature").hide();
            $("#debris_location").hide();
            $("#tree_location").hide();
        } else {
            if ($(this).val() === "1") { /*Pothole in Street*/
                $("#ssa").show();
                $("#filled_pot_holes").show();
                $("#currentActivity").show();
                $("#daysParked").hide();
                $("#licensePlate").hide();
                $("#mostRecentAction").show();
                $("#color").hide();
                $("#make_model").hide();
                $("#delivered_black_carts_num").hide();
                $("#graffiti_location").hide();
                $("#surface_type").hide();
                $("#baited_premises").hide();
                $("#garbage_premises_num").hide();
                $("#rat_premises_num").hide();
                $("#violation_nature").hide();
                $("#debris_location").hide();
                $("#tree_location").hide();
            }else if ($(this).val() === "2") { /*Tree Debris*/
                $("#debris_location").show();
                $("#tree_location").hide();
                $("#ssa").hide();
                $("#currentActivity").hide();
                $("#daysParked").hide();
                $("#licensePlate").hide();
                $("#mostRecentAction").hide();
                $("#color").hide();
                $("#make_model").hide();
                $("#delivered_black_carts_num").hide();
                $("#graffiti_location").hide();
                $("#surface_type").hide();
                $("#filled_pot_holes").hide();
                $("#baited_premises").hide();
                $("#garbage_premises_num").hide();
                $("#rat_premises_num").hide();
                $("#violation_nature").hide();
            }else if ($(this).val() === "3") { /*Tree Trim*/
                $("#tree_location").show();
                $("#debris_location").hide();
                $("#ssa").hide();
                $("#graffiti_location").hide();
                $("#surface_type").hide();
                $("#currentActivity").hide();
                $("#daysParked").hide();
                $("#licensePlate").hide();
                $("#mostRecentAction").hide();
                $("#color").hide();
                $("#make_model").hide();
                $("#delivered_black_carts_num").hide();
                $("#filled_pot_holes").hide();
                $("#baited_premises").hide();
                $("#garbage_premises_num").hide();
                $("#rat_premises_num").hide();
                $("#violation_nature").hide();
            }else if ($(this).val() === "4") { /*Graffiti Removal*/
                $("#ssa").show();
                $("#graffiti_location").show();
                $("#surface_type").show();
                $("#currentActivity").hide();
                $("#daysParked").hide();
                $("#licensePlate").hide();
                $("#mostRecentAction").hide();
                $("#color").hide();
                $("#make_model").hide();
                $("#delivered_black_carts_num").hide();
                $("#filled_pot_holes").hide();
                $("#baited_premises").hide();
                $("#garbage_premises_num").hide();
                $("#rat_premises_num").hide();
                $("#violation_nature").hide();
                $("#debris_location").hide();
                $("#tree_location").hide();
            }else if ($(this).val() === "5") { /*Street Light - 1/Out*/
                $("#ssa").hide();
                $("#currentActivity").hide();
                $("#daysParked").hide();
                $("#licensePlate").hide();
                $("#mostRecentAction").hide();
                $("#color").hide();
                $("#make_model").hide();
                $("#delivered_black_carts_num").hide();
                $("#graffiti_location").hide();
                $("#surface_type").hide();
                $("#filled_pot_holes").hide();
                $("#baited_premises").hide();
                $("#garbage_premises_num").hide();
                $("#rat_premises_num").hide();
                $("#violation_nature").hide();
                $("#debris_location").hide();
                $("#tree_location").hide();
            }else if ($(this).val() === "6") { /*Street Lights - All/Out*/
                $("#ssa").hide();
                $("#currentActivity").hide();
                $("#daysParked").hide();
                $("#licensePlate").hide();
                $("#mostRecentAction").hide();
                $("#color").hide();
                $("#make_model").hide();
                $("#delivered_black_carts_num").hide();
                $("#graffiti_location").hide();
                $("#surface_type").hide();
                $("#filled_pot_holes").hide();
                $("#baited_premises").hide();
                $("#garbage_premises_num").hide();
                $("#rat_premises_num").hide();
                $("#violation_nature").hide();
                $("#debris_location").hide();
                $("#tree_location").hide();
            }else if ($(this).val() === "7") { /*Street Light Out*/
                $("#ssa").hide();
                $("#currentActivity").hide();
                $("#daysParked").hide();
                $("#licensePlate").hide();
                $("#mostRecentAction").hide();
                $("#color").hide();
                $("#make_model").hide();
                $("#delivered_black_carts_num").hide();
                $("#graffiti_location").hide();
                $("#surface_type").hide();
                $("#filled_pot_holes").hide();
                $("#baited_premises").hide();
                $("#garbage_premises_num").hide();
                $("#rat_premises_num").hide();
                $("#violation_nature").hide();
                $("#debris_location").hide();
                $("#tree_location").hide();
            }else if ($(this).val() === "8") { /*Rodent Baiting/Rat Complaint*/
                $("#ssa").hide();
                $("#baited_premises").show();
                $("#garbage_premises_num").show();
                $("#rat_premises_num").show();
                $("#currentActivity").show();
                $("#daysParked").hide();
                $("#licensePlate").hide();
                $("#mostRecentAction").show();
                $("#color").hide();
                $("#make_model").hide();
                $("#delivered_black_carts_num").hide();
                $("#graffiti_location").hide();
                $("#surface_type").hide();
                $("#filled_pot_holes").hide();
                $("#violation_nature").hide();
                $("#debris_location").hide();
                $("#tree_location").hide();
            }else if ($(this).val() === "9") { /*Sanitation Code Violation*/
                $("#violation_nature").show();
                $("#ssa").hide();
                $("#currentActivity").hide();
                $("#daysParked").hide();
                $("#licensePlate").hide();
                $("#mostRecentAction").hide();
                $("#color").hide();
                $("#make_model").hide();
                $("#delivered_black_carts_num").hide();
                $("#graffiti_location").hide();
                $("#surface_type").hide();
                $("#filled_pot_holes").hide();
                $("#baited_premises").hide();
                $("#garbage_premises_num").hide();
                $("#rat_premises_num").hide();
                $("#debris_location").hide();
                $("#tree_location").hide();
            }else if ($(this).val() === "10") { /*Garbage Cart Black Maintenance/Replacement*/
                $("#ssa").show();
                $("#currentActivity").show();
                $("#delivered_black_carts_num").show();
                $("#daysParked").hide();
                $("#licensePlate").hide();
                $("#mostRecentAction").show();
                $("#color").hide();
                $("#make_model").hide();
                $("#graffiti_location").hide();
                $("#surface_type").hide();
                $("#filled_pot_holes").hide();
                $("#baited_premises").hide();
                $("#garbage_premises_num").hide();
                $("#rat_premises_num").hide();
                $("#violation_nature").hide();
                $("#debris_location").hide();
                $("#tree_location").hide();
            }else if ($(this).val() === "11") { /*Abandoned Vehicle Complaint*/
                $("#ssa").show();
                $("#currentActivity").show();
                $("#daysParked").show();
                $("#licensePlate").show();
                $("#mostRecentAction").show();
                $("#color").show();
                $("#make_model").show();
                $("#delivered_black_carts_num").hide();
                $("#graffiti_location").hide();
                $("#surface_type").hide();
                $("#filled_pot_holes").hide();
                $("#baited_premises").hide();
                $("#garbage_premises_num").hide();
                $("#rat_premises_num").hide();
                $("#violation_nature").hide();
                $("#debris_location").hide();
                $("#tree_location").hide();
            }
        }
    });
});
