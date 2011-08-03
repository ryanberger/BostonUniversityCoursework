/*
 * FILE:        WordCountMapper.cs
 *                                                                      
 * DESCRIPTION: The file contains implementation for providing map functionality for WordCount sample splitting the input string into individual words.
 *
 */

using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using Research.MapReduce.Core;

namespace CS755.MapReduce.FileTransform
{
    /// <summary>
    /// Provides the map functionality for WordCount sample by splitting the input string into
    /// individual words.
    /// </summary>
    public sealed class FileTransformMapper : IMapper<int, string, string, int>
    {
        #region Methods

        /// <summary>
        /// Splits the input string into individual words and sets occurance of each word to 1.
        /// </summary>
        /// <param name="key">The line number of the input record.</param>
        /// <param name="value">A single line of text from the input.</param>
        /// <param name="context">An instance of <see cref="Research.MapReduce.Core.
        /// MapContext{K, V}"/>.</param>
        /// <returns>Enumerable of (word, count) key value pairs.</returns>
        public IEnumerable<KeyValuePair<string, int>> Map(int key, string value, MapContext<int, string> context)
        {
            foreach (string word in Regex.Split(value, "[^a-zA-Z0-9]", RegexOptions.Singleline).
                Where(tuple => !string.IsNullOrEmpty(tuple)))
            {
                yield return new KeyValuePair<string, int>(word, 1);
            }
        }

        /// <summary>
        /// Empty method implementation since there is nothing to configure.
        /// </summary>
        /// <param name="context">An instance of <see cref="Research.MapReduce.Core.
        /// MapContext{K, V}"/>.</param>
        public void Configure(MapContext<int, string> context) { }

        #endregion
    }
}
