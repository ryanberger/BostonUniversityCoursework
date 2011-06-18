<?php session_start(); require 'templates/approve.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>Edit Feed</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
		<script type="text/javascript" src="javascripts/validations.js"></script>
		<script type="text/javascript">
			function confirmDeleteFeed()
			{
				return confirm("Are you sure you really want to delete this feed?");
			}
		</script>
    </head>
    <body>
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
			<?php
				$oldVendorId = $_GET['vId'];
				$feedId = $_GET['fId'];
				
				$query = "SELECT description, delivery_type, delivery_freq, build_freq, approx_size, filename_pattern FROM feeds WHERE feed_id=$feedId";
				$result = $mysqli->query($query);
				list($oldDesc, $oldDeliveryType, $oldDeliveryFreq, $oldBuildFreq, $oldApproxSize, $oldFilenamePattern) = $result->fetch_row();
				
				echo '<form name="feedInfo" method="post" action="" onsubmit="return validateFeed();">
					<table>
					<tr>
						<td>Description: </td>
						<td><input type="text" name="desc" id="desc" class="textfield" value="'.$oldDesc.'" /></td>
					</tr>
					<tr>
						<td>Vendor: </td>
						<td><select id="chooseVendor" name="chooseVendor">'; 
							$query = "SELECT vendor_id, vendor_name FROM vendor_info ORDER BY vendor_name";
							$result = $mysqli->query($query);
							while (list($vendorId, $vendorName) = $result->fetch_row())
							{
								printf('<option value="%s"', $vendorId);
								if ($oldVendorId == $vendorId)
								{
									printf(' selected="selected"');
								}
								printf('>%s</option>', $vendorName);
							}
					echo '</select></td>
					</tr>
					<tr>
						<td>Delivery Type: </td>
						<td><input type="text" name="deliveryType" id="deliveryType" class="textfield" value="'.$oldDeliveryType.'" /></td>
					</tr>
					<tr>
						<td>Delivery Frequency: </td>
						<td><input type="text" name="deliveryFreq" id="deliveryFreq" class="textfield" value="'.$oldDeliveryFreq.'" /></td>
					</tr>
					<tr>
						<td>Build Frequency: </td>
						<td><input type="text" name="buildFreq" id="buildFreq" class="textfield" value="'.$oldBuildFreq.'" /></td>
					</tr>
					<tr>
						<td>Approximate Size: </td>
						<td><input type="text" name="approxSize" id="approxSize" class="textfield" value="'.$oldApproxSize.'" /></td>
					</tr>
					<tr>
						<td>Filename Pattern: </td>
						<td><input type="text" name="filenamePattern" id="filenamePattern" class="textfield" value="'.$oldFilenamePattern.'" /></td>
					</tr>
					</table>
					<input type="button" name="cancel" value="Cancel" onclick="history.go(-1);" />
					<input type="submit" name="updateFeed" id="updateFeed" value="Update Feed" />					
				</form>
				<form name="delete" method="post" action="" onsubmit="return confirmDeleteFeed();">
					<input type="submit" name="deleteFeed" id="deleteFeed" value="Delete Feed" />
				</form>
				<br />';

					if (isset($_POST['updateFeed']))
					{
						$desc = $_POST['desc'];
						$vendorId = $_POST['chooseVendor'];
						$deliveryType = $_POST['deliveryType'];
						$deliveryFreq = $_POST['deliveryFreq'];
						$buildFreq = $_POST['buildFreq'];
						$approxSize = $_POST['approxSize'];
						$filenamePattern = $_POST['filenamePattern'];
						
						$update = "UPDATE feeds SET description = '$desc', vendor_id = $vendorId, delivery_type = '$deliveryType', delivery_freq = '$deliveryFreq', build_freq = '$buildFreq', approx_size = '$approxSize', filename_pattern = '$filenamePattern' WHERE feed_id = $feedId";
						$mysqli->query($update);

						if ($mysqli->errno)
						{
							printf('Unknown error. Please try again.');
						}
						else
						{
							printf('%s was successfully updated! Click <a href="index.php">here</a> to go back.', $desc);
						}
						
					}
					
					if (isset($_POST['deleteFeed']))
					{
						$delete = "DELETE FROM feeds WHERE feed_id = $feedId";
						$mysqli->query($delete);
						
						if ($mysqli->errno)
						{
							printf('Error occured when trying to delete product. Please try again.');
						}
						else
						{
							printf('%s successfully deleted! Click <a href="index.php">here</a> to go back.', $desc);
						}
						
					}
				?>
            </div>
        </div>
    </body>
</html>