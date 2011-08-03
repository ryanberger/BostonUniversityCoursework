<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
</head>

<body>
<div>
	<form action="phplab2.php" method="post">
		<input type="text" id="age" /><input type="button" />
    </form>
    <?php 
	if ($_POST['age']) 
	{
		$age = $_POST['age'];
		echo "Dog age: " . (7 * $age);
	}
	?>
</div>
</body>
</html>