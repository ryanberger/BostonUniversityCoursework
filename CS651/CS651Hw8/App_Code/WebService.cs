using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;

using System.Web.Script.Services;
using System.Runtime.Serialization.Json;
using System.Text;
using System.IO;      

/// <summary>
/// Summary description for WebService
/// </summary>
[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
[System.Web.Script.Services.ScriptService]
public class WebService : System.Web.Services.WebService {

    public WebService () {

        //Uncomment the following line if using designed components 
        //InitializeComponent(); 
    }

    public class Sum
    {
        public int Num1;
        public int Num2;
        public int Answer;
    }

    [WebMethod]
    public string HelloWorld() {
        return "Hello World";
    }

    [WebMethod]
    [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
    public string X2(int num1, int num2)
    {
        Sum s = new Sum { Num1 = num1, Num2 = num2, Answer = 0 };

        s.Answer = s.Num1 + s.Num2;

        string answer = Convert.ToString(s.Answer);

        DataContractJsonSerializer serializer = new DataContractJsonSerializer(answer.GetType());
        MemoryStream ms = new MemoryStream();
        serializer.WriteObject(ms, answer);
        string jsonstring = Encoding.Default.GetString(ms.ToArray());
        ms.Close();
        return jsonstring; 
    }
    
}

