// Ryan Berger, BU ID: U41933866

// Ex 1
function replaceTarget()
{
	document.getElementById("target").innerHTML = "I have changed the HTML from " + document.getElementById("target").innerHTML + " to: Hi there!";
}

// Ex 2
function checkValue(element)
{
    // First test using IE notation, then FireFox since the two browsers accept different implementations
	if (element.parentNode.firstChild.value == "Cool!" || element.previousElementSibling.value == "Cool!")
	{
		var divTag = document.createElement("div");
		divTag.innerHTML = "Thanks, I know...";
		
		element.parentNode.appendChild(divTag);
	}
}

// Ex 3
function generateFavoriteCarList(element)
{
	// First clear out element so that all of its contents can be replaced
	element.innerHTML = "";
	var ul = document.createElement("ul");
	var carList= element.appendChild(ul);
	carList.appendChild(createCarLi("Jeep Grand Cherokee", "Silver"));
	carList.appendChild(createCarLi("Ford Mustang", "Orange"));
	carList.appendChild(createCarLi("Porsche", "Red"));
}

function createCarLi(displayText, color)
{
    var carLi = document.createElement("li");
    
    /* For Ex 4: I could't get the onclick attribute to work properly in my browser.
       When inspecting the generated element, I could see that the proper attribute 
       had been created, but it didn't respond properly to being clicked. I
       checked around online and saw that this particular function can be
       problematic when getting created "on-the-fly", but none of the proposed
       work-arounds ended up working for me. */
       
    //carLi.onclick = function() {generateFavoriteColor(carLi, color);};
    carLi.setAttribute("onclick", "generateFavoriteColor(carLi, color);"); 
    carLi.innerHTML = displayText;
    return carLi;
}

// Ex 4
function generateFavoriteColor(element, color)
{
	var spanTag = document.createElement("span");
	spanTag.innerHTML = " Favorite Color: " + color;
	
	element.appendChild(spanTag);
}