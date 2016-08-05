using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Net;
using System.IO;
using System.Xml;
using System.Text.RegularExpressions; 

public partial class _Default : System.Web.UI.Page 
{
    protected void Page_Load(object sender, EventArgs e)
    {

    }
    protected void Button1_Click(object sender, EventArgs e)
    {
        string strnewUrl;
        string strResponse;
        strnewUrl = Server.HtmlDecode(TextBox1.Text);
   
        // create the request object
        HttpWebRequest req = (HttpWebRequest)WebRequest.Create(strnewUrl);
        req.Method = "POST";
        req.ContentLength = 0;

        try
        {
            HttpWebResponse rep = (HttpWebResponse)req.GetResponse();
            HttpStatusCode sc = rep.StatusCode;
            //int sci = Convert.ToInt32(sc);
            string eName = string.Empty;
            string output = string.Empty;

            TextBox3.Text = sc.ToString();

            StreamReader stIn = new StreamReader(rep.GetResponseStream());
            strResponse = stIn.ReadToEnd();
            stIn.Close();

            output = Regex.Match(strResponse, @"<title>(?<TITLE>.*)</title>").Groups["TITLE"].ToString();

            //XmlTextReader oXmlReader = new XmlTextReader(strnewUrl);

            //try
            //{
            //    while (oXmlReader.Read())
            //    {
            //        switch (oXmlReader.NodeType)
            //        {
            //            case XmlNodeType.Element:
            //                eName = oXmlReader.Name;
            //                break;
            //            case XmlNodeType.Text:
            //                switch (eName)
            //                {
            //                    case "title":
            //                        output += oXmlReader.Value + ", ";
            //                        break;
            //                }
            //                break;
            //        }
            //    }
            //}
            //catch (XmlException xe)
            //{
            //    TextBox2.Text = xe.Message;
            //} 

            if (!String.IsNullOrEmpty(output))
            {
                TextBox2.Text = output;
            }
            else
            {
                TextBox2.Text = "Could not retrieve title!";
            }

        }
        catch (WebException we)
        {
            TextBox2.Text = we.Message;  
        }
    }
}
