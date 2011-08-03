/*
 * FILE:        WordCountReducer.cs
 *                                                                      
 * DESCRIPTION: The file contains implementation for providing combine and reduce functionality for WordCount sample by calculating the sum of occurances of the input key(word).
 *
 */

using System.Collections.Generic;
using System.Linq;
using Research.MapReduce.Core;

namespace CS755.MapReduce.FileTransform
{
    /// <summary>
    /// Provides the combine and reduce functionality for WordCount sample by calculating the sum
    /// of occurances of a word.
    /// </summary>
    public sealed class WordCountReducer : IReducer<string, int, string, int>
    {
        #region Methods

        /// <summary>
        /// Calculates the sum of occurances of the input key(word).
        /// </summary>
        /// <param name="key">Word whose occurances are to be added.</param>
        /// <param name="values">Occurances of the word provided as key.</param>
        /// <param name="context">An instance of <see cref="Research.MapReduce.Core.
        /// ReduceContext{K, V}"/>.</param>
        /// <returns>Enumerable of (word, count) key value pairs.</returns>
        public IEnumerable<KeyValuePair<string, int>> Reduce(string key, IEnumerable<int> values, ReduceContext<string, int> context)
        {
            yield return new KeyValuePair<string, int>(key, values.Sum());
        }

        /// <summary>
        /// Empty method implementation since there is nothing to configure.
        /// </summary>
        /// <param name="context">An instance of <see cref="Research.MapReduce.Core.
        /// ReduceContext{K, V}"/>.</param>
        public void Configure(ReduceContext<string, int> context) { }

        #endregion
    }
}
