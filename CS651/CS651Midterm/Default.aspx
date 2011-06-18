<%@ Page Language="C#" AutoEventWireup="true" CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Ryan Berger CS651 Midterm</title>
    <style type="text/css">
        #InputText
        {
            height: 150px;
            width: 360px;
        }
        #Result
        {
            height: 150px;
            width: 360px;
        }
    </style>
</head>
<body>
    <form id="form1" runat="server">
    <div>
        <asp:ScriptManager ID="ScriptManager1" runat="server">
        </asp:ScriptManager>
        <asp:UpdatePanel ID="UpdatePanel1" runat="server">
            <ContentTemplate>
                <asp:TextBox ID="InputText" runat="server" TextMode="MultiLine"
                    OnTextChanged="UpdateResultField" AutoPostBack="true"/>
                <textarea id="Result" rows="2" cols="10" disabled="disabled" runat="server"></textarea>
                <br />
                <asp:Button ID="Button1" runat="server" Text="Initialize" OnClick="FindFrequencies" />
                <asp:Button ID="Button2" runat="server" Text="Computer guess (Character)" OnClick="ComputerGuessCharacter" />
                <asp:Button ID="Button3" runat="server" Text="Computer guess (Bigram)" OnClick="ComputerGuessBigram" />
                <asp:Button ID="Button4" runat="server" Text="Computer guess (Trigram)" OnClick="ComputerGuessTrigram" />
                <br /><br />
                <asp:DropDownList ID="CharResults" Enabled="false" runat="server"/>
                <asp:TextBox ID="CharGuess" MaxLength="1" OnTextChanged="GuessCharacter" Enabled="false" runat="server" AutoPostBack="true"/>
                <asp:DropDownList ID="BigramResults" Enabled="false" runat="server"/>
                <asp:TextBox ID="BigramGuess" MaxLength="2" OnTextChanged="GuessBigram" Enabled="false" runat="server" AutoPostBack="true"/>
                <asp:DropDownList ID="TrigramResults" Enabled="false" runat="server"/>
                <asp:TextBox ID="TrigramGuess" MaxLength="3" OnTextChanged="GuessTrigram" Enabled="false" runat="server" AutoPostBack="true"/>
                <div id="CharStorage" runat="server" visible="false" />
                <div id="BigramStorage" runat="server" visible="false" />
                <div id="TrigramStorage" runat="server" visible="false" />
            </ContentTemplate>
        </asp:UpdatePanel>
    </div>
    </form>
</body>
</html>
