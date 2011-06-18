using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Text;

public partial class _Default : System.Web.UI.Page 
{
    protected void Page_Load(object sender, EventArgs e)
    {

    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void FindFrequencies(object sender, EventArgs e)
    {
        CharResults.Items.Clear();
        BigramResults.Items.Clear();
        TrigramResults.Items.Clear();
        Result.InnerHtml = InputText.Text.ToUpper();
        FindCharacterFrequencies();
        FindBigramFrequencies();
        FindTrigramFrequencies();
    }

    /// <summary>
    /// 
    /// </summary>
    private void FindCharacterFrequencies()
    {
        CharResults.Enabled = true;
        CharGuess.Enabled = true;
        StringBuilder charResults = new StringBuilder();
        StringBuilder charStorage = new StringBuilder();
        string text = InputText.Text.ToUpper();
        Dictionary<char, int> charOccur = new Dictionary<char, int>();

        foreach (char c in text)
        {
            if (!charOccur.ContainsKey(c) && !Regex.IsMatch(c.ToString(), @"[\s|\.|,]"))
                charOccur.Add(c, text.Count(f => f == c));
        }

        var items = from k in charOccur.Keys
                    orderby charOccur[k] descending
                    select k;

        foreach (char k in items)
        {
            if (charOccur[k] > 0)
            {
                CharResults.Items.Add(k.ToString());
                charStorage.Append(k.ToString());
            }
        }
        CharStorage.InnerHtml = charStorage.ToString();
    }

    /// <summary>
    /// 
    /// </summary>
    private void FindBigramFrequencies()
    {
        BigramResults.Enabled = true;
        BigramGuess.Enabled = true;
        StringBuilder bigramResults = new StringBuilder();
        StringBuilder bigramStorage = new StringBuilder();
        string text = InputText.Text.ToUpper();
        string bigram = "";
        string bigramRgx = "";
        Dictionary<string, int> bigramOccur = new Dictionary<string, int>();

        for (int i = 0; i < text.Length - 2; i++)
        {
            bigram = text[i].ToString() + text[i + 1].ToString();
            bigramRgx = cleanForRegex(bigram, 1);

            if (!bigramOccur.ContainsKey(bigram) && !Regex.IsMatch(bigram, @"[\s|\.|,]"))
                bigramOccur.Add(bigram, Regex.Matches(text, bigramRgx).Count);
        }

        var items = from k in bigramOccur.Keys
                    orderby bigramOccur[k] descending
                    select k;

        foreach (string k in items)
        {
            if (bigramOccur[k] > 0)
            {
                BigramResults.Items.Add(k);
                bigramStorage.Append(k + ",");
            }
        }

        BigramStorage.InnerHtml = bigramStorage.ToString();
    }

    /// <summary>
    /// 
    /// </summary>
    private void FindTrigramFrequencies()
    {
        TrigramResults.Enabled = true;
        TrigramGuess.Enabled = true;
        StringBuilder trigramResults = new StringBuilder();
        StringBuilder trigramStorage = new StringBuilder();
        string text = InputText.Text.ToUpper();
        string trigram = "";
        string trigramRgx = "";
        Dictionary<string, int> trigramOccur = new Dictionary<string, int>();

        for (int i = 0; i < text.Length - 3; i++)
        {
            trigram = text[i].ToString() + text[i + 1].ToString() + text[i + 2].ToString();
            trigramRgx = cleanForRegex(trigram, 2);

            if (!trigramOccur.ContainsKey(trigram) && !Regex.IsMatch(trigram, @"[\s|\.|,]"))
                trigramOccur.Add(trigram, Regex.Matches(text, trigramRgx).Count);
        }

        var items = from k in trigramOccur.Keys
                    orderby trigramOccur[k] descending
                    select k;

        foreach (string k in items)
        {
            if (trigramOccur[k] > 0)
            {
                TrigramResults.Items.Add(k);
                trigramStorage.Append(k + ",");
            }
        }

        TrigramStorage.InnerHtml = trigramStorage.ToString();
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void GuessCharacter(object sender, EventArgs e)
    {
        string text = InputText.Text.ToUpper();
        string result = Result.InnerText;
        string inputChar = CharResults.SelectedItem.Text;
        string guess = CharGuess.Text;

        Result.InnerText = ReplaceString(text, result, inputChar, guess, 1);
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void GuessBigram(object sender, EventArgs e)
    {
        string text = InputText.Text.ToUpper();
        string result = Result.InnerText;
        string inputChar = BigramResults.SelectedItem.Text;
        string guess = BigramGuess.Text;

        Result.InnerText = ReplaceString(text, result, inputChar, guess, 2);
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void GuessTrigram(object sender, EventArgs e)
    {
        string text = InputText.Text.ToUpper();
        string result = Result.InnerText;
        string inputChar = TrigramResults.SelectedItem.Text;
        string guess = TrigramGuess.Text;

        Result.InnerText = ReplaceString(text, result, inputChar, guess, 3);
    }

    /// <summary>
    /// Replace answer box characters with the user's or the computer's guess
    /// </summary>
    /// <param name="text"></param>
    /// <param name="result"></param>
    /// <param name="inputString"></param>
    /// <param name="guess"></param>
    /// <param name="length"></param>
    /// <returns></returns>
    private static string ReplaceString(string text, string result, string inputString, string guess, int length)
    {
        StringBuilder resultString = new StringBuilder();
        resultString.Append(result);
        guess = guess.ToString().ToLower();

        for (int x = 0; x < text.Length - (length - 1); x++)
        {
            if (text.Substring(x, length) == inputString)
            {
                resultString.Remove(x, length);
                resultString.Insert(x, guess);
            }
        }
        return resultString.ToString();
    }

    /// <summary>
    /// Escape special characters in order for them to be taken literally in Regex
    /// </summary>
    /// <param name="bigram"></param>
    /// <returns></returns>
    private static string cleanForRegex(string bigram, int length)
    {
        for (int j = length; j >= 0; j--)
        {
            switch (bigram[j])
            {
                case '[':
                case ']':
                case '\\':
                case '^':
                case '$':
                case '.':
                case '|':
                case '?':
                case '*':
                case '+':
                case '(':
                case ')':
                    bigram = bigram.Insert(j, @"\");
                    break;
            }
        }
        return bigram;
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void ComputerGuessCharacter(object sender, EventArgs e)
    {
        string text = InputText.Text.ToUpper();
        string result = Result.InnerText;
        string chars = CharStorage.InnerText;
        string frequency = "etaoinshrdlcumwfgypbvkjxqz";
        int i = 0;

        foreach (char c in chars)
        {
            if (i > frequency.Length - 1)
                break;
            result = ReplaceString(text, result, c.ToString(), frequency[i].ToString().ToLower(), 1);
            i++;
        }

        Result.InnerText = result;
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void ComputerGuessBigram(object sender, EventArgs e)
    {
        string text = InputText.Text.ToUpper();
        string result = Result.InnerText;
        string bigrams = BigramStorage.InnerText;
        string frequency = "th,he,in,er,an,re,nd,at,on,nt,ha,es,st," + 
                           "en,ed,to,it,ou,ea,hi,is,or,ti,as,te,et," + 
                           "ng,of,al,de,se,le,sa,si,ar,ve,ra,ld,ru";
        string[] freqTable = frequency.Split(',');
        string[] bigramTable = bigrams.Split(',');
        int i = 0;

        foreach (string b in bigramTable)
        {
            if (i > freqTable.Length - 1)
                break;
            result = ReplaceString(text, result, b, freqTable[i].ToLower(), 2);
            i++;
        }

        Result.InnerText = result;
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void ComputerGuessTrigram(object sender, EventArgs e)
    {
        string text = InputText.Text.ToUpper();
        string result = Result.InnerText;
        string trigrams = TrigramStorage.InnerText;
        string frequency = "the,and,tha,ent,ing,ion,tio,for," +
                           "nde,has,nce,edt,tis,oft,sth,men";
        string[] freqTable = frequency.Split(',');
        string[] trigramTable = trigrams.Split(',');
        int i = 0;

        foreach (string t in trigramTable)
        {
            if (i > freqTable.Length - 1)
                break;
            result = ReplaceString(text, result, t, freqTable[i].ToLower(), 3);
            i++;
        }

        Result.InnerText = result;
    }

    /// <summary>
    /// Updates solution to contain any spaces or punctuation entered by user while decoding
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void UpdateResultField(object sender, EventArgs e)
    {
        // I attempted to build in logic to allow the user to add in spaces and punctuation while maintaining their decoding work,
        // but it's too buggy at the moment to be of any use...

        //StringBuilder resultString = new StringBuilder();
        //string text = InputText.Text;
        //string result = Result.InnerText;
        //resultString.Append(result);
        //char c;

        //if (result.Length > 0 && text.Length != result.Length)
        //{
        //    MatchCollection mc = Regex.Matches(text, @"((\s|\W){1})"); // Match any whitespace characters or punctuation

        //    foreach (Match m in mc)
        //    {
        //        if (m.Length > 0)
        //        {
        //            c = m.ToString()[0];

        //            if (result.Length >= m.Index && result[m.Index] != c)
        //            {
        //                resultString.Insert(m.Index, c);
        //            }
        //        }
        //    }
        //    Result.InnerText = resultString.ToString();
        //}
    }
}
