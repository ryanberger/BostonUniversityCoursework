<?php session_start(); require 'templates/approve.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>Create Account</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
		<script type="text/javascript" src="javascripts/validations.js"></script>
    </head>
    <body>
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
				<form name="userInfo" method="post" action="<?php $_SERVER['PHP.SELF'] ?>" onsubmit="return validateAccount();">
					<table>
					<tr>
						<td>Username: </td>
						<td><input type="text" name="newUsername" id="newUsername" class="textfield" /></td>
					</tr>
					<tr>
						<td>Password: </td>
						<td><input type="password" name="newPassword" id="newPassword" class="textfield" /></td>
					</tr>
					<tr>
						<td>Confirm Password: </td>
						<td><input type="password" name="confirmPassword" id="confirmPassword" class="textfield" /></td>
					</tr>
					<tr>
						<td colspan="2"><input type="button" name="cancel" value="Cancel" onclick="location.href='manageAccounts.php';" />
						<input type="submit" name="addAccount" id="addAccount" value="Create Account" /></td>
					</tr>
					</table>
				</form>
				<?php
					if (isset($_POST['addAccount']))
					{
						$username = $_POST['newUsername'];
						$password = $_POST['newPassword'];
						
						$insert = "INSERT INTO user_info (username, password) VALUES ('$username', '$password')";
						$mysqli->query($insert);
						
						if ($mysqli->errno == 1062)
						{
							printf('User %s already exists. Please choose a different one.', $username);
						}
						else
						{
							printf('User %s successfully added! Click <a href="manageAccounts.php">here </a>to go back.', $username);
						}
					}
				?>
            </div>
        </div>
    </body>
</html>