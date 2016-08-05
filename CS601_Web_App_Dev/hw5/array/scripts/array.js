function change_band()
{
    var selectBox = document.getElementById("bands");
    var choice = selectBox.selectedIndex;
    
    switch (selectBox.options[choice].value)
    {
        case "iron_maiden":
            document.getElementById("image").setAttribute("src", "images/iron_maiden.jpg");
            document.getElementById("image").setAttribute("alt", "Iron Maiden");
            document.getElementById("main_text").innerHTML = "<h1>Iron Maiden</h1><h2>1987</h2><h3>Their seminal work.<br />Cold and fast - the way rock should be</h3>";
            break;
        case "dragon_force":
            document.getElementById("image").setAttribute("src", "images/dragon_force.jpg");
            document.getElementById("image").setAttribute("alt", "Dragon Force");
            document.getElementById("main_text").innerHTML = "<h1>Dragon Force</h1><h2>2004</h2><h3>Insane guitar and drums. Can you keep up?</h3>";
            break;
        case "kamelot":
            document.getElementById("image").setAttribute("src", "images/kamelot.jpg");
            document.getElementById("image").setAttribute("alt", "Kamelot");
            document.getElementById("main_text").innerHTML = "<h1>Kamelot</h1><h2>2000</h2><h3>I've never listened to this band before.<br />They might be good - who knows?</h3>";
            break;
        default:
            break;
    }
}