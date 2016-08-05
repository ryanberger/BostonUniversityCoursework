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
        
    }

    protected void Update_Output(object sender, EventArgs e)
    {
        Hashtable table = new Hashtable();
        string contents = TextBox1.Text;
        string[] words = contents.Split(' ');
        string output = string.Empty;

        foreach (string word in words)
        {
            if (table[word] == null)
            {
                table[word] = 1;
            }
            else
            { 
                int n = (int)table[word]; 
                table[word] = n+1;
            }
        }

        SortedList entries = new SortedList();
        foreach (DictionaryEntry d in table)
        {
            entries.Add(d.Key, d.Value);
        }

        foreach (DictionaryEntry de in entries)
        {
            output += de.Key + "(" + de.Value + ")<br/>"; 
        }

        Output1.InnerHtml = output;
        
    }
}
