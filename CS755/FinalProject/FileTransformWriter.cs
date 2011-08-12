/*
 * FILE:        FileTransformWriter.cs
 *                                                                      
 * DESCRIPTION: The file contains implementation to write the output of 'FileTransformReducer' in XML format.
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
	/// Writes the output of <see cref="FileTransformReducer"/> 
    /// in XML format.
    /// </summary>
    [Serializable]
    public sealed class FileTransformWriter : IRecordWriter<int, string>
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
		/// Creates an instance of <see cref="FileTransformWriter"/>.
        /// </summary>
        /// <param name="cloudClient">Instance of <see cref="Research.MapReduce.Core.CloudClient"/> 
        /// for accessing azure blob storage.</param>
        /// <param name="containerName">Name of <see cref="Microsoft.WindowsAzure.StorageClient.CloudBlobContainer" />
        /// to which output of <see cref="FileTransformReducer"/> is to be written.</param>
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
		/// Writes the provided output records of <see cref="FileTransformReducer"/>
        /// to <see cref="Microsoft.WindowsAzure.StorageClient.CloudBlob"/>.
        /// </summary>
        /// <param name="outputPartition">Name of the <see cref="Microsoft.WindowsAzure.StorageClient.
        /// CloudBlob"/> to which records are to be written.</param>
		/// <param name="records">Output records of <see cref="FileTransformReducer"/>.</param>
        public void Write(string outputPartition, IEnumerable<KeyValuePair<int, string>> records)
        {
        	FileTransformController.RecordCount = 0;

            CloudBlobContainer container = this.CloudClient.BlobClient.GetContainerReference(this.
                ContainerName);
            container.CreateIfNotExist();
            CloudBlob blob = container.GetBlobReference(outputPartition);
            using (StreamWriter stream = new StreamWriter(blob.OpenWrite()))
            {
				stream.Write("<root>");
                foreach (KeyValuePair<int, string> record in records)
                {
                    stream.Write(record.Value);
                	FileTransformController.RecordCount++;
                }
				stream.Write("</root>");
            }
        }

        /// <summary>
        /// Returns null since results need not be sent back to 
        /// <see cref="FileTransformController"/>.
        /// </summary>
        /// <returns>Null</returns>
        public IReduceResult<int, string> GetResult()
        {
            // Note:- Returning null here since results need not sent back to controller and 
            // 'Job.GetReduceOutputsToController' will not be set to 'true' during job submission.
            return null;
        }

        #endregion
    }
}