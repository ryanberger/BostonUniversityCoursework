using System;
using System.Xml;

public partial class _Default : System.Web.UI.Page 
{
    public const string APP_ID = "ciMARmTV34HgPaVX3EkHzq_ejyJVE8qDogvf6KVi8MQLG0QU4tTv6eTWAYXA.A--";
    

    protected void Page_Load(object sender, EventArgs e)
    {

    }
    protected void Button1_Click(object sender, EventArgs e)
    {
        // Instructions for interacting with this web service can be found here: http://developer.yahoo.com/maps/rest/V1/geocode.html
        string location = TextBox1.Text;
        string url = "http://local.yahooapis.com/MapsService/V1/geocode?appid=" + APP_ID + "&location=" + location;

        // Reading XML from a URL credited to http://www.aspcode.net/Reading-XML-from-a-URL-with-C.aspx
        XmlTextReader oXmlReader = new XmlTextReader(url);
        string eName = string.Empty;

        mydiv.InnerHtml = "";

        while (oXmlReader.Read())
        {
            switch (oXmlReader.NodeType)
            {
                case XmlNodeType.Element:
                    eName = oXmlReader.Name;
                    break;
                case XmlNodeType.Text:
                    switch (eName)
                    {
                        case "Latitude":
                            mydiv.InnerHtml += "Latitude: " + oXmlReader.Value + "<br />";
                            break;
                        case "Longitude":
                            mydiv.InnerHtml += "Longitude: " + oXmlReader.Value + "<br />";
                            break;
                        case "Address":
                            mydiv.InnerHtml += "Address: " + oXmlReader.Value + "<br />";
                            break;
                        case "City":
                            mydiv.InnerHtml += "City: " + oXmlReader.Value + "<br />";
                            break;
                        case "State":
                            mydiv.InnerHtml += "State: " + oXmlReader.Value + "<br />";
                            break;
                        case "Zip":
                            mydiv.InnerHtml += "Zip: " + oXmlReader.Value + "<br />";
                            break;
                        case "Country":
                            mydiv.InnerHtml += "Country: " + oXmlReader.Value + "<br />";
                            break;
                    }
                    break;
            }
        }
    }
}
