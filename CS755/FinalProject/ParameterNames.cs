/*
 * FILE:        ParameterNames.cs
 *                                                                      
 * DESCRIPTION: The file contains names of parameters used by WordCountController.
 *
 */

namespace CS755.MapReduce.FileTransform
{
    /// <summary>
    /// Contains names of parameters used by <see cref="Research.MapReduce.Samples.WordCount.FileTransformController"/>.
    /// </summary>
    public class ParameterNames
    {
        #region Fields

        public const string InputDataLocation = "InputDataLocation";
        public const string OutputDataLocation = "OutputDataLocation";
        public const string Mappers = "Mappers";
        public const string Reducers = "Reducers";
        public const string JobTimeoutInMinutes = "JobTimeoutInMinutes";

        #endregion
    }
}
