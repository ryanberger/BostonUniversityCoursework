<?php session_start(); ?>

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
					$vendorId = $_GET['id'];
					$vendorQuery = "SELECT vendor_name, address, phone, email FROM vendor_info WHERE vendor_id=$vendorId";
					$vendorResult = $mysqli->query($vendorQuery);
					
					list($vendorName, $address, $phone, $email) = $vendorResult->fetch_row();
					
					echo '<div><strong>Vendor Name: </strong>'.$vendorName.'</div>
						<div><strong>Address: </strong>'.$address.'</div>
						<div><strong>Phone: </strong>'.$phone.'</div>
						<div><strong>Email: </strong>'.$email.'</div>
					';
					
					if (isset($_SESSION['phplogin'])
						|| $_SESSION['phplogin'] == true) {
						echo '<div><input type="button" value="Edit Vendor Info" onclick="location.href=\'editVendor.php?id='.$vendorId.'\';" /></div>';
					}
					
					echo '<br />';
					
					$feedQuery = "SELECT feed_id, description, delivery_type, delivery_freq, build_freq, approx_size, filename_pattern 
						FROM feeds WHERE vendor_id=$vendorId";
					$feedResult = $mysqli->query($feedQuery);
					
					if ( $feedResult->num_rows > 0 )
					{
						printf('<h3>Related Feeds</h3><table class="formatted"><tr><th>Description</th><th>Delivery Type</th><th>Delivery Frequency</th><th>Build Frequency</th><th>Approximate Size</th><th>Filename Pattern</th></tr>');
						while (list($feedId, $desc, $deliveryType, $deliveryFreq, $buildFreq, $approxSize, $filenamePattern) = $feedResult->fetch_row())
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
					
					$prodQuery = "SELECT DISTINCT i.prodcode, i.prodname, i.date_range 
						FROM prod_info i, vendor_prod v WHERE v.vendor_id=$vendorId AND i.prodcode=v.prodcode";
					$prodResult = $mysqli->query($prodQuery);
					
					if ( $prodResult->num_rows > 0 )
					{
						printf('<br /><h3>Related Products</h3><table class="formatted"><tr><th>Product Code</th><th>Product Full Name</th><th>Approximate Size</th><th>Date Range</th></tr>');
						while (list($prodCode, $prodName, $dateRange) = $prodResult->fetch_row())
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
				?>
            </div>
        </div>
    </body>
</html>