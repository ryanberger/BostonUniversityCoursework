<link rel="stylesheet" type="text/css" href="stylesheets/boxy.css" />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js"></script>
<script type="text/javascript" src="javascripts/jquery.boxy.js"></script>

<?php
	echo '<div class="left-col">
	<a href="index.php"><img src="images/ebsco_logo.jpg" width="135" height="53" border="0" /></a>
	<div class="menu-item"><a href="#" onclick="about();">About</a></div>';
	if (!isset($_SESSION['phplogin'])
		|| $_SESSION['phplogin'] !== true) {
		echo '<div class="menu-item"><a href="login.php">Admin Login</a></div>';
	}
	else
	{
		echo '<div class="menu-item"><a href="logout.php">Admin Logout</a></div>';
		echo '<div class="menu-item"><a href="newVendor.php">New Vendor</a></div>';
		echo '<div class="menu-item"><a href="newFeed.php">New Feed</a></div>';
		echo '<div class="menu-item"><a href="newProduct.php">New Product</a></div>';
		echo '<div class="menu-item"><a href="addFiles.php">Add Files</a></div>';
		echo '<div class="menu-item"><a href="manageAccounts.php">Manage Accounts</a></div>';
	}
	
echo '</div>
<div class="header">
	<div class="right-align">
		<form action="mainSearch.php" method="post">
			<div class="search-bar">
			<input type="text" id="searchTerm" name="searchTerm" value="'.$_SESSION['searchTerm'].'" />
			<select id="searchOption" name="searchOption">
					<option value="prodcode" '.selected('prodcode').'>Product Code</option>
					<option value="vendor_name" '.selected('vendor_name').'>Vendor Name</option>
					<option value="feed_name" '.selected('feed_name').'>Feed Name</option>
				</select>
				<input type="submit" name="search" value="Search" />
			</div>
		</form>
	</div>
</div>';

function selected($term)
{
	if ($_SESSION['searchOption'] == $term)
	{
		return 'selected="selected"';
	}
	else
	{
		return '';
	}
}

?>

<script type="text/javascript">
	function about()
	{
		new Boxy("<p>Created by: Ryan Berger &copy; 2009</p>", {title: "About", draggable: false, modal: true});
	}
</script>