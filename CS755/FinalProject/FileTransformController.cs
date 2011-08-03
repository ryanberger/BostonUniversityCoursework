/*
 * FILE:        WordCountController.cs
 *                                                                      
 * DESCRIPTION: The file contains implementation for providing the 'controller' functionality for WordCount sample.
 *
 */

using System;
using Microsoft.WindowsAzure.StorageClient;
using Research.MapReduce.Core;
using Research.MapReduce.Library;

namespace CS755.MapReduce.FileTransform
{
    /// <summary>
    /// Provides the 'controller' functionality for WordCount sample.
    /// </summary>
    [Controller(Name = "WordCountController", Description = "Counts the number of times each unique word is found in the input data.")]
    public sealed class FileTransformController : Controller
    {
        #region Input JobParameters

        /// <summary>
        /// Duration (in minutes) within which the wordcount job must complete.
        /// </summary>
        [ControllerParameter(Name = ParameterNames.JobTimeoutInMinutes, Description = "Duration within which a single k-means job must complete.", DefaultValue = "5", IsRequired = true)]
        public int JobTimeoutInMinutes { get; set; }

        #endregion

        #region Methods

        /// <summary>
        /// Entry point for the execution of <see cref="Research.MapReduce.Samples.WordCount.FileTransformController"/>.
        /// </summary>
        public override void Run()
        {
            ValidateInputParameters();

            JobConfiguration jobConf = new JobConfiguration
            {
                MapperType = typeof(FileTransformMapper),
                CombinerType = typeof(WordCountReducer),
                ReducerType = typeof(WordCountReducer),
                MapOutputStorage = MapOutputStoreType.Local,
                KeyPartitioner = typeof(HashModuloKeyPartitioner<string, int>),
                ExceptionPolicy = new TerminateOnFirstException(),
                JobTimeout = TimeSpan.FromMinutes(JobTimeoutInMinutes)
            };

            Job job = new Job(jobConf, this);
            job.DataPartitioner = new BlobTextPartitioner(this.CloudClient, this.
                InputDataLocation, this.RequestedNumberOfMappers);
            job.RecordWriter = new FileTransformWriter(this.CloudClient, this.OutputDataLocation);
            job.NoOfReduceTasks = this.RequestedNumberOfReducers;

            job.Run();
        }

        /// <summary>
        /// Validates the input parameters provided for running WordCount.
        /// </summary>
        private void ValidateInputParameters()
        {
            if (string.IsNullOrEmpty(this.StorageConnectionString) || string.IsNullOrWhiteSpace(
                this.StorageConnectionString))
            {
                throw new ArgumentNullException("StorageConnectionString");
            }
            if (string.IsNullOrEmpty(this.InputDataLocation) || string.IsNullOrWhiteSpace(
                this.InputDataLocation))
            {
                throw new ArgumentNullException("InputDataLocation");
            }
            else
            {
                ValidateInputDataLocation();
            }

            if (string.IsNullOrEmpty(this.OutputDataLocation) || string.IsNullOrWhiteSpace(
                this.OutputDataLocation))
            {
                throw new ArgumentNullException("OutputDataLocation");
            }
            else
            {
                ValidateOutputDataLocation();
            }

            if (this.RequestedNumberOfMappers <= 0)
            {
                throw new ArgumentException("Mappers should be greater than zero.");
            }

            if (this.RequestedNumberOfReducers <= 0)
            {
                throw new ArgumentException("Reducers should be greater than zero.");
            }

            if (this.JobTimeoutInMinutes <= 0)
            {
                throw new ArgumentException("JobTimeoutInMinutes should be greater than zero.");
            }
        }

        /// <summary>
        /// Validates the input parameter 'InputDataLocation'.
        /// </summary>
        private void ValidateInputDataLocation()
        {
            this.InputDataLocation.ValidateUriFormat("InputDataLocation");
            try
            {
                CloudBlob blob = this.CloudClient.BlobClient.GetBlobReference(this.
                    InputDataLocation);
                blob.FetchAttributes();
                if (blob.Properties.Length <= 0)
                {
                    throw new ArgumentException(
                    string.Format(
                        "Input location [{0}] is empty.",
                        this.InputDataLocation));
                }
            }
            catch (StorageException ex)
            {
                throw new ArgumentException(
                    string.Format(
                        "Error occurred while trying to access input location [{0}]",
                        this.InputDataLocation),
                    ex);
            }
        }
        
        /// <summary>
        /// Creates an instance of the <see cref="Microsoft.WindowsAzure.StorageClient.CloudBlobContainer"/>
        /// specified by input parameter 'OutputDataLocation' if it does not exist. Deletes all blobs from 
        /// the container if it exists.
        /// </summary>
        private void ValidateOutputDataLocation()
        {
            try
            {
                CloudBlobContainer outputContainer = this.CloudClient.BlobClient.GetContainerReference(
                    this.OutputDataLocation);
                outputContainer.CreateIfNotExist();
                outputContainer.Clean();
            }
            catch (StorageException ex)
            {
                throw new ArgumentException(
                    string.Format(
                        "Error occurred while trying to initialize output location [{0}]",
                        this.OutputDataLocation),
                    ex);
            }
        }

        #endregion
    }
}
