<?php 
	session_start();
	include("templates/sqlConnect.php"); 
	if(isset($_POST['login']))
	{
		$username = $_POST['username'];
		$password = $_POST['password'];
		$query = "SELECT username FROM user_info WHERE username = '$username' AND password = '$password'";
		$result = $mysqli->query($query);
		
		if ( $result->num_rows > 0 ) 
		{
			$_SESSION['phplogin'] = true;
			header('Location: adminHome.php');
			exit;
		} 
		else 
		{
			printf("<div>Incorrect username/password combination. Please try again.</div>");
		}
	}
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>Home</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
    </head>
    <body>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
                <form name="login_form" method="post" action="<?php $_SERVER['PHP.SELF'] ?>">
                    <table>
					<tr><td>Username: </td><td><input type="text" name="username" /></td></tr>
					<tr><td>Password: </td><td><input type="password" name="password" /></td></tr>
					</table>
					<p><input type="submit" name="login" value="Login" /></p>
				</form>
            </div>
        </div>
    </body>
</html>