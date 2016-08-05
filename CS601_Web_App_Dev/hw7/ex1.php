<?php
    function fahrenheitToCelsius($tempF) {
        $tempC = round(($tempF - 32) / 1.8, 1);
		printf("The temperature you entered (%s&deg;F) is equivalent to %s&deg;C", $tempF, $tempC);    
    }
	
	function celciusToFahrenheit($tempC) {
        $tempF = round(($tempC * 1.8) + 32, 1);
		printf("The temperature you entered (%s&deg;C) is equivalent to %s&deg;F", $tempC, $tempF);    
    }
	
	$temp = $_POST['temp'];
	
	if (isset($_POST['FtoC']))
	{
		fahrenheitToCelsius($temp);
	}
	
	if (isset($_POST['CtoF']))
	{
		celciusToFahrenheit($temp);
	}
?>