<?php session_start(); require 'templates/approve.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>Edit Product</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
		<script type="text/javascript" src="javascripts/validations.js"></script>
		<script type="text/javascript">
			function confirmDeleteProduct()
			{
				return confirm("Are you sure you really want to delete this product?");
			}
		</script>
    </head>
    <body>
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
			<?php
				$oldProdCode = $_GET['prodcode'];
				
				$query = "SELECT prodname, date_range FROM prod_info WHERE prodcode = '$oldProdCode'";
				$result = $mysqli->query($query);
				list($oldProdName, $oldDateRange) = $result->fetch_row();
				
				echo '<form name="productInfo" method="post" action="" onsubmit="return validateProduct();">
						<table>
						<tr>
							<td>Product Code: </td>
							<td><input type="text" name="prodCode" id="prodCode" class="textfield" value="'.$oldProdCode.'" /></td>
						</tr>
						<tr>
							<td>Product Long Name: </td>
							<td><input type="text" name="prodName" id="prodName" class="textfield" value="'.$oldProdName.'" /></td>
						</tr>
						<tr>
							<td>Date Range: </td>
							<td><input type="text" name="dateRange" id="dateRange" class="textfield" value="'.$oldDateRange.'" /></td>
						</tr>
						<tr>
							<td>Associated Vendor(s): </td>
							<td><select name="chooseVendors[]" multiple="multiple" size="5">';
									$vendorQuery = "SELECT vendor_id, vendor_name FROM vendor_info ORDER BY vendor_name";
									$vendorResult = $mysqli->query($vendorQuery);
									while (list($vendorId, $vendorName) = $vendorResult->fetch_row())
									{
										$vendorProdQuery = "SELECT * FROM vendor_prod WHERE vendor_id='$vendorId' AND prodcode='$oldProdCode'";
										$vendorProdResult = $mysqli->query($vendorProdQuery);
										printf('<option value="%s"', $vendorId);
										if ($vendorProdResult->num_rows > 0)
										{
											printf(' selected="selected"');
										}
										printf('>%s</option>', $vendorName);
									}
							echo '</select>
							</td>
						</tr>
						<tr>
							<td>Associated Feed(s): </td>
							<td><select name="chooseFeeds[]" multiple="multiple" size="5">'; 
									$feedQuery = "SELECT feed_id, description FROM feeds ORDER BY description";
									$feedResult = $mysqli->query($feedQuery);
									while (list($feedId, $desc) = $feedResult->fetch_row())
									{
										$feedProdQuery = "SELECT * FROM feed_prod WHERE feed_id='$feedId' AND prodcode='$oldProdCode'";
										$feedProdResult = $mysqli->query($feedProdQuery);	
										printf('<option value="%s"', $feedId);
										if ($feedProdResult->num_rows > 0)
										{
											printf(' selected="selected"');
										}
										printf('>%s</option>', $desc);
									}
							echo '</select>
							</td>
						</tr>
						</table>
						<input type="button" name="cancel" value="Cancel" onclick="history.go(-1);" />
						<input type="submit" name="updateProduct" id="updateProduct" value="Update Product" />
					</form>
					<form name="delete" method="post" action="" onsubmit="return confirmDeleteProduct();">
						<input type="submit" name="deleteProduct" id="deleteProduct" value="Delete Product" />
					</form>
					<br />';

					if (isset($_POST['updateProduct']))
					{
						$prodCode = $_POST['prodCode'];
						$prodName = $_POST['prodName'];
						$dateRange = $_POST['dateRange'];
						$vendors = $_POST['chooseVendors'];
						$feeds = $_POST['chooseFeeds'];
						
						$mainUpdate = "UPDATE prod_info SET prodcode = '$prodCode', prodname = '$prodName', date_range = '$dateRange' WHERE prodcode = '$oldProdCode'";
						$mysqli->query($mainUpdate);
						
						// Delete all related entries from vendor_prod table before re-adding each relationship
						$vendorProdDelete = "DELETE FROM vendor_prod WHERE prodcode='$oldProdCode'";
						$mysqli->query($vendorProdDelete);
						
						if ($vendors)
						{
							foreach ($vendors as $vendorId)
							{
								$VendorProdInsert = "INSERT INTO vendor_prod (vendor_id, prodcode) VALUES ('$vendorId', '$prodCode')";
								$mysqli->query($VendorProdInsert);
							}
						}
						
						// Delete all related entries from feed_prod table before re-adding each relationship
						$feedProdDelete = "DELETE FROM feed_prod WHERE prodcode='$oldProdCode'";
						$mysqli->query($feedProdDelete);
						
						if ($feeds)
						{
							foreach ($feeds as $feedId)
							{
								$FeedProdInsert = "INSERT INTO feed_prod (feed_id, prodcode) VALUES ('$feedId', '$prodCode')";
								$mysqli->query($FeedProdInsert);
							}
						}
						
						// Finally, check for any errors
						if ($mysqli->errno)
						{
							printf('Unknown error. Please try again.');
						}
						else
						{
							printf('%s was successfully updated! Click <a href="index.php">here</a> to go back.', $oldProdCode);
						}
						
					}
					
					if (isset($_POST['deleteProduct']))
					{
						$deleteMain = "DELETE FROM prod_info WHERE prodcode = '$oldProdCode'";
						$mysqli->query($deleteMain);
						
						$deleteFeedProd = "DELETE FROM feed_prod WHERE prodcode = '$oldProdCode'";
						$mysqli->query($deleteFeedProd);
						
						$deleteVendorProd = "DELETE FROM vendor_prod WHERE prodcode = '$oldProdCode'";
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