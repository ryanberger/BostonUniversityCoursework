<?php session_start(); require 'templates/approve.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<!-- Ryan Berger, BU ID: U41933866 -->
    <head>
        <title>Edit File</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/template.css" />
		<script type="text/javascript" src="javascripts/validations.js"></script>
		<script type="text/javascript">
			function confirmDeleteFile()
			{
				return confirm("Are you sure you really want to delete this file?");
			}
		</script>
    </head>
    <body>
		<?php include("templates/sqlConnect.php"); ?>
		<?php include("templates/menu.php"); ?>
        <div class="right-col">
            <div class="indented">
			<?php
			
			$oldFeedId = $_GET['id'];
			$oldFilePath = $_GET['path'];
			$oldFileName = $_GET['name'];
			
			$fileQuery = "SELECT file_size, date_posted FROM files WHERE feed_id = $oldFeedId AND file_path = '".mysqli_real_escape_string($mysqli, $oldFilePath)."' AND file_name = '".mysqli_real_escape_string($mysqli, $oldFileName)."'";
			$fileResult = $mysqli->query($fileQuery);
			
			list($oldFileSize, $oldDatePosted) = $fileResult->fetch_row();
			
			echo '<form name="files" method="post" action="" onsubmit="return validateFiles();">
					<table>
					<tr>
						<td>File Name: </td>
						<td><input type="text" name="fileName" id="fileName" class="textfield" value="'.$oldFileName.'" /></td>
					</tr>
					<tr>
						<td>File Path: </td>
						<td><input type="text" name="filePath" id="filePath" class="textfield" value="'.$oldFilePath.'" /></td>
					</tr>
					<tr>
						<td>File size (KB): </td>
						<td><input type="text" name="fileSize" id="fileSize" class="textfield" value="'.$oldFileSize.'" /></td>
					</tr>
					<tr>
						<td>Date posted: </td>
						<td><input type="text" name="datePosted" id="datePosted" class="textfield" value="'.$oldDatePosted.'" /></td>
					</tr>
					<tr>
						<td>Feed: </td>
						<td><select id="chooseFeed" name="chooseFeed">';
							$query = "SELECT feed_id, description FROM feeds";
							$result = $mysqli->query($query);
							while (list($feedId, $desc) = $result->fetch_row())
							{
								printf('<option value="%s"', $feedId);
								if ($oldFeedId == $feedId)
								{
									printf(' selected="selected"');
								}
								printf('>%s</option>', $desc);
							}
					echo '</select></td>
					</tr>
					</table>
					<input type="button" name="cancel" value="Cancel" onclick="location.href=\'index.php\';" />
					<input type="submit" name="updateFile" id="updateFile" value="Update File" />
				</form>
				<form name="delete" method="post" action="" onsubmit="return confirmDeleteFile();">
					<input type="submit" name="deleteFile" id="deleteFile" value="Delete File" />
				</form>
				<br />';
				?>
				<?php
					if (isset($_POST['updateFile']))
					{
						$selectedFiles = $_POST['selectedFiles'];
						
						$filePath = mysqli_real_escape_string($mysqli, $_POST['filePath']);
						$fileName = mysqli_real_escape_string($mysqli, $_POST['fileName']);
						$fileSize = $_POST['fileSize'];
						$datePosted = $_POST['datePosted'];
						$feedId = $_POST['chooseFeed'];
						
						$update = "UPDATE files SET feed_id = $feedId, file_path = '$filePath', file_name = '$fileName', file_size = '$fileSize', date_posted = '$datePosted' WHERE feed_id = $oldFeedId AND file_path = '".mysqli_real_escape_string($mysqli, $oldFilePath)."' AND file_name = '".mysqli_real_escape_string($mysqli, $oldFileName)."'";
						$mysqli->query($update);
						
						if ($mysqli->errno == 1062)
						{
							printf('%s already exists for this feed. Please choose a different one.', $fileName);
						}
						else
						{
							printf('%s successfully updated!', $fileName);
						}
					}
					
					if (isset($_POST['deleteFile']))
					{
						$delete = "DELETE FROM files WHERE feed_id = $oldFeedId AND file_path = '".mysqli_real_escape_string($mysqli, $oldFilePath)."' AND file_name = '".mysqli_real_escape_string($mysqli, $oldFileName)."'";
						$mysqli->query($delete);
						
						if ($mysqli->errno)
						{
							printf('Error occured when trying to delete file. Please try again.');
						}
						else
						{
							printf('%s successfully deleted! Click <a href="index.php">here</a> to go back.', $oldFileName);
						}
						
					}
				?>
            </div>
        </div>
    </body>
</html>