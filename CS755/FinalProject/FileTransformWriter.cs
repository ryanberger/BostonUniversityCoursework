/*
 * FILE:        WordCountWriter.cs
 *                                                                      
 * DESCRIPTION: The file contains implementation to write the output of 'WordCountReducer' in CSV format.
 *
 */

using System;
using System.Collections.Generic;
using System.IO;
using Microsoft.WindowsAzure.StorageClient;
using Research.MapReduce.Core;

namespace CS755.MapReduce.FileTransform
{
    /// <summary>
    /// Writes the output of <see cref="Research.MapReduce.Samples.WordCount.WordCountReducer"/> 
    /// in CSV format.
    /// </summary>
    [Serializable]
    public sealed class FileTransformWriter : IRecordWriter<string, int>
    {
        #region Properties

        /// <summary>
        /// Instance of <see cref="Research.MapReduce.Core.CloudClient"/> for accessing 
        /// azure blob storage.
        /// </summary>
        public CloudClient CloudClient { get; private set; }

        /// <summary>
        /// The name of <see cref="Microsoft.WindowsAzure.StorageClient.CloudBlobContainer" /> 
        /// that is to be used for writing the output.
        /// </summary>
        public string ContainerName { get; private set; }

        #endregion

        #region Constructors

        /// <summary>
        /// Creates an instance of <see cref="Research.MapReduce.Samples.WordCount.WordCountWriter"/>.
        /// </summary>
        /// <param name="cloudClient">Instance of <see cref="Research.MapReduce.Core.CloudClient"/> 
        /// for accessing azure blob storage.</param>
        /// <param name="containerName">Name of <see cref="Microsoft.WindowsAzure.StorageClient.CloudBlobContainer" />
        /// to which output of <see cref="Research.MapReduce.Samples.WordCount.WordCountReducer"/> is to be written.</param>
        public FileTransformWriter(CloudClient cloudClient, string containerName)
        {
            if (cloudClient == null)
            {
                throw new ArgumentNullException("cloudClient");
            }
            if (string.IsNullOrEmpty(containerName) || string.IsNullOrWhiteSpace(containerName))
            {
                throw new ArgumentNullException("containerName");
            }
            this.CloudClient = cloudClient;
            this.ContainerName = containerName;
        }

        #endregion

        #region Methods

        /// <summary>
        /// Writes the provided output records of <see cref="Research.MapReduce.Samples.WordCount.WordCountReducer"/>
        /// to <see cref="Microsoft.WindowsAzure.StorageClient.CloudBlob"/>.
        /// </summary>
        /// <param name="outputPartition">Name of the <see cref="Microsoft.WindowsAzure.StorageClient.
        /// CloudBlob"/> to which records are to be written.</param>
        /// <param name="records">Output records of <see cref="Research.MapReduce.Samples.WordCount.WordCountReducer"/>.</param>
        public void Write(string outputPartition, IEnumerable<KeyValuePair<string, int>> records)
        {
            CloudBlobContainer container = this.CloudClient.BlobClient.GetContainerReference(this.
                ContainerName);
            container.CreateIfNotExist();
            CloudBlob blob = container.GetBlobReference(outputPartition);
            using (StreamWriter stream = new StreamWriter(blob.OpenWrite()))
            {
                foreach (KeyValuePair<string, int> record in records)
                {
                    stream.WriteLine("{0},{1}", record.Key, record.Value);
                }
            }
        }

        /// <summary>
        /// Returns null since results need not be sent back to 
        /// <see cref="Research.MapReduce.Samples.WordCount.FileTransformController"/>.
        /// </summary>
        /// <returns>Null</returns>
        public IReduceResult<string, int> GetResult()
        {
            // Note:- Returning null here since results need not sent back to controller and 
            // 'Job.GetReduceOutputsToController' will not be set to 'true' during job submission.
            return null;
        }

        #endregion
    }
}