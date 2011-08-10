/*
 * FILE:        Program.cs
 *                                                                      
 * DESCRIPTION: The file contains the implementation for a client application for running WordCount sample.
 *
 */

using System;
using System.Collections.Generic;
using System.Configuration;
using Microsoft.WindowsAzure;
using Microsoft.WindowsAzure.StorageClient;
using Research.MapReduce.Core;
using CS755.MapReduce.FileTransform.Properties;

namespace CS755.MapReduce.FileTransform
{
    class Program
    {
        #region Fields

        private const string PackageName = "FileTransform";
        private static string masterConnectionString;
        private static CloudStorageAccount storageAccount;
        private static Dictionary<string, string> controllerArgs;

        #endregion

        #region Methods

        static void Main(string[] args)
        {
            // Read values of 'masterConnectionString' and 'storageAccount' from app.config.
            ValidateAndGetConfiguration();

            if (args.Length == 0)
            {
                Console.WriteLine();
                Console.WriteLine(@"Usage:- -InputDataLocation::<value> -OutputDataLocation::<value> -Mappers::<value> -Reducers::<value> -JobTimeoutInMinutes::<value>");
                args = GetDefaultInputArgs();
                Console.WriteLine();
                Console.WriteLine("Using default arguments >> {0}", args.GetString());
                Console.WriteLine();
            }

            // Populate controller parameter values from input arguments.
            controllerArgs = new Dictionary<string, string>();
            foreach (string arg in args)
            {
                string[] argArray = arg.Split(new string[] { "::" }, StringSplitOptions.
                    RemoveEmptyEntries);
                controllerArgs.Add(argArray[0].TrimStart('-'), argArray[1]);
            }

            // Submit application for execution and wait till it completes.
            // Note:- Using a timeout that is slightly greater than the wordcount job. Use a larger
            // timeout if other applications have already been submitted before this one.
            int timeoutInMinutes = int.Parse(controllerArgs[ParameterNames.JobTimeoutInMinutes]) + 2;
            SubmitAndWaitForApplication(timeoutInMinutes);

            Console.WriteLine("Press <ENTER> to exit.");
            Console.ReadLine();
        }

        /// <summary>
        /// Reads and validates values of 'masterConnectionString' and 'StorageConnectionString' 
        /// from app.config.         
        /// </summary>
        private static void ValidateAndGetConfiguration()
        {
            masterConnectionString = ConfigurationManager.AppSettings["MasterConnectionString"];
            string storageConnectionString = ConfigurationManager.AppSettings["MasterConnectionString"];

            if (string.IsNullOrEmpty(masterConnectionString) || string.IsNullOrWhiteSpace(masterConnectionString))
            {
                throw new InvalidOperationException("MasterConnectionString configuration missing.");
            }
            if (string.IsNullOrEmpty(storageConnectionString) || string.IsNullOrWhiteSpace(storageConnectionString))
            {
                throw new InvalidOperationException("StorageConnectionString configuration missing.");
            }

            if (!CloudStorageAccount.TryParse(masterConnectionString, out storageAccount))
            {
                throw new InvalidOperationException("MasterConnectionString incorrect.");
            }

            if (!CloudStorageAccount.TryParse(storageConnectionString, out storageAccount))
            {
                throw new InvalidOperationException("StorageConnectionString incorrect.");
            }
        }

        /// <summary>
        /// Gets default input arguments for running the sample.
        /// </summary>
        /// <returns>String array containing the arguments for running the sample.</returns>
        private static string[] GetDefaultInputArgs()
        {
            string inputContainerName = "wordcount-input-" + Environment.UserName;
            string outputContainerName = "wordcount-output-" + Environment.UserName;

            string[] args = new string[5];

            // Arg0 - InputDataLocation
            CloudBlobContainer container = GetInitializedContainer(inputContainerName);
            CloudBlob blob = container.GetBlobReference("sampleinput.txt");
            blob.UploadText(Resources.sampleinput);
            args[0] = string.Format("-{0}::{1}", ParameterNames.InputDataLocation, blob.Uri);

            // Arg1 - OutputDataLocation          
            container = GetInitializedContainer(outputContainerName);
            args[1] = string.Format("-{0}::{1}", ParameterNames.OutputDataLocation,
                outputContainerName);

            // Arg2 - Mappers
            args[2] = string.Format("-{0}::{1}", ParameterNames.Mappers, 4);

            // Arg3 - Reducers
            args[3] = string.Format("-{0}::{1}", ParameterNames.Reducers, 2);

            // Arg4 - JobTimeoutInMinutes
            args[4] = string.Format("-{0}::{1}", ParameterNames.JobTimeoutInMinutes, 2);

            return args;
        }
        
        /// <summary>
        /// Creates a <see cref="Microsoft.WindowsAzure.StorageClient.CloudBlobContainer"/> if it does 
        /// not already exist, cleans the container if it exists.
        /// </summary>
        /// <param name="name">Name of the <see cref="Microsoft.WindowsAzure.StorageClient.CloudBlobContainer"/>
        /// to be initialized.</param>
        /// <returns>Cleaned <see cref="Microsoft.WindowsAzure.StorageClient.CloudBlobContainer"/> 
        /// object.</returns>
        private static CloudBlobContainer GetInitializedContainer(string name)
        {
            CloudBlobContainer container = storageAccount.CreateCloudBlobClient().
                GetContainerReference(name);
            container.CreateIfNotExist();
            container.Clean();
            return container;
        }

        /// <summary>
        /// Submits the WordCount application for execution and waits till it completes.
        /// </summary>
        /// <param name="timeoutInMinutes">Timeout (in minutes) value for the application.</param>
        private static void SubmitAndWaitForApplication(int timeoutInMinutes)
        {
            MapReduceClient client = new MapReduceClient(PackageName, Guid.NewGuid().
                ToString("N"), typeof(FileTransformController).AssemblyQualifiedName, controllerArgs);
            client.Submit(masterConnectionString);

            Console.WriteLine("Application submitted successfully...");

            Console.WriteLine("Waiting for completion...");

            bool concluded = client.WaitForCompletion(masterConnectionString, TimeSpan.
                FromMinutes(timeoutInMinutes), TimeSpan.FromSeconds(5));
            if (concluded)
            {
                if (client.Succeeded)
                {
                    Console.WriteLine("Application completed successfully. Output is available in blob container: {0}",
                        controllerArgs[ParameterNames.OutputDataLocation]);
                }
                else
                {
                    Console.WriteLine("Application failed due to: \n {0}", client.FailReason);
                }
            }
            else
            {
                Console.WriteLine("Application wait timeout elapsed.");
            }
        }

        #endregion
    }
}
