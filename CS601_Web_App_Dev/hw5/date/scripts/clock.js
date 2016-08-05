function showClock()
{
    var theTime = new Date();
    var month = theTime.getMonth();
    var day = theTime.getDate();
    var year = theTime.getFullYear();
    var hours = theTime.getHours();
    var mins = theTime.getMinutes();
    var secs = theTime.getSeconds();
    var AorP = "";

    // First, translate the month number (zero-indexed) into a more readable format
    switch (month + 1)
    {
        case 1:
            month = "January";
            break;
        case 2:
            month = "February";
            break;
        case 3:
            month = "March";
            break;
        case 4:
            month = "April";
            break;
        case 5:
            month = "May";
            break;
        case 6:
            month = "June";
            break;
        case 7:
            month = "July";
            break;
        case 8:
            month = "August";
            break;
        case 9:
            month = "September";
            break;
        case 10:
            month = "October";
            break;
        case 11:
            month = "November";
            break;
        case 12:
            month = "December";
            break;
    }

    // Determine if time (military format) is in the AM or PM
    if (hours >= 12)
    {
        AorP = "PM";
    }
    else
    {
        AorP = "AM";
    }

    // Translate hours from military format to 12-hour format
    if (hours >= 13)
    {
        hours -= 12;
    }
    else if (hours == 0)
    {
        hours = 12;
    }

    // Append a "0" to minutes or seconds that are single-digit
    if (mins < 10)
    {
        mins = "0" + mins;
    }
    if (secs < 10)
    {
        secs = "0" + secs;
    }

    document.getElementById("clock").innerHTML = month + " " + day + ", " + year + " " + hours + ":" + mins + ":" + secs + " " + AorP;
    // Run this function once every second
    setTimeout('showClock();', 1000);
}