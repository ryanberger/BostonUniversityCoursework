using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using RottenTomatoesViewer.Web.MovieTheaterInfo;
using System.Text;
using System.Web.Script.Serialization;

namespace RottenTomatoesViewer.Web
{
    public partial class GetData : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            string zip = Request.Params["zip"];
            string query = Request.Params["query"];
            GetTheaterResults(zip, query);
        }

        private void GetTheaterResults(string zip, string query)
        {            
            MovieInformationSoapClient sc = new MovieInformationSoapClient();
            var info = sc.GetTheatersAndMovies(zip, 15);

            string output = string.Empty;

            foreach (Theater t in info)
            {
                foreach (Movie m in t.Movies)
                {
                    if (m.Name == query)
                    {
                        output += string.Format("{0}: {1}\n", t.Name, t.Address);
                    }
                }
            }

            if (string.IsNullOrWhiteSpace(zip))
            {
                output = "Please enter valid zip code.";
            }
            else if (string.IsNullOrWhiteSpace(output))
            {
                output = "Sorry, no theaters were found.";
            }

            Response.Write(output);
        }
    }
}