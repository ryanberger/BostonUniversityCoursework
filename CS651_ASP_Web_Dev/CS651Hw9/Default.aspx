<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
        <asp:TextBox ID="TextBox1" runat="server" Width="583px"></asp:TextBox>
        <asp:Button ID="Button1" runat="server" Text="Display Website Title" onclick="Button1_Click" />
        <asp:TextBox
            ID="TextBox3" runat="server" Width="185px"></asp:TextBox>
        <br />
        <asp:TextBox ID="TextBox2" runat="server" Height="321px" Width="639px"></asp:TextBox>
    </div>
    </form>
</body>
</html>
