<?php session_start(); require 'templates/approve.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>Edit Account</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
		<script type="text/javascript" src="javascripts/validations.js"></script>
		<script type="text/javascript">
			function confirmDeleteAccount()
			{
				return confirm("Are you sure you really want to delete this account?");
			}
		</script>
    </head>
    <body>
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
				<?php
					$oldUsername = $_GET['username'];
					
					$query = "SELECT password FROM user_info WHERE username = '$oldUsername'";
					$result = $mysqli->query($query);
					list($oldPassword) = $result->fetch_row();
					
					echo '<form name="userInfo" method="post" action="" onsubmit="return validateAccount();">
								<table>
								<tr>
									<td>Username: </td>
									<td><input type="text" name="newUsername" id="newUsername" class="textfield" value="'.$oldUsername.'" /></td>
								</tr>
								<tr>
									<td>Password: </td>
									<td><input type="password" name="newPassword" id="newPassword" class="textfield" value="'.$oldPassword.'" /></td>
								</tr>
								<tr>
									<td>Confirm Password: </td>
									<td><input type="password" name="confirmPassword" id="confirmPassword" class="textfield" value="'.$oldPassword.'" /></td>
								</tr>
								</table>
								<input type="button" name="cancel" value="Cancel" onclick="location.href=\'manageAccounts.php\';" />
								<input type="submit" name="updateAccount" id="updateAccount" value="Update Account" />
							</form>
							<form name="delete" method="post" action="" onsubmit="return confirmDeleteAccount();">
								<input type="submit" name="deleteAccount" id="deleteAccount" value="Delete Account" />
							</form>
							<br />';
					
					if (isset($_POST['updateAccount']))
					{
						$newUsername = $_POST['newUsername'];
						$newPassword = $_POST['newPassword'];
						$update = "UPDATE user_info SET username = '$newUsername', password = '$newPassword' WHERE username = '$oldUsername'";
						$mysqli->query($update);
						
						if ($mysqli->errno == 1062)
						{
							printf('User %s already exists. Please choose a different one.', $username);
						}
						else
						{
							printf('User %s successfully updated! Click <a href="index.php">here </a>to go back.', $username);
						}
						
					}
					
					if (isset($_POST['deleteAccount']))
					{
						$delete = "DELETE FROM user_info WHERE username = '$oldUsername'";
						$mysqli->query($delete);
						
						if ($mysqli->errno)
						{
							printf('Error occured when trying to delete account. Please try again.');
						}
						else
						{
							printf('User %s successfully deleted! Click <a href="index.php">here</a> to go back.', $username);
						}
						
					}
				?>
            </div>
        </div>
    </body>
</html>