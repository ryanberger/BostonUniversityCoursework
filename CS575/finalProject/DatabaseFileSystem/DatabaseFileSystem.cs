using System;
using System.Collections.Generic;
using System.Text;
using DatabaseFileSystem.FileSystemDataSetTableAdapters;

namespace DatabaseFileSystem
{
    class DatabaseFileSystem
    {
        private readonly FilesTableAdapter _tableAdapter = new FilesTableAdapter();

        /// <summary>
        /// Adds the file to the database.
        /// </summary>
        /// <param name="filename">The filename.</param>
        /// <param name="type">The type.</param>
        /// <param name="size">The size.</param>
        /// <param name="path">The path.</param>
        /// <param name="permission">The permission.</param>
        /// <param name="dateCreated">The date created.</param>
        /// <param name="dateModified">The date modified.</param>
        /// <param name="dateAccessed">The date accessed.</param>
        /// <param name="readOnly">if set to <c>true</c> [read only].</param>
        /// <param name="hidden">if set to <c>true</c> [hidden].</param>
        public void addFile(string filename, string type, long size, string path, string permission, DateTime dateCreated, DateTime dateModified, DateTime dateAccessed, bool readOnly, bool hidden)
        {
            // Check to make sure file isn't already in database
            if (_tableAdapter.CountQuery(filename, path) == 0)
            {
                _tableAdapter.Insert(filename, type, size, path, permission, dateCreated, dateModified, dateAccessed, readOnly, hidden);
            }
        }

        /// <summary>
        /// Deletes the file from the database.
        /// </summary>
        /// <param name="filename">The filename.</param>
        /// <param name="path">The path.</param>
        public void deleteFile(string filename, string path)
        {
            _tableAdapter.DeleteFile(filename, path);
        }

        /// <summary>
        /// Updates the file in the database.
        /// </summary>
        /// <param name="newPath">The new path.</param>
        /// <param name="filename">The filename.</param>
        /// <param name="oldPath">The old path.</param>
        public void updateFile(string newPath, string filename, string oldPath)
        {
            // Check to make sure file isn't already in database
            if (_tableAdapter.CountQuery(filename, newPath) == 0)
            {
                _tableAdapter.UpdateQuery(newPath, filename, oldPath);
            }
        }

        /// <summary>
        /// Searches the file in the database.
        /// </summary>
        /// <param name="searchTerm">The search term.</param>
        /// <returns></returns>
        public FileSystemDataSet.FilesDataTable searchFile(string searchTerm)
        {
            searchTerm = string.Format("%{0}%", searchTerm); // Add in wildcard characters on each side
            FileSystemDataSet.FilesDataTable dataTable = _tableAdapter.SearchData(searchTerm);
            return dataTable;
        }
    }
}
