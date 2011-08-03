<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>CS651 HW2 RBerger</title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
        Enter as much text as you want</div>
        <p>
            <asp:TextBox ID="TextBox1" TextMode="MultiLine" Rows="8" runat="server" AutoPostBack="true" OnTextChanged="Update_Output"></asp:TextBox>
        </p> 
        <div>
            <div id="Output1" runat="server"></div>
        </div>
    </form>
</body>
</html>
