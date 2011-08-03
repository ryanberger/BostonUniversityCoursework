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
					$query = "SELECT vendor_id, vendor_name FROM vendor_info ORDER BY vendor_name";
					$result = $mysqli->query($query);
					
					printf('<table class="formatted"><tr><th>Vendor Name</th></tr>');
					while (list($vendorId, $vendorName) = $result->fetch_row())
					{
						printf('<tr><td><a href="vendorInfo.php?id=%s">%s</a></td></tr>', $vendorId, $vendorName);
					}
					printf('</table>');
				?>
            </div>
        </div>
    </body>
</html>