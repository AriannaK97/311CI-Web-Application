$( document ).ready(function() {

    $(".next").click(function(){
        var page = $("#page").val();
        page = parseInt(page) + 1;
        $("#page").val(page);
        $("#searchButton").click();
         return false;
    });

    $(".previous").click(function(){
        var page = $("#page").val();
        if (page == 0)
            return false
        page = parseInt(page) - 1;
        $("#page").val(page);
        $("#searchButton").click();
        return false;
    });

/*    if($("#searcher.searchType").val() === "1"){
        let queryType = "Requests per type that were created within a specified time range"
    }else if($("#searcher.searchType").val() === "2"){
        let queryType = "Τotal requests per day for a specific request type and time range"
    }else if($("#searcher.searchType").val() === "3"){
        let queryType = "Μost common service request per zipcode for a specific date"
    }else if($("#searcher.searchType").val() === "4"){
        let queryType = "Average completion time per service request for a specific date range"
    }else if($("#searcher.searchType").val() === "5"){
        let queryType = "Most common service request in a specified bounding box for a specific day"
    }else if($("#searcher.searchType").val() === "6"){
        let queryType = "Top-5 Special Service Areas (SSA) with regards to total number of requests per day\n" +
            "                                    for a specific date range"
    }else if($("#searcher.searchType").val() === "7"){
        let queryType = "License plates involved in abandoned vehicle complaints more\n" +
            "                                    than once"
    }else if($("#searcher.searchType").val() === "8"){
        let queryType = "Second most common color of vehicles involved in abandoned vehicle complaints"
    }else if($("#searcher.searchType").val() === "9"){
        let queryType = "Rodent baiting requests where the number of premises baited is less than a specified\n" +
            "                                    number"
    }else if($("#searcher.searchType").val() === "10"){
        let queryType = "Rodent baiting requests where the number of premises with garbage is less than a specified\n" +
            "                                    number"
    }else if($("#searcher.searchType").val() === "11"){
        let queryType = "Rodent baiting requests where the number of premises with rats is less than a specified\n" +
            "                                    number"
    }else if($("#searcher.searchType").val() === "12"){
        let queryType = "Police districts that have handled “pot holes” requests with more than one number\n" +
            "                                    of potholes on the same day that they also handled “rodent baiting” requests with more than\n" +
            "                                    one number of premises baited, for a specific day"
    }else if($("#searcher.searchType").val() === "13"){
        let queryType = "Find Incidents by zip code"
    }else if($("#searcher.searchType").val() === "14"){
        let queryType = "Find Incidents by street address"
    }else if($("#searcher.searchType").val() === "15"){
        let queryType = "Find incidents by street address and zip code"
    }*/


    if (parseInt($("#page").val()) > 0)
        $(".previous").show();
    else
        $(".previous").hide();


    $("#exampleFormControlSelect1").change(function () {
        if ($(this).val() === "Other") {
            $("#requestType").hide();
            $("#firstDate").hide();
            $("#secondDate").hide();
            $("#streetAddress").hide();
            $("#premisesBaited").hide();
            $("#zipcode").hide();
            $("#firstLatitude").hide();
            $("#firstLongitude").hide();
            $("#secondLatitude").hide();
            $("#secondLongitude").hide();
            $("#garbagePremises").hide();
            $("#ratPremises").hide();
            $("#potholes").hide();
        } else {
            if ($(this).val() === "1") {
                $("#showRow").show();
                $("#firstDate").show();
                $("#secondDate").show();
                $("#requestType").hide();
                $("#streetAddress").hide();
                $("#premisesBaited").hide();
                $("#zipcode").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            }else if ($(this).val() === "2") {
                $("#showRow").show();
                $("#requestType").show();
                $("#firstDate").show();
                $("#secondDate").show();
                $("#streetAddress").hide();
                $("#premisesBaited").hide();
                $("#zipcode").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            } else if ($(this).val() === "3") {
                $("#showRow").show();
                $("#firstDate").show();
                $("#requestType").hide();
                $("#secondDate").hide();
                $("#streetAddress").hide();
                $("#premisesBaited").hide();
                $("#zipcode").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            } else if ($(this).val() === "4") {
                $("#showRow").show();
                $("#firstDate").show();
                $("#secondDate").show();
                $("#requestType").hide();
                $("#streetAddress").hide();
                $("#premisesBaited").hide();
                $("#zipcode").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            } else if ($(this).val() === "5") {
                $("#showRow").show();
                $("#firstDate").show();
                $("#firstLatitude").show();
                $("#firstLongitude").show();
                $("#secondLatitude").show();
                $("#secondLongitude").show();
                $("#requestType").hide();
                $("#secondDate").hide();
                $("#streetAddress").hide();
                $("#premisesBaited").hide();
                $("#zipcode").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            } else if ($(this).val() === "6") {
                $("#showRow").show();
                $("#firstDate").show();
                $("#secondDate").show();
                $("#requestType").hide();
                $("#streetAddress").hide();
                $("#premisesBaited").hide();
                $("#zipcode").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            } else if ($(this).val() === "7" || $(this).val() === "8") {
                $("#showRow").show();
                $("#requestType").hide();
                $("#firstDate").hide();
                $("#secondDate").hide();
                $("#streetAddress").hide();
                $("#premisesBaited").hide();
                $("#zipcode").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            } else if ($(this).val() === "9") {
                $("#showRow").show();
                $("#premisesBaited").show();
                $("#requestType").hide();
                $("#firstDate").hide();
                $("#secondDate").hide();
                $("#streetAddress").hide();
                $("#zipcode").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            } else if ($(this).val() === "10") {
                $("#showRow").show();
                $("#garbagePremises").show();
                $("#requestType").hide();
                $("#firstDate").hide();
                $("#secondDate").hide();
                $("#streetAddress").hide();
                $("#premisesBaited").hide();
                $("#zipcode").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            } else if ($(this).val() === "11") {
                $("#showRow").show();
                $("#ratPremises").show();
                $("#requestType").hide();
                $("#firstDate").hide();
                $("#secondDate").hide();
                $("#streetAddress").hide();
                $("#premisesBaited").hide();
                $("#zipcode").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#potholes").hide();
            } else if ($(this).val() === "12") {
                $("#showRow").show();
                $("#firstDate").show();
                $("#requestType").hide();
                $("#secondDate").hide();
                $("#streetAddress").hide();
                $("#premisesBaited").show();
                $("#zipcode").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").show();
            } else if ($(this).val() === "13") {
                $("#showRow").show();
                $("#zipcode").show();
                $("#requestType").hide();
                $("#firstDate").hide();
                $("#secondDate").hide();
                $("#streetAddress").hide();
                $("#premisesBaited").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            }else if ($(this).val() === "14") {
                $("#showRow").show();
                $("#streetAddress").show();
                $("#requestType").hide();
                $("#firstDate").hide();
                $("#secondDate").hide();
                $("#premisesBaited").hide();
                $("#zipcode").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            } else if ($(this).val() === "15") {
                $("#showRow").show();
                $("#streetAddress").show();
                $("#zipcode").show();
                $("#requestType").hide();
                $("#firstDate").hide();
                $("#secondDate").hide();
                $("#premisesBaited").hide();
                $("#firstLatitude").hide();
                $("#firstLongitude").hide();
                $("#secondLatitude").hide();
                $("#secondLongitude").hide();
                $("#garbagePremises").hide();
                $("#ratPremises").hide();
                $("#potholes").hide();
            }
        }
    });
});
