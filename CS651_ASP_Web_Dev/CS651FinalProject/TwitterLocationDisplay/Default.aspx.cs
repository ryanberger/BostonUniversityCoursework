using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Xml;
using System.Runtime.Serialization.Json;

using LinqToTwitter;
using System.IO;
using System.Text;

public partial class _Default : System.Web.UI.Page 
{
    public const string APP_ID = "0w0zUPjV34G2vUrWG.A3MNBaxsRbBa_jDB070SXIBHFLRPqnnp.NL6xkfO41IA--";

    public class Data
    {
        public string Name;
        public string Latitude;
        public string Longitude;
        public string Trend1;
        public string Query1;
        public string Trend2;
        public string Query2;
        public string Trend3;
        public string Query3;
    }

    protected void Page_Load(object sender, EventArgs e)
    {
        TwitterContext twitterCtx = new TwitterContext();
        List<string> locations = new List<string>();
        int count = 0;
        string name = string.Empty;
        string latitude = string.Empty;
        string longitude = string.Empty;
        List<Data> outputList = new List<Data>();
        string url = string.Empty;
        string customLoc = Request.Params["Location"];

        if (!String.IsNullOrEmpty(customLoc))
        {
            if (customLoc == "Default") // Custom location not defined
            {
                // Find out the locations where trends are most concentrated
                var trends =
                    from trnd in twitterCtx.Trends
                    where trnd.Type == TrendType.Available
                    select trnd;

                var trend = trends.FirstOrDefault();

                trend.Locations.ToList().ForEach(
                    loc => locations.Add(loc.WoeID));
            }
            else
            {
                locations = getNearbyLocations(customLoc);
            }


            // Convert every woeID into coordinates
            foreach (string woeID in locations)
            {
                // Only find the top 10
                if (count > 10)
                    break;

                bool isDone = false;

                url = String.Format("http://where.yahooapis.com/v1/place/{0}?appid={1}", woeID, APP_ID);
                XmlTextReader oXmlReader = new XmlTextReader(url);

                string eName = string.Empty;

                while (oXmlReader.Read() && !isDone)
                {
                    switch (oXmlReader.NodeType)
                    {
                        case XmlNodeType.Element:
                            eName = oXmlReader.Name;
                            break;
                        case XmlNodeType.Text:
                            switch (eName)
                            {
                                case "name":
                                    name = oXmlReader.Value;
                                    break;
                                case "latitude":
                                    latitude = oXmlReader.Value;
                                    break;
                                case "longitude":
                                    longitude = oXmlReader.Value;
                                    isDone = true;
                                    break;
                            }
                            break;
                    }
                }

                // Find the top trends for each location
                if (woeID != null)
                {
                    try
                    {
                        var queries =
                            (from trnd in twitterCtx.Trends
                             where trnd.Type == TrendType.Location &&
                                   trnd.WeoID == int.Parse(woeID)
                             select trnd)
                                .ToList();

                        outputList.Add(new Data
                                           {
                                               Name = name,
                                               Latitude = latitude,
                                               Longitude = longitude,
                                               Trend1 = queries[0].Name,
                                               Query1 = queries[0].Query,
                                               Trend2 = queries[1].Name,
                                               Query2 = queries[1].Query,
                                               Trend3 = queries[2].Name,
                                               Query3 = queries[2].Query
                                           });
                        count++;
                    }
                    catch (Exception ex)
                    {
                        Page page = HttpContext.Current.Handler as Page;
                        if (page != null)
                        {
                            string textForMessage = "Error: could not find any trends for " + name;
                            ScriptManager.RegisterStartupScript(page, page.GetType(), "err_msg", "alert('" + textForMessage + "');",
                                                            true);
                        }
                    }
                }
            }

            DataContractJsonSerializer serializer = new DataContractJsonSerializer(outputList.GetType());
            MemoryStream ms = new MemoryStream();
            //serialize the object to memory stream
            serializer.WriteObject(ms, outputList);
            //convert the serizlized object to string
            string jsonString = Encoding.Default.GetString(ms.ToArray());
            ms.Close();

            if (Page.ClientQueryString.Length > 0)
            {
                Response.Write(jsonString);
            }
        }
    }

    private List<string> getNearbyLocations(string customLoc)
    {
        string url = String.Format("http://where.yahooapis.com/v1/places.q('{0}')?appid={1}", HttpUtility.UrlEncode(customLoc), APP_ID);
        XmlTextReader oXmlReader = new XmlTextReader(url);

        List<string> woeIDList = new List<string>();
        string eName = string.Empty;
        bool isDone = false;

        while (oXmlReader.Read() && !isDone)
        {
            switch (oXmlReader.NodeType)
            {
                case XmlNodeType.Element:
                    eName = oXmlReader.Name;
                    break;
                case XmlNodeType.Text:
                    switch (eName)
                    {
                        case "woeid":
                            woeIDList.Add(oXmlReader.Value);
                            isDone = true;
                            break;
                    }
                    break;
            }
        }

        /* I tried retrieving the neighboring locations, but most of the time the cities/towns are too small to have trends */

        //url = String.Format("http://where.yahooapis.com/v1/place/{0}/neighbors?appid={1}", woeID, APP_ID);
        //oXmlReader = new XmlTextReader(url);

        //eName = string.Empty;

        //while (oXmlReader.Read())
        //{
        //    switch (oXmlReader.NodeType)
        //    {
        //        case XmlNodeType.Element:
        //            eName = oXmlReader.Name;
        //            break;
        //        case XmlNodeType.Text:
        //            switch (eName)
        //            {
        //                case "woeid":
        //                    woeIDList.Add(oXmlReader.Value);
        //                    break;
        //            }
        //            break;
        //    }
        //}
        return woeIDList;
    }

}