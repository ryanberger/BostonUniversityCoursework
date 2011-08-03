function validateAccount()
{
	var username = document.getElementById('newUsername').value;
	var password = document.getElementById('newPassword').value;
	var confirmPassword = document.getElementById('confirmPassword').value;
	
	if (username == "")
	{
		alert("You must specify a username. Please try again.");
		return false;
	}
	else if (password == "" || password.length < 6)
	{
		alert("You must specify a password of at least 6 characters. Please try again.");
		return false;
	}
	else if (password != confirmPassword)
	{
		alert("Password values don't match. Please try again.");
		return false;
	}
	else
	{
		return true;
	}

}

function validateVendor()
{
    var vendorName = document.getElementById('vendorName').value;
    
    if (vendorName == "")
	{
		alert("You must specify a vendor name. Please try again.");
		return false;
	}
	else
	{
	    return true;
	}
}

function validateFeed()
{
    var desc = document.getElementById('desc').value;
    var approxSize = document.getElementById('approxSize').value;
    
    if (desc == "")
	{
		alert("You must specify a description. Please try again.");
		return false;
	}
	else if (!(isInteger(approxSize)))
	{
	    alert("Approximate size must be a valid integer. Please try again.");
		return false;
	}
	else if (approxSize < 0)
	{
	    alert("Approximate size cannot be a negative integer. Please try again.");
		return false;
	}
	else
	{
	    return true;
	}
}

function validateProduct()
{
    var prodCode = document.getElementById('prodCode').value;
    var prodName = document.getElementById('prodName').value;
    
    if (prodCode == "")
	{
		alert("You must specify a product code. Please try again.");
		return false;
	}
	else if (prodName = "")
	{
	    alert("You must specify a product name. Please try again.");
		return false;
	}
	else
	{
	    return true;
	}
}

function validateFiles()
{
    var fileName = document.getElementById('fileName').value;
    var filePath = document.getElementById('filePath').value;
    var fileSize = document.getElementById('fileSize').value;
    var datePosted = document.getElementById('datePosted').value;
    var rgxDate = /\d{4}\-\d{2}\-\d{2}/; // Check to make sure date is in the following format: CCYY-MM-DD
    
    if (fileName == "")
	{
		alert("You must specify a file name. Please try again.");
		return false;
	}
	else if (filePath = "")
	{
	    alert("You must specify a file path. Please try again.");
		return false;
	}
	else if (!(isInteger(fileSize)))
	{
	    alert("File size must be a valid integer. Please try again.");
		return false;
	}
	else if (fileSize < 0)
	{
	    alert("File size cannot be a negative integer. Please try again.");
		return false;
	}
	else if (!(datePosted.match(rgxDate)))
	{
	    alert("Date posted is not in CCYY-MM-DD format. Please try again.");
		return false;
	}
	else
	{
	    return true;
	}
}
