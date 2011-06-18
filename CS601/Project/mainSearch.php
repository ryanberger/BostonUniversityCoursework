<?php 
	session_start(); 
	if (isset($_POST['search']))
	{
		$searchTerm = $_POST['searchTerm'];
		$searchOption = $_POST['searchOption'];
		
		$_SESSION['searchTerm'] = $searchTerm;
		$_SESSION['searchOption'] = $searchOption;
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
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
                <?php
					switch ($searchOption)
					{
						case 'prodcode':
							$query = "SELECT * FROM prod_info WHERE prodcode LIKE '%$searchTerm%'";
							$result = $mysqli->query($query);
							if ( $result->num_rows > 0 )
							{
								printf('<table class="formatted"><tr><th>Product Code</th><th>Product Name</th><th>Approximate Size</th><th>Date Range</th></tr>');
								while (list($prodCode, $prodName, $dateRange) = $result->fetch_row())
								{
									$sizeQuery = "SELECT SUM(approx_size) FROM feeds f, feed_prod p WHERE p.prodcode = '$prodCode' AND f.feed_id = p.feed_id";
									$sizeResult = $mysqli->query($sizeQuery);
									list($approxSize) = $sizeResult->fetch_row();
									
									printf('<tr><td><a href="prodInfo.php?prodcode=%s">%s</a></td><td>%s</td><td>%s</td><td>%s</td>', $prodCode, $prodCode, $prodName, $approxSize, $dateRange);
									if (isset($_SESSION['phplogin'])
										|| $_SESSION['phplogin'] == true) {
											printf('<td><input type="button" value="Edit" onclick="location.href=\'editProduct.php?prodcode='.$prodCode.'\';" /></td>');
									}
									printf('</tr>');
								}
								printf('</table>');
							}
							break;
						case 'vendor_name':
							$query = "SELECT * FROM vendor_info WHERE vendor_name LIKE '%$searchTerm%'";
							$result = $mysqli->query($query);
							if ( $result->num_rows > 0 )
							{
								printf('<table class="formatted"><tr><th>Vendor Name</th><th>Address</th><th>Phone</th><th>Email</th></tr>');
								while (list($vendorId, $vendorName, $address, $phone, $email) = $result->fetch_row())
								{
									printf('<tr><td><a href="vendorInfo.php?id=%s">%s</a></td><td>%s</td><td>%s</td><td>%s</td>', $vendorId, $vendorName, $address, $phone, $email);
									if (isset($_SESSION['phplogin'])
										|| $_SESSION['phplogin'] == true) {
										printf('<td><input type="button" value="Edit" onclick="location.href=\'editVendor.php?id='.$vendorId.'\';" /></td>');
									}
									printf('</tr>');
								}
								printf('</table>');
							}
							break;
						case 'feed_name':
							$query = "SELECT * FROM feeds WHERE description LIKE '%$searchTerm%'";
							$result = $mysqli->query($query);
							if ( $result->num_rows > 0 )
							{
								printf('<table class="formatted"><tr><th>Description</th><th>Delivery Type</th><th>Delivery Frequency</th><th>Build Frequency</th><th>Approximate Size</th><th>Filename Pattern</th></tr>');
								while (list($feedId, $vendorId, $desc, $deliveryType, $deliveryFreq, $buildFreq, $approxSize, $filenamePattern) = $result->fetch_row())
								{
									printf('<tr><td><a href="feedInfo.php?id=%s&desc=%s">%s</a></td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td>', $feedId, $desc, $desc, $deliveryType, $deliveryFreq, $buildFreq, $approxSize, $filenamePattern);
									if (isset($_SESSION['phplogin'])
										|| $_SESSION['phplogin'] == true) {
										printf('<td><input type="button" value="Edit" onclick="location.href=\'editFeed.php?vId='.$vendorId.'&fId='.$feedId.'\';" /></td>');
									}
									printf('</tr>');
								}
								printf('</table>');
							}
							break;
						default:
							break;
					}
					
					if ( $result->num_rows == 0 )
					{
						printf("Sorry, no matches were found. Please try again.");
					}
				?>
            </div>
        </div>
    </body>
</html>