<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>RBerger CS651 HW4</title>
    <script type="text/javascript">
        var ajaxRequest;
        var cachedAjaxRequest;
        var currentPic = 100;
        var theURL;
        var theCachedURL;
        
        function initAjax()
        {
            try
            {
                ajaxRequest = new XMLHttpRequest();
                cachedAjaxRequest = new XMLHttpRequest();
            }
            catch(Error)
            {
                ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
                cachedAjaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
            }
            
            ShowPic(currentPic);
        }
        
        function GetPic(thisPic)
        {
            theCachedURL = "GetPicture.aspx?picid=" + thisPic;
            cachedAjaxRequest.open("GET", theCachedURL);
            cachedAjaxRequest.send();
        }
        
        function ShowPic(thisPic)
        {
            theURL = "GetPicture.aspx?picid=" + thisPic;
            ajaxRequest.open();
            ajaxRequest.onreadystatechange = DisplayPic();
            ajaxRequest.send();
        }
        
        function DisplayPic()
        {
            var divPic = document.getElementById("pic");
            if (ajaxRequest.readyState == 4)
            {
                divPic.innerHTML = "<img alt='not present' src='" + theURL + "'/>";
            }
        }
        
    </script>
</head>
<body>
    <form id="form1" runat="server">
    <div>
    
    </div>
    </form>
</body>
</html>
