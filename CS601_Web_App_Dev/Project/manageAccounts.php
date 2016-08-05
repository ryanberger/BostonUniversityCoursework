<?php session_start(); require 'templates/approve.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>Manage Accounts</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
    </head>
    <body>
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
                <?php
					$query = "SELECT * FROM user_info ORDER BY username";
					$result = $mysqli->query($query);
					
					printf('<table><tr><th>Accounts</th></tr>');
					while (list($username) = $result->fetch_row())
					{
						printf('<tr><td>%s</td><td><input type="button" name="editAccount" value="Edit" onclick="location.href=\'editAccount.php?username=%s\';" /></td></tr>', $username, $username);
					}
					printf('</table>');
				?>
				<br />
				<input type="button" name="addAccount" value="Add New Account" onclick="location.href='addAccount.php';" />
            </div>
        </div>
    </body>
</html>