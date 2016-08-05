<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Ryan Berger HW1</title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
        
    </div>
    
    <p>
        This page invokes the Yahoo! Geocoding Web Service which allows you to find the specific latitude and longitude 
        for an address.</p>
    <p>
        This free field lets users enter any of the following:</p>
    <ul>
        <li>
            city, state</li>
        <li>
            city, state, zip</li>
        <li>
            zip</li>
        <li>
            street, city, state</li>
        <li>
            street, city, state, zip</li>
        <li>
            street, zip</li>
    </ul>
    <p>
        Location:&nbsp; <asp:TextBox ID="TextBox1" runat="server" Width="375px"></asp:TextBox>
    </p>
    <asp:Button ID="Button1" runat="server" Text="Submit Location" 
        onclick="Button1_Click" />
        <p>
            <div id="mydiv" runat="server" />
        </p>
    </form>
    </body>
</html>
