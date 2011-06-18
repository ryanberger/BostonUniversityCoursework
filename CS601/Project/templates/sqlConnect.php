<?php
	$mysqli = new mysqli("localhost", "admin", "ebsco");
	$mysqli->select_db("project");
	
	if ($mysqli->errno)
	{
		printf("Unable to connect to the database:<br /> %s", $mysqli->error);
		exit();
	}
?>