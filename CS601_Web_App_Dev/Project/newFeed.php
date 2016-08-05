<?php session_start(); require 'templates/approve.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>New Feed</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
		<script type="text/javascript" src="javascripts/validations.js"></script>
    </head>
    <body>
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
				<form name="feedInfo" method="post" action="<?php $_SERVER['PHP.SELF'] ?>" onsubmit="return validateFeed();">
					<table>
					<tr>
						<td>Description: </td>
						<td><input type="text" name="desc" id="desc" class="textfield" /></td>
					</tr>
					<tr>
						<td>Vendor: </td>
						<td><select id="chooseVendor" name="chooseVendor">
						<?php 
							$query = "SELECT vendor_id, vendor_name FROM vendor_info ORDER BY vendor_name";
							$result = $mysqli->query($query);
							while (list($vendorId, $vendorName) = $result->fetch_row())
							{
								printf('<option value="%s">%s</option>', $vendorId, $vendorName);
							}
						?>
						</select></td>
					</tr>
					<tr>
						<td>Delivery Type: </td>
						<td><input type="text" name="deliveryType" id="deliveryType" class="textfield" /></td>
					</tr>
					<tr>
						<td>Delivery Frequency: </td>
						<td><input type="text" name="deliveryFreq" id="deliveryFreq" class="textfield" /></td>
					</tr>
					<tr>
						<td>Build Frequency: </td>
						<td><input type="text" name="buildFreq" id="buildFreq" class="textfield" /></td>
					</tr>
					<tr>
						<td>Approximate Size: </td>
						<td><input type="text" name="approxSize" id="approxSize" class="textfield" /></td>
					</tr>
					<tr>
						<td>Filename Pattern: </td>
						<td><input type="text" name="filenamePattern" id="filenamePattern" class="textfield" /></td>
					</tr>
					<tr>
						<td colspan="2"><input type="button" name="cancel" value="Cancel" onclick="history.go(-1);" />
						<input type="submit" name="addFeed" id="addFeed" value="Create Feed" /></td>
					</tr>
					</table>
				</form>
				<?php
					if (isset($_POST['addFeed']))
					{
						$vendorId = $_POST['chooseVendor'];
						$deliveryType = $_POST['deliveryType'];
						$deliveryFreq = $_POST['deliveryFreq'];
						$buildFreq = $_POST['buildFreq'];
						$approxSize = $_POST['approxSize'];
						$filenamePattern = mysqli_real_escape_string($mysqli, $_POST['filenamePattern']);
						$desc = mysqli_real_escape_string($mysqli, $_POST['desc']);
						
						$insert = "INSERT INTO feeds (vendor_id, delivery_type, delivery_freq, build_freq, approx_size, filename_pattern, description) 
								VALUES ('$vendorId', '$deliveryType', '$deliveryFreq', '$buildFreq', '$approxSize', '$filenamePattern', '$desc')";
						$mysqli->query($insert);
						
						if ($mysqli->errno)
						{
							printf('Error occured when trying to create feed. Please try again.');
						}
						else
						{
							printf('Feed successfully created!');
						}
					}
				?>
            </div>
        </div>
    </body>
</html>