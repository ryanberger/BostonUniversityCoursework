<?php session_start(); require 'templates/approve.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>New Vendor</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
		<script type="text/javascript" src="javascripts/validations.js"></script>
    </head>
    <body>
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
				<?php
					
					echo '<form name="vendorInfo" method="post" action="" onsubmit="return validateVendor();">
								<table>
								<tr>
									<td>Vendor Name: </td>
									<td><input type="text" name="vendorName" id="vendorName" class="textfield" /></td>
								</tr>
								<tr>
									<td>Address: </td>
									<td><input type="text" name="address" id="address" class="textfield" /></td>
								</tr>
								<tr>
									<td>Phone: </td>
									<td><input type="text" name="phone" id="phone" class="textfield" /></td>
								</tr>
								<tr>
									<td>Email: </td>
									<td><input type="text" name="email" id="email" class="textfield" /></td>
								</tr>
								<tr>
									<td colspan="2"><input type="button" name="cancel" value="Cancel" onclick="history.go(-1);" /><input type="submit" name="addVendor" id="addVendor" value="Add Vendor" /></td>
								</tr>
								</table>
							</form>';
					
					if (isset($_POST['addVendor']))
					{
						$vendorName = $_POST['vendorName'];
						$address = $_POST['address'];
						$phone = $_POST['phone'];
						$email = $_POST['email'];
						$insert = "INSERT INTO vendor_info (vendor_name, address, phone, email) VALUES ('$vendorName', '$address', '$phone', '$email')";
						$mysqli->query($insert);
						
						if ($mysqli->errno)
						{
							printf('Unknown error. Please try again.');
						}
						else
						{
							printf('%s was successfully added! Click <a href="#" onclick="history.go(-1);">here </a>to go back.', $vendorName);
						}
						
					}
				?>
            </div>
        </div>
    </body>
</html>