using System;
using System.Collections;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;

public partial class _Default : System.Web.UI.Page 
{
    protected void Page_Load(object sender, EventArgs e)
    {
        Dictionary<string, int> dictionary = new Dictionary<string, int>();
        string contents = Request.Params["text"];
        if (!string.IsNullOrEmpty(contents))
        {
            string[] words = contents.Split(' ', ',', '.', ';', '!', '?');
            string output = string.Empty;

            foreach (string word in words)
            {
                if (!string.IsNullOrEmpty(word))
                {
                    if (!dictionary.ContainsKey(word.ToLowerInvariant()))
                    {
                        dictionary.Add(word.ToLowerInvariant(), 1);
                    }
                    else
                    {
                        int n = 0;
                        dictionary.TryGetValue(word, out n);
                        dictionary[word] = n + 1;
                    }
                }
            }

            foreach (KeyValuePair<string, int> kv in dictionary)
            {
                output += kv.Key + "(" + kv.Value + ")<br/>";
            }
            Response.Write(output);
        }
    }
}
