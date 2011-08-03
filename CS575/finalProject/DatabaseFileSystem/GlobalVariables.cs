using System.Collections.Generic;

namespace DatabaseFileSystem
{
    static class GlobalVariables
    {
        private static string _currentPath = "";
        private static Dictionary<string, bool> _fileOnClipboard = new Dictionary<string, bool>();

        public static string CurrentPath
        {
            get { return _currentPath; }
            set { _currentPath = value; }
        }

        public static Dictionary<string, bool> FileOnClipboard
        {
            get { return _fileOnClipboard; }
            set { _fileOnClipboard = value; }
        }
    }
}
