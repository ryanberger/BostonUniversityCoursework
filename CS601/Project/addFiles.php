<?php session_start(); require 'templates/approve.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>Add Files</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
		<script type="text/javascript" src="javascripts/validations.js"></script>
		<!--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/mootools/1.2.2/mootools.js"></script> 
		<script type="text/javascript" src="/fancyupload/Fx.ProgressBar.js"></script> 
		<script type="text/javascript" src="/fancyupload/Swiff.Uploader.js"></script> 
		<script type="text/javascript" src="/fancyupload/FancyUpload3.Attach.js"></script>-->
		
    </head>
    <body>
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
				<form name="files" method="post" action="<?php $_SERVER['PHP.SELF'] ?>" onsubmit="return validateFiles();">
					<table>
					<tr>
						<td>Select file: </td>
						<td><input type="file" name="fileName" id="fileName" /></td>
					</tr>
					<tr>
						<td>File Path: </td>
						<td><input type="text" name="filePath" id="filePath" class="textfield" /></td>
					</tr>
					<tr>
						<td>File size (KB): </td>
						<td><input type="text" name="fileSize" id="fileSize" class="textfield" /></td>
					</tr>
					<tr>
						<td>Date posted: </td>
						<td><input type="text" name="datePosted" id="datePosted" class="textfield" /></td>
					</tr>
					<tr>
						<td>Feed: </td>
						<td><select id="chooseFeed" name="chooseFeed">
						<?php 
							$query = "SELECT feed_id, description FROM feeds";
							$result = $mysqli->query($query);
							while (list($feedId, $desc) = $result->fetch_row())
							{
								printf('<option value="%s">%s</option>', $feedId, $desc);
							}
						?>
						</select></td>
					</tr>
					<tr>
						<td colspan="2"><input type="button" name="cancel" value="Cancel" onclick="location.href='index.php';" />
						<input type="submit" name="addFiles" id="addFiles" value="Add File" /></td>
					</tr>
					</table>
				</form>
				<?php
					if (isset($_POST['addFiles']))
					{
						$selectedFiles = $_POST['selectedFiles'];
						
						$pathInfo = pathinfo($selectedFiles);
						//$fileName = realpath($pathInfo[dirname]) . '\\' . $pathInfo[basename];
						//printf("DIR name: %s", realpath($pathInfo[dirname]));
						//printf("Base name: %s", $pathInfo[basename]);
						
						$filePath = mysqli_real_escape_string($mysqli, $_POST['filePath']);
						//$filePath = mysqli_real_escape_string($mysqli, realpath($pathInfo[dirname]));
						$fileName = mysqli_real_escape_string($mysqli, $pathInfo[basename]);
						$fileSize = $_POST['fileSize'];
						$datePosted = $_POST['datePosted'];
						$feedId = $_POST['chooseFeed'];
						
						$insert = "INSERT INTO files (feed_id, file_path, file_name, file_size, date_posted) VALUES ($feedId, '$filePath', '$fileName', '$fileSize', '$datePosted')";
						$mysqli->query($insert);
						
						if ($mysqli->errno == 1062)
						{
							printf('%s already exists for this feed. Please choose a different one.', $fileName);
						}
						else
						{
							printf('%s successfully added!', $fileName);
						}
					}
				?>
            </div>
        </div>
    </body>
</html>