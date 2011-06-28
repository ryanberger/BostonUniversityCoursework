using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using System.Xml.Linq;
using System.IO;

using System.Text.RegularExpressions;  

namespace ConsoleApplication1
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length != 2)
            {
                System.Console.WriteLine("Please enter an input file and an output file");
                System.Console.WriteLine("Usage: ConsoleApplication1 <infile> <outfile>");
                return;
            }

            string filename = args[0];
            string pjfilename = System.AppDomain.CurrentDomain.BaseDirectory.ToString() + filename;
            string newfilename = args[1];

            FileInfo t = new FileInfo(newfilename);
            StreamWriter Tex = t.CreateText();

            XDocument wiki = XDocument.Load(pjfilename);
            IEnumerable<XElement> queryResult;
            //queryResult = wiki.Descendants("person").Where(p => p.Element("FirstName").Value.StartsWith("S"));
            queryResult = wiki.Descendants("page").Select(p => p);
            //queryResult = wiki.Descendants("page").Where(p => p.Element("FirstName").Value.StartsWith("S"));
            //queryResult = wiki.Descendants("mediawiki").Select(p => p);
            int numlines = queryResult.Count();

            int i = 0;
            foreach (XElement p in queryResult)
            {
                //ListBox1.Items.Add(p.Element("title").Value + ", " + p.Element("revision").Element("timestamp").Value);
                string article = p.Element("revision").Element("text").Value;
                string regex = "(== Taxonavigation ==|==Taxonavigation==)([^(==)]+)";
                string[] splits = Regex.Split(article, regex, RegexOptions.IgnoreCase);
                string taxonavigation = splits.Count() > 1 ? splits[2].Replace("\n", " ").Replace("<br />", " ").Replace("&nbsp", " ") : "";

                regex = "(== Name ==|==Name==)([^(==)]+)";
                splits = Regex.Split(article, regex, RegexOptions.IgnoreCase);
                string name = splits.Count() > 1 ? splits[2].Replace("\n", " ").Replace("<br />", " ").Replace("&nbsp", " ") : "";

                regex = "(== References ==|==References==)([^(==)]+)";
                splits = Regex.Split(article, regex, RegexOptions.IgnoreCase);
                string references = splits.Count() > 1 ? splits[2].Replace("\n", " ").Replace("<br />", " ").Replace("&nbsp", " ") : "";

                //string newline = p.Element("title").Value + ", " +
                //    p.Element("revision").Element("timestamp").Value + ", " +
                //    "== Taxonavigation == " + taxonavigation + ", " +
                //    "== Name == " + name + ", " +
                //    "== References == " + references;

                string newline = p.Element("title").Value + ", " +
                    p.Element("revision").Element("timestamp").Value + ", " +
                    "== Taxonavigation == " + taxonavigation + ", " +
                    "== Name == " + name;

                i++;
                Tex.WriteLine(newline);
                Console.WriteLine(i.ToString() + "/" + numlines.ToString());    
            }
            Tex.Write(Tex.NewLine);
            Tex.Close();
        }
    }
}
