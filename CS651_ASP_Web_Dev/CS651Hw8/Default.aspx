<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>RBerger CS651 HW 8</title>

    <script src="jquery-1.3.2.min.js" type="text/javascript">
    </script>

    <script type="text/javascript">
//        function parseResult(persons) {
//            var lists = '';
//            for (var i = 1; i <= persons.length; i++) {
//                if (lists == '') {
//                    lists = persons[i].FirstName;
//                }
//                else {
//                    lists = lists + " <br />" + persons[i].FirstName;
//                }
//            }
//            return lists;
//        }

        function parseResult(answer) {
            var lists = answer[0].Answer;
            
            return lists;
        }

        $(function() {
            $("#calc_submit").click(function() {
                // Set up a string to POST parameters.  
                // You can create the JSON string as well.

                //var dataString = "num1=" + $("#num1").val() + "&num2=" + $("#num2").val();

                $.ajax({
                    type: "POST",
                    url: "/CS651Hw8/WebService.asmx/X2",
                    data: "{'num1': '" + $("#num1").val() + "', 'num2': '" + $("#num2").val() + "'}",
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    beforeSend: function(XMLHttpRequest) {
                        $('#loading-panel').empty().html('Loading...');
                        //alert("b4..");
                    },
                    success: function(msg) {
                        $('#answer').val(eval(msg.d));
                        //alert("success: " + msg.d);
                    },
                    error: function(msg) {
                        $('#answer').append(msg);
                        alert("oops: " + msg);
                    },
                    complete: function(XMLHttpRequest, textStatus) {
                        $('#loading-panel').empty();
                        //alert("complete!");
                    }
                });
            });
        });               
    </script>
    
</head>
<body>
    <div>
        <input id="num1" type="text" />&nbsp;+&nbsp;<input id="num2" type="text" />
        <input id="calc_submit" type="button" value="=" />
        <input id="answer" type="text" disabled="disabled" />
        <div id="loading-panel" />
    </div>
</body>
</html>
