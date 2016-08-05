using System;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;
using System.Web;

namespace CS651Hw6Ex1
{
    public class SyncHandler : IHttpHandler
    {
        #region IHttpHandler Members

        Regex _vowelRegex = new Regex(@"A|a|E|e|I|i|O|o|U|u|Y|y", RegexOptions.Compiled);

        public bool IsReusable
        {
            get { return false; }
        }

        public void ProcessRequest(HttpContext context)
        {
            string text = "This is a handler which was written by Ryan Berger. All of the vowels will appear in blue!";
            StringBuilder formattedText = new StringBuilder();

            foreach (char c in text)
            {
                if (_vowelRegex.IsMatch(c.ToString()))
                {
                    formattedText.Append("<span style=\"color:blue;\">" + c.ToString() + "</span>");
                }
                else
                {
                    formattedText.Append(c.ToString());
                }
            }
            
            context.Response.Write(formattedText.ToString());
            
        }

        #endregion
    }
}
