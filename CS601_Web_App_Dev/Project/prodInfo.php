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
					$prodCode = $_GET['prodcode'];
					
					$feedQuery = "SELECT f.vendor_id, f.feed_id, f.description, f.delivery_type, f.delivery_freq, f.build_freq, f.approx_size, f.filename_pattern 
						FROM feeds f, feed_prod p WHERE p.prodcode='$prodCode' AND f.feed_id = p.feed_id";
					$feedResult = $mysqli->query($feedQuery);
					
					if ( $feedResult->num_rows > 0 )
					{
						printf('<h3>Related Feeds for %s</h3><table class="formatted"><tr><th>Description</th><th>Delivery Type</th><th>Delivery Frequency</th><th>Build Frequency</th><th>Approximate Size</th><th>Filename Pattern</th></tr>', $prodCode);
						while (list($vendorId, $feedId, $desc, $deliveryType, $deliveryFreq, $buildFreq, $approxSize, $filenamePattern) = $feedResult->fetch_row())
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
					else
					{
						printf('<h3>No feeds are currently associated with %s.</h3>', $prodCode);	
					}
				?>
            </div>
        </div>
    </body>
</html>