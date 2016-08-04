/*
 * FILE:        FileTransformReducer.cs
 *                                                                      
 * DESCRIPTION: The file contains implementation for providing combine and reduce functionality for transforming the data.
 *
 */

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Xml;
using Research.MapReduce.Core;

namespace CS755.MapReduce.FileTransform
{
    /// <summary>
    /// Provides the combine and reduce functionality for the transform
    /// </summary>
    public sealed class FileTransformReducer : IReducer<int, string, int, string>
    {
		struct Record
		{
			public string Title;
			public string DateAdded;
			public string Body;
		}

        #region Methods

    	/// <summary>
        /// Transforms the record.
        /// </summary>
        /// <param name="key">Record Number.</param>
        /// <param name="values">The record (should only be one in the list).</param>
        /// <param name="context">An instance of <see cref="Research.MapReduce.Core.
        /// ReduceContext{K, V}"/>.</param>
        /// <returns>The transformed record identified by the record number.</returns>
        public IEnumerable<KeyValuePair<int, string>> Reduce(int key, IEnumerable<string> values, ReduceContext<int, string> context)
    	{
			XmlDocument xmlDoc = new XmlDocument();
			xmlDoc.LoadXml(values.First());

    		var record = ParseXml(xmlDoc);
			RunRules(ref record);

			string value = string.Format("<record><title>{0}</title><date>{1}</date><text>{2}</text></record>",
				record.Title, record.DateAdded, record.Body);

			yield return new KeyValuePair<int, string>(key, value);
        }

        /// <summary>
        /// Empty method implementation since there is nothing to configure.
        /// </summary>
        /// <param name="context">An instance of <see cref="Research.MapReduce.Core.
        /// ReduceContext{K, V}"/>.</param>
        public void Configure(ReduceContext<int, string> context) { }

		private static Record ParseXml(XmlDocument test)
		{
			var record = new Record();
			record.Title = test.SelectSingleNode(@"//title").InnerText;
			record.DateAdded = test.SelectSingleNode(@"//timestamp").InnerText;
			record.Body = test.SelectSingleNode(@"//text").InnerText;

			return record;
		}

		private static void RunRules(ref Record record)
		{
			record.Title = record.Title.TrimEnd('.');
			record.DateAdded = DateTime.Parse(record.DateAdded).ToShortDateString();
			record.Body = Regex.Replace(record.Body, @"<[^>]*>", String.Empty); // Strip HTML tags
		}

    	#endregion
    }
}
