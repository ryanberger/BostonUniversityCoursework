<?php session_start(); require 'templates/approve.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>New Product</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
		<script type="text/javascript" src="javascripts/validations.js"></script>
    </head>
    <body>
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
				<form name="productInfo" method="post" action="" onsubmit="return validateProduct();">
					<table>
					<tr>
						<td>Product Code: </td>
						<td><input type="text" name="prodCode" id="prodCode" class="textfield" /></td>
					</tr>
					<tr>
						<td>Product Long Name: </td>
						<td><input type="text" name="prodName" id="prodName" class="textfield" /></td>
					</tr>
					<tr>
						<td>Date Range: </td>
						<td><input type="text" name="dateRange" id="dateRange" class="textfield" /></td>
					</tr>
					<tr>
						<td>Associated Vendor(s): </td>
						<td><select name="chooseVendors[]" multiple="multiple" size="5">
							<?php 
								$query = "SELECT vendor_id, vendor_name FROM vendor_info ORDER BY vendor_name";
								$result = $mysqli->query($query);
								while (list($vendorId, $vendorName) = $result->fetch_row())
								{
									printf('<option value="%s">%s</option>', $vendorId, $vendorName);
								}
							?>
							</select>
						</td>
					</tr>
					<tr>
						<td>Associated Feed(s): </td>
						<td><select name="chooseFeeds[]" multiple="multiple" size="5">
							<?php 
								$query = "SELECT feed_id, description FROM feeds ORDER BY description";
								$result = $mysqli->query($query);
								while (list($feedId, $desc) = $result->fetch_row())
								{
									printf('<option value="%s">%s</option>', $feedId, $desc);
								}
							?>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2"><input type="button" name="cancel" value="Cancel" onclick="history.go(-1);" /><input type="submit" name="addProduct" id="addProduct" value="Add Product" /></td>
					</tr>
					</table>
				</form>
				<?php
					if (isset($_POST['addProduct']))
					{
						$prodCode = $_POST['prodCode'];
						$prodName = $_POST['prodName'];
						$dateRange = $_POST['dateRange'];
						$vendors = $_POST['chooseVendors'];
						$feeds = $_POST['chooseFeeds'];
						
						$mainInsert = "INSERT INTO prod_info (prodcode, prodname, date_range) VALUES ('$prodCode', '$prodName', '$dateRange')";
						$mysqli->query($mainInsert);
						
						if ($vendors)
						{
							foreach ($vendors as $vendorId)
							{
								$VendorInsert = "INSERT INTO vendor_prod (vendor_id, prodcode) VALUES ('$vendorId', '$prodCode')";
								$mysqli->query($VendorInsert);
							}
						}
						if ($feeds)
						{
							foreach ($feeds as $feedId)
							{
								$VendorInsert = "INSERT INTO feed_prod (feed_id, prodcode) VALUES ('$feedId', '$prodCode')";
								$mysqli->query($VendorInsert);
							}
						}
						
						// Finally, check for any errors
						if ($mysqli->errno)
						{
							printf('Unknown error. Please try again.');
						}
						else
						{
							printf('%s was successfully added! Click <a href="#" onclick="history.go(-1);">here </a>to go back.', $prodCode);
						}
						
					}
				?>
            </div>
        </div>
    </body>
</html>