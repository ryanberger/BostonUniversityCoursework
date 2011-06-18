using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Collections;

public partial class _Default : System.Web.UI.Page 
{
    protected void Page_Load(object sender, EventArgs e)
    {

    }

    protected void Update_Output(object sender, EventArgs e)
    {
        Dictionary<string, int> dictionary = new Dictionary<string, int>();
        string contents = TextBox1.Text;
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

            int j = 1;
            OutputDiv.InnerHtml = "";

            for (int i = 1; i <= 10; i++)
            {
                OutputDiv.InnerHtml += String.Format("<select name=\"listbox{0}\" id=\"listbox{1}\" size=\"4\">", i, i);

                if (i < 10)
                {
                    foreach (KeyValuePair<string, int> kv in dictionary.Where(p => p.Key.Length == i).OrderBy(p => p.Key))
                    {
                        OutputDiv.InnerHtml += String.Format("<option value=\"{0}\">{1}</option>", j, kv.Key);
                        j++;
                    }
                }
                else
                {
                    foreach (KeyValuePair<string, int> kv in dictionary.Where(p => p.Key.Length >= i).OrderBy(p => p.Key))
                    {
                        OutputDiv.InnerHtml += String.Format("<option value=\"{0}\">{1}</option>", j, kv.Key);
                        j++;
                    }
                }

                j = 1;
                OutputDiv.InnerHtml += "</select>";
            }
        }
    }
}
