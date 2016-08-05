<?php session_start(); require 'templates/approve.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>Edit Vendor</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
		<script type="text/javascript" src="javascripts/validations.js"></script>
		<script type="text/javascript">
			function confirmDeleteVendor()
			{
				return confirm("Are you sure you really want to delete this vendor?");
			}
		</script>
    </head>
    <body>
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
				<?php
					$vendorId = $_GET['id'];
					
					$query = "SELECT vendor_name, address, phone, email FROM vendor_info WHERE vendor_id = '$vendorId'";
					$result = $mysqli->query($query);
					list($vendorName, $address, $phone, $email) = $result->fetch_row();
					
					echo '<form name="vendorInfo" method="post" action="" onsubmit="return validateVendor();">
								<table>
								<tr>
									<td>Vendor Name: </td>
									<td><input type="text" name="vendorName" id="vendorName" value="'.$vendorName.'" /></td>
								</tr>
								<tr>
									<td>Address: </td>
									<td><input type="text" name="address" id="address" value="'.$address.'" /></td>
								</tr>
								<tr>
									<td>Phone: </td>
									<td><input type="text" name="phone" id="phone" value="'.$phone.'" /></td>
								</tr>
								<tr>
									<td>Email: </td>
									<td><input type="text" name="email" id="email" value="'.$email.'" /></td>
								</tr>
								</table>
									<input type="button" name="cancel" value="Cancel" onclick="location.href=\'vendorInfo.php?id='.$vendorId.'\';" />
									<input type="submit" name="updateVendor" id="updateVendor" value="Update Vendor" />
							</form>
							<form name="delete" method="post" action="" onsubmit="return confirmDeleteVendor();">
								<input type="submit" name="deleteVendor" id="deleteVendor" value="Delete Vendor" />
							</form>
							<br />';
					
					if (isset($_POST['updateVendor']))
					{
						$vendorName = $_POST['vendorName'];
						$address = $_POST['address'];
						$phone = $_POST['phone'];
						$email = $_POST['email'];
						$update = "UPDATE vendor_info SET vendor_name = '$vendorName', address = '$address', phone = '$phone', email = '$email' WHERE vendor_id = '$vendorId'";
						$mysqli->query($update);
						
						if ($mysqli->errno)
						{
							printf('Unknown error. Please try again.');
						}
						else
						{
							printf('%s was successfully updated! Click <a href="vendorInfo.php?id=%s">here </a>to go back.', $vendorName, $vendorId);
						}
						
					}
					
					if (isset($_POST['deleteVendor']))
					{
						$deleteMain = "DELETE FROM vendor_info WHERE vendor_id = '$vendorId'";
						$mysqli->query($deleteMain);
						
						$deleteVendorProd = "DELETE FROM vendor_prod WHERE vendor_id = '$vendorId'";
						$mysqli->query($deleteVendorProd);
						
						if ($mysqli->errno)
						{
							printf('Error occured when trying to delete product. Please try again.');
						}
						else
						{
							printf('%s successfully deleted! Click <a href="index.php">here</a> to go back.', $oldProdCode);
						}
						
					}
				?>
            </div>
        </div>
    </body>
</html>