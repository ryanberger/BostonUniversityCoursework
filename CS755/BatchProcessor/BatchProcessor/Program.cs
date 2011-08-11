using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Xml;

namespace BatchProcessor
{
	class Program
	{
		struct Record
		{
			public string Title;
			public string DateAdded;
			public string Body;
		}

		static void Main(string[] args)
		{
			int recordsProcessed = 0;
			DateTime startTime = DateTime.Now;
			
			
			TextWriter writer = new StreamWriter(@"..\..\..\Output\text.xml");
			writer.Write("<root>");

			var files = Directory.GetFiles(@"..\..\..\Input", "*.xml");

			foreach (var file in files)
			{
				FileStream input = new FileStream(file, FileMode.Open);
				XmlTextReader xmlText = new XmlTextReader(input);
				XmlNamespaceManager ns;

				XmlDocument test;
				while ((test = NextRecord(xmlText, out ns)) != null)
				{
					var record = ParseXml(test, ns);
					RunRules(ref record);
					WriteOutput(record, writer);
					recordsProcessed++;
				}
				input.Close();
			}

			writer.Write("</root>");
			writer.Close();
			Console.WriteLine("Time elapsed (seconds): {0}", (DateTime.Now - startTime).TotalSeconds);
			Console.WriteLine("Records processed: {0}", recordsProcessed);
			Console.WriteLine("Records per second: {0}", recordsProcessed / (DateTime.Now - startTime).TotalSeconds);
		}

		private static XmlDocument NextRecord(XmlTextReader xmlText, out XmlNamespaceManager ns)
		{
			string record = string.Empty;
			ns = null;

			while (!xmlText.Name.Equals("page"))
			{
				if (xmlText.EOF)
				{
					return null;
				}
				xmlText.Read();
			}

			record = xmlText.ReadOuterXml();

			XmlDocument test = new XmlDocument();
			test.LoadXml(record);
			ns = new XmlNamespaceManager(test.NameTable);
			ns.AddNamespace("ns", "http://www.mediawiki.org/xml/export-0.5/");
			return test;
		}

		private static Record ParseXml(XmlDocument test, XmlNamespaceManager ns)
		{
			var record = new Record();
			record.Title = test.SelectSingleNode(@"//ns:title", ns).InnerText;
			record.DateAdded = test.SelectSingleNode(@"//ns:timestamp", ns).InnerText;
			record.Body = test.SelectSingleNode(@"//ns:text", ns).InnerText;

			return record;
		}

		private static void RunRules(ref Record record)
		{
			record.Title = record.Title.TrimEnd('.');
			record.DateAdded = DateTime.Parse(record.DateAdded).ToShortDateString();
			record.Body = Regex.Replace(record.Body, @"<[^>]*>", String.Empty); // Strip HTML tags
		}

		private static void WriteOutput(Record record, TextWriter writer)
		{
			writer.Write(string.Format("<record><title>{0}</title><date>{1}</date><text>{2}</text></record>",
				record.Title, record.DateAdded, record.Body));
		}

	}
}
