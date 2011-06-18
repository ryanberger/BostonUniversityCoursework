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
					$feedId = $_GET['id'];
					$feedDesc = $_GET['desc'];
					
					$fileQuery = "SELECT file_path, file_name, file_size, date_posted
						FROM files WHERE feed_id=$feedId ORDER BY file_path";
					$fileResult = $mysqli->query($fileQuery);
					
					if ( $fileResult->num_rows > 0 )
					{
						printf('<h3>Related Files for %s feed</h3><table border="0">', $feedDesc);
						while (list($filePath, $fileName, $fileSize, $datePosted) = $fileResult->fetch_row())
						{
							if ($tempPath != $filePath)
							{
								printf('</table><br /><table class="formatted"><tr><td colspan="3">%s</td></tr>
									<tr><th>File Name</th><th>File Size (KB)</th><th>Date Posted</th></tr>', $filePath);
							}
							printf('<tr><td>%s</td><td>%s</td><td>%s</td>', $fileName, $fileSize, $datePosted);
							if (isset($_SESSION['phplogin'])
								|| $_SESSION['phplogin'] == true) {
								printf('<td><input type="button" value="Edit" onclick="location.href=\'editFile.php?id='.$feedId.'&path='.mysqli_real_escape_string($mysqli, $filePath).'&name='.$fileName.'\';" /></td>');
							}
							printf('</tr>');
							$tempPath = $filePath;
						}
						printf('</table>');
					}
					else
					{
						printf('<h3>No files are currently associated with %s feed.</h3>', $feedDesc);	
					}
				?>
            </div>
        </div>
    </body>
</html>