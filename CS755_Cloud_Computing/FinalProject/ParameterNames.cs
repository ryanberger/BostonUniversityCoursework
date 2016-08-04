/*
 * FILE:        ParameterNames.cs
 *                                                                      
 * DESCRIPTION: The file contains names of parameters used by FileTransformController.
 *
 */

namespace CS755.MapReduce.FileTransform
{
    /// <summary>
    /// Contains names of parameters used by <see cref="FileTransformController"/>.
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
