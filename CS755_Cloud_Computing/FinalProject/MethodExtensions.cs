/*
 * FILE:        MethodExtensions.cs
 *                                                                      
 * DESCRIPTION: The file contains implementation for common purpose extension methods.
 *
 */

using System;
using System.Text;
using Microsoft.WindowsAzure.StorageClient;

namespace CS755.MapReduce.FileTransform
{
    /// <summary>
    /// Contains common purpose extension methods.
    /// </summary>
    internal static class MethodExtensions
    {
        #region Methods

        /// <summary>
        /// Validates that the string is a well-formed uri. Throws an exception if the input
        /// string is not a well-formed uri.
        /// </summary>
        /// <param name="uri">String to be validated.</param>
        /// <param name="uriParamName">Name of the parameter.</param>
        /// <exception cref="System.ArgumentException"/>
        public static void ValidateUriFormat(this string uri, string uriParamName)
        {
            if (!Uri.IsWellFormedUriString(uri, UriKind.Absolute))
            {
                throw new ArgumentException(
                    string.Format(
                        "Value [{0}] specified for [{1}] is not a valid uri.",
                        uri,
                        uriParamName));
            }
        }
                
        /// <summary>
        /// Deletes all the <see cref="Microsoft.WindowsAzure.StorageClient.CloudBlob"/> in the specified 
        /// <see cref="Microsoft.WindowsAzure.StorageClient.CloudBlobContainer"/>.
        /// </summary>
        /// <param name="container">The <see cref="Microsoft.WindowsAzure.StorageClient.
        /// CloudBlobContainer"/> to be cleaned.</param>
        public static void Clean(this CloudBlobContainer container)
        {
            foreach (CloudBlob blob in container.ListBlobs(new BlobRequestOptions
            {
                UseFlatBlobListing = true
            }))
            {
                blob.Delete();
            }
        }

        /// <summary>
        /// Converts a string array to a single string.(The elements of the string array are 
        /// concatenated with a space between them).
        /// </summary>
        /// <param name="strArray">Array containing the strings that need to be concatenated.</param>
        /// <returns>Concatenated string representing the input string array.</returns>
        public static string GetString(this string[] strArray)
        {
            StringBuilder builder = new StringBuilder();
            foreach (string str in strArray)
            {
                builder.AppendFormat("{0} ", str);
            }
            return builder.ToString().TrimEnd();
        }

        #endregion
    }
}