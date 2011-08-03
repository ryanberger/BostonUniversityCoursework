function compareDates() 
{
    var date1 = document.getElementById("date1").value;
    var date2 = document.getElementById("date2").value;
    var rgxDate = /\d{1,2}\/\d{1,2}\/\d{4}/;
    
    if (!(rgxDate.test(date1)))
    {
        alert(date1 + " is not a valid date!");
    }
    else if (!(rgxDate.test(date2)))
    {
        alert(date2 + " is not a valid date!");
    }
    else
    {   
        date1 = Date.parse(date1);
        date2 = Date.parse(date2);
        
        if (date1 < date2)
        {
            document.getElementById("output").innerHTML = "Your first date is less than your second date.";
        }
        else if (date1 > date2)
        {
            document.getElementById("output").innerHTML = "Your first date is greater than your second date.";
        }
        else if (date1 == date2)
        {
            document.getElementById("output").innerHTML = "Your first date is equal to your second date.";
        }
        else
        {
            document.getElementById("output").innerHTML = "Dates could not be compared!";
        }
    }
}