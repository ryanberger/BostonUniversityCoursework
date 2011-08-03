<%@ Page Language="C#" AutoEventWireup="true" CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Ryan Berger Hw3 Ex1</title>
    <link rel="stylesheet" href="images/style.css" type="text/css" />
    <script type="text/javascript">
        // XMLHttpRequest
        var ajaxRequest;
        
        // initAJAX
        function initAjax() {
            try {
                ajaxRequest = new XMLHttpRequest(); 
            }
            catch (Error) {
                // IE 4 to IE 6
                ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP"); 
            }
        }
        // input
        function handleInput() {
            var T1 = document.getElementById("Textarea1");
            var theURL = "Default.aspx?text=" + T1.value;
            ajaxRequest.open("GET", theURL);
            ajaxRequest.onreadystatechange = handleUpdate;
            ajaxRequest.send();
        }
        // output
        function handleUpdate() {
            var ansDiv = document.getElementById("Output1");
            if (ajaxRequest.readyState == 4 ) {
                ansDiv.innerHTML = ajaxRequest.responseText.split(" ")[0];
            }
        }        
    </script>
</head>
<body onload="initAjax();">
<div id="page" align="center">
		<div id="header">
			<div id="companyname" align="left">Aqua</div>
			<div align="right" class="links_menu" id="menu"><a href="#">Home</a> | <a href="#">About Us</a> | <a href="#">Products</a> | <a href="#">Our Services</a> | <a href="#">Contact Us</a> </div>
		</div>
		<br />
		<div id="content">
			<div id="leftpanel">
				<div class="table_top">
					<div align="center"><span class="title_panel">News</span> </div>
				</div>
				<div class="table_content">
					<div class="table_text">
						<span class="news_date">October 16, 2006</span> <br />
						<span class="news_text">Curabitur arcu tellus, suscipit in, aliquam eget, ultricies id, sapien. Nam est.</span><br />
						<span class="news_more"><a href="#">Read More</a></span><br /><br />
						<span class="news_date">September 27, 2006</span> <br />
						<span class="news_text">Curabitur arcu tellus, suscipit in, aliquam eget, ultricies id, sapien. Nam est.</span><br />
						<span class="news_more"><a href="#">Read More</a></span>				
					</div>
				</div>
				<div class="table_bottom">
					<img src="images/table_bottom.jpg" width="204" height="23" border="0" alt="" />
				</div>
				<br />
				<div class="table_top">
					<span class="title_panel">Links</span>
				</div>
				<div class="table_content">
					<div class="table_text">
						<span class="news_more"><a href="http://www.winkhosting.com">Wink Hosting </a></span><br />
						<span class="news_more"><a href="http://www.google.com">Google </a></span><br />
						<span class="news_more"><a href="http://www.oswd.org">OSWD</a></span><br />
						<span class="news_more"><a href="http://www.yahoo.com">Yahoo</a></span><br />
						<span class="news_more"><a href="http://www.amazon.com">Amazon</a></span><br />
					</div>
				</div>
				<div class="table_bottom">
					<img src="images/table_bottom.jpg" width="204" height="23" border="0" alt="" />
				</div>
				<br />
			</div>
			<div id="contenttext">
				<span class="title_blue">Welcome to Aqua!</span><br />
				<span class="subtitle_gray">Some info here</span><br />
				<br />
				<p class="body_text" align="justify">
				    Enter as much text as you want
                    <div>
                        <textarea id="Textarea1" rows="8" cols="21" onkeyup="handleInput();"></textarea>
                    </div>
                    <div id="Output1">
                    </div>
				</p>
			</div>
			<br />
			<br />
			<div class="footer">
				<br />
				<a href="#">Home</a> | <a href="#">About Us</a> | <a href="#">Products</a> | <a href="#">Our Services</a> | <a href="#">Contact Us</a> | Your Company Name. Designed by <a href="http://www.winkhosting.com/">Wink Hosting</a>.
			</div>
		</div>
	</div>
</body>
</html>
