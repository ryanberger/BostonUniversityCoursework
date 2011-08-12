/*
 * FILE:        FileTransformMapper.cs
 *                                                                      
 * DESCRIPTION: The file contains implementation for providing map functionality for splitting the input file into individual records.
 *
 */

using System.Collections.Generic;
using Research.MapReduce.Core;

namespace CS755.MapReduce.FileTransform
{
    /// <summary>
	/// Provides the map functionality for FileTransform by splitting the input file into individual records.
    /// </summary>
    public sealed class FileTransformMapper : IMapper<int, string, int, string>
    {
    	private string record = string.Empty;
    	private int recordNum = 0;
		private bool isRecord = false;


        #region Methods

        /// <summary>
        /// Splits the input file into individual records and sets the key to the record number.
        /// </summary>
        /// <param name="key">The line number of the input record.</param>
        /// <param name="value">A single line of text from the input.</param>
        /// <param name="context">An instance of <see cref="Research.MapReduce.Core.
        /// MapContext{K, V}"/>.</param>
        /// <returns>Enumerable of (recordNum, record) key value pairs.</returns>
        public IEnumerable<KeyValuePair<int, string>> Map(int key, string value, MapContext<int, string> context)
        {
			if (value.Trim().StartsWith("<record>"))
			{
				record += value;
				isRecord = true;
			}
			else if (value.Trim().StartsWith("</record>"))
			{
				record += value;
				isRecord = false;
				recordNum++;
				yield return new KeyValuePair<int, string>(recordNum, record);
			}
			else if (isRecord)
			{
				record += value;
			}
        }

        /// <summary>
		/// Configure Mapper.
        /// </summary>
        /// <param name="context">An instance of <see cref="Research.MapReduce.Core.
        /// MapContext{K, V}"/>.</param>
        public void Configure(MapContext<int, string> context)
        {
        	
        }

        #endregion
    }
}
