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
