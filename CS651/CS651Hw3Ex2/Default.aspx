<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Ryan Berger Hw3 Ex2</title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
        <asp:ScriptManager ID="ScriptManager1" runat="server" />
            
        <asp:UpdatePanel ID="UpdatePanel1" runat="server">
            <ContentTemplate>
                <asp:Panel ID="Panel1" runat="server" Height="87px" Width="267px">
                    <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox> <br />
                    <asp:TextBox ID="TextBox2" runat="server"></asp:TextBox> <br />
                    <asp:Button ID="Button1" runat="server" Text="Multiply" onclick="Button1_Click" /> <br />
                    <asp:Label ID="Label1" runat="server" Text="Answer"></asp:Label>
                </asp:Panel>
            </ContentTemplate>
        </asp:UpdatePanel>
    </div>
    </form>
</body>
</html>
