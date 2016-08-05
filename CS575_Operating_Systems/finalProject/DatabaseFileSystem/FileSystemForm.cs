using System;
using System.Windows.Forms;
using System.IO;
using System.Collections.Generic;
using Microsoft.VisualBasic;
using System.Data;

namespace DatabaseFileSystem
{
    
    public partial class FileSystemForm : Form
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="FileSystemForm"/> class.
        /// </summary>
        public FileSystemForm()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Handles the Load event of the FE_frm control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void FE_frm_Load(object sender, EventArgs e)
        {
            foreach (DriveInfo drv in DriveInfo.GetDrives())
            {
                if(drv.IsReady)
                {
                    TreeNode t = new TreeNode();
                    t.Text = drv.Name;   
                    t.Nodes.Add("");
                    treeView.Nodes.Add(t);
                }
            }
        }

        /// <summary>
        /// Handles the BeforeExpand event of the treeView1 control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.Windows.Forms.TreeViewCancelEventArgs"/> instance containing the event data.</param>
        private void treeView1_BeforeExpand(object sender, TreeViewCancelEventArgs e)
        {
            try
            {
                TreeNode parentnode = e.Node;
                DirectoryInfo dr = new DirectoryInfo(parentnode.FullPath);
                parentnode.Nodes.Clear();
                foreach (DirectoryInfo dir in dr.GetDirectories())
                {
                    TreeNode node = new TreeNode();
                    node.Text = dir.Name;
                    node.Nodes.Add("");
                    parentnode.Nodes.Add(node);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error!");
            }
        }

        /// <summary>
        /// Handles the AfterSelect event of the treeView1 control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.Windows.Forms.TreeViewEventArgs"/> instance containing the event data.</param>
        private void treeView1_AfterSelect(object sender, TreeViewEventArgs e)
        {
            try
            {
                TreeNode current = e.Node;
                string path = current.FullPath;
                refreshFiles(path);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error!");
            }
        }

        /// <summary>
        /// Refreshes the files.
        /// </summary>
        /// <param name="path">The path.</param>
        private void refreshFiles(string path)
        {
            GlobalVariables.CurrentPath = path;
            string[] Files = Directory.GetFiles(path);
            string[] Directories = Directory.GetDirectories(path);
            string[] subinfo = new string[4];
            listView.Clear();
            listView.Columns.Add("Name", 255);
            listView.Columns.Add("Size", 100);
            listView.Columns.Add("Type", 80);
            listView.Columns.Add("Date Modified", 150);
            foreach (string Dname in Directories)
            {
                subinfo[0] = GetFileName(Dname);
                subinfo[1] = "";
                subinfo[2] = "FOLDER";
                subinfo[3] = "";
                ListViewItem DItems = new ListViewItem(subinfo);
                listView.Items.Add(DItems);
            }
            foreach (string Fname in Files)
            {
                subinfo[0] = GetFileName(Fname);
                subinfo[1] = GetSizeInfo(Fname);
                subinfo[2] = "FILE";
                subinfo[3] = GetDateModified(Fname).ToString();
                ListViewItem FItems = new ListViewItem(subinfo);
                listView.Items.Add(FItems);
            }
        }

        /// <summary>
        /// Searches the database for files and returns the results.
        /// </summary>
        /// <param name="searchTerm">The search term.</param>
        private void searchFiles(string searchTerm)
        {
            DatabaseFileSystem dfs = new DatabaseFileSystem();
            string[] subinfo = new string[5];
            listView.Clear();
            listView.Columns.Add("Name", 255);
            listView.Columns.Add("Path", 300);
            listView.Columns.Add("Size", 100);
            listView.Columns.Add("Type", 80);
            listView.Columns.Add("Date Modified", 150);

            FileSystemDataSet.FilesDataTable dataTable = dfs.searchFile(searchTerm);

            foreach (DataRow dr in dataTable.Rows)
            {
                subinfo[0] = dr["filename"].ToString();
                subinfo[1] = dr["path"].ToString();
                subinfo[2] = dr["size"].ToString();
                subinfo[3] = dr["type"].ToString();
                subinfo[4] = dr["date_modified"].ToString();
                ListViewItem FItems = new ListViewItem(subinfo);
                listView.Items.Add(FItems);
            }

        }

        /// <summary>
        /// Gets the name of the file.
        /// </summary>
        /// <param name="path">The path.</param>
        /// <returns></returns>
        public string GetFileName(string path)
        {
            int Nameindex = path.LastIndexOf('\\');
            return path.Substring(Nameindex + 1);
        }

        /// <summary>
        /// Gets the raw size of the file.
        /// </summary>
        /// <param name="path">The path.</param>
        /// <returns></returns>
        public long GetRawSize(string path)
        {
            FileInfo fi = new FileInfo(path);
            long size = fi.Length;
            return size;
        }

        /// <summary>
        /// Gets the size info of a file in a readable format.
        /// </summary>
        /// <param name="path">The path.</param>
        /// <returns></returns>
        public string GetSizeInfo(string path)
        {
            FileInfo fi = new FileInfo(path);
            long size = fi.Length;
            string txtsize = "";
            if (size < 1024)
            {
                txtsize = "byte";
            }
            else if (size > 1024)
            {
                size = size / 1024;
                txtsize = "Kb";
            }
            if (size > 1024)
            {
                size = size / 1024;
                txtsize = "Mb";
            }
            if (size > 1024)
            {
                size = size / 1024;
                txtsize = "Gb";
            }
            return size + " " + txtsize;
        }

        public DateTime GetDateAdded(string path)
        {
            FileInfo fi = new FileInfo(path);
            DateTime dateAdded = fi.CreationTime;
            return dateAdded;
        }

        public DateTime GetDateModified(string path)
        {
            FileInfo fi = new FileInfo(path);
            DateTime dateModified = fi.LastWriteTime;
            return dateModified;
        }

        public DateTime GetDateAccessed(string path)
        {
            FileInfo fi = new FileInfo(path);
            DateTime dateAccessed = fi.LastAccessTime;
            return dateAccessed;
        }

        public bool IsReadOnly(string path)
        {
            FileInfo fi = new FileInfo(path);
            bool isReadOnly = fi.IsReadOnly;
            return isReadOnly;
        }

        /// <summary>
        /// Handles the Click event of the selectAllToolStripMenuItem control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void selectAllToolStripMenuItem_Click(object sender, EventArgs e)
        {
            for (int i = 0; i < listView.Items.Count; i++)
            {
                listView.Items[i].Selected = true;
            }
        }

        /// <summary>
        /// Handles the Click event of the exitToolStripMenuItem1 control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void exitToolStripMenuItem1_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        /// <summary>
        /// Handles the Click event of the newToolStripMenuItem control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void newToolStripMenuItem_Click(object sender, EventArgs e)
        {
            string newFileName = Interaction.InputBox("Enter the filename", "Filename", "default.txt", 0, 0);

            // Combine the new file name with the path
            string newFile = System.IO.Path.Combine(GlobalVariables.CurrentPath, newFileName);

            // Create the file
            if (!System.IO.File.Exists(newFile))
            {
                using (System.IO.FileStream fs = System.IO.File.Create(newFile))
                { }
                DatabaseFileSystem dfs = new DatabaseFileSystem();
                dfs.addFile(newFileName, "FILE", 0, GlobalVariables.CurrentPath, "rwxrwxrwx", DateTime.Now, DateTime.Now, DateTime.Now, false, false);
            }
            else
            {
                MessageBox.Show(GlobalVariables.CurrentPath + newFile + " already exists!", "Error");
            }

            refreshFiles(GlobalVariables.CurrentPath);
        }


        /// <summary>
        /// Handles the 1 event of the aboutToolStripMenuItem_Click control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void aboutToolStripMenuItem_Click_1(object sender, EventArgs e)
        {
            MessageBox.Show("Database File System by: Ryan Berger\nCS575", "About");
        }

        /// <summary>
        /// Handles the Click event of the toolStripMenuItem1 control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void toolStripMenuItem1_Click(object sender, EventArgs e)
        {
            string newFolderName = Interaction.InputBox("Enter the folder name", "Folder name", "default", 0, 0);

            string newPath = System.IO.Path.Combine(GlobalVariables.CurrentPath, newFolderName);

            // Create the folder
            System.IO.Directory.CreateDirectory(newPath);

            refreshFiles(GlobalVariables.CurrentPath);
        }

        /// <summary>
        /// Handles the Click event of the copyToolStripMenuItem control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void copyToolStripMenuItem_Click(object sender, EventArgs e)
        {
            GlobalVariables.FileOnClipboard.Clear();
            for (int i = 0; i < listView.Items.Count; i++)
            {
                if (listView.Items[i].Selected)
                    GlobalVariables.FileOnClipboard.Add(System.IO.Path.Combine(GlobalVariables.CurrentPath, listView.Items[i].Text), false);
            }
        }

        /// <summary>
        /// Handles the Click event of the cutToolStripMenuItem control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void cutToolStripMenuItem_Click(object sender, EventArgs e)
        {
            GlobalVariables.FileOnClipboard.Clear();
            for (int i = 0; i < listView.Items.Count; i++)
            {
                if (listView.Items[i].Selected)
                    GlobalVariables.FileOnClipboard.Add(System.IO.Path.Combine(GlobalVariables.CurrentPath, listView.Items[i].Text), true);
            }
        }

        /// <summary>
        /// Handles the Click event of the pasteToolStripMenuItem control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void pasteToolStripMenuItem_Click(object sender, EventArgs e)
        {
            DatabaseFileSystem dfs = new DatabaseFileSystem();
            string fileName = string.Empty;
            string oldPath = string.Empty;
            string newPath = string.Empty;
            string destFile = string.Empty;
            Dictionary<string, bool> files = GlobalVariables.FileOnClipboard;

            foreach (KeyValuePair<string, bool> s in files)
            {
                // Use static Path methods to extract only the file name from the path.
                fileName = System.IO.Path.GetFileName(s.Key);
                oldPath = System.IO.Path.GetDirectoryName(s.Key);
                newPath = GlobalVariables.CurrentPath;
                destFile = System.IO.Path.Combine(newPath, fileName);
                try
                {
                    if (s.Value) // Cut-mode
                    {
                        dfs.updateFile(newPath, fileName, oldPath);
                        System.IO.File.Move(s.Key, destFile);
                    }
                    else // Copy-mode
                    {
                        dfs.addFile(fileName, "FILE", GetRawSize(destFile), newPath, "rwxrwxrwx", GetDateAdded(destFile), GetDateModified(destFile), GetDateAccessed(destFile), IsReadOnly(destFile), false);
                        System.IO.File.Copy(s.Key, destFile, false);
                    }
                }
                catch (IOException ex)
                {
                    MessageBox.Show(ex.Message, "Error!");
                }
            }

            refreshFiles(GlobalVariables.CurrentPath);
        }

        /// <summary>
        /// Handles the Click event of the deleteToolStripMenuItem control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void deleteToolStripMenuItem_Click(object sender, EventArgs e)
        {
            DatabaseFileSystem dfs = new DatabaseFileSystem();
            string filename = string.Empty;

            if (MessageBox.Show("Really delete?", "Confirm delete", MessageBoxButtons.YesNo) == DialogResult.Yes)
            {
                for (int i = 0; i < listView.Items.Count; i++)
                {
                    if (listView.Items[i].Selected)
                    {
                        filename = listView.Items[i].Text;
                        System.IO.File.Delete(System.IO.Path.Combine(GlobalVariables.CurrentPath, filename));

                        dfs.deleteFile(filename, GlobalVariables.CurrentPath);

                        // TODO: Delete a directory and all subdirectories...
                        //try
                        //{
                        //    System.IO.Directory.Delete(System.IO.Path.Combine(GlobalVariables.CurrentPath, listView.Items[i].Text), true);
                        //}

                        //catch (System.IO.IOException ex)
                        //{
                        //    MessageBox.Show(ex.Message, "Error!");
                        //}
                    }
                }
            }
            refreshFiles(GlobalVariables.CurrentPath);
        }

        /// <summary>
        /// Handles the Click event of the indexSelectedToolStripMenuItem control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void indexSelectedToolStripMenuItem_Click(object sender, EventArgs e)
        {
            DatabaseFileSystem dfs = new DatabaseFileSystem();
            string fullPath = string.Empty;

            for (int i = 0; i < listView.Items.Count; i++)
            {
                if (listView.Items[i].Selected)
                {
                    fullPath = GlobalVariables.CurrentPath + "\\" + listView.Items[i].Text;
                    dfs.addFile(listView.Items[i].Text, "FILE", GetRawSize(fullPath), GlobalVariables.CurrentPath, "rwxrwxrwx", GetDateAdded(fullPath), GetDateModified(fullPath), GetDateAccessed(fullPath), IsReadOnly(fullPath), false);
                }
            }
        }

        /// <summary>
        /// Handles the TextChanged event of the textBox1 control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="System.EventArgs"/> instance containing the event data.</param>
        private void textBox1_TextChanged(object sender, EventArgs e)
        {
            if (searchBox.Text.Length > 2)
            {
                searchFiles(searchBox.Text);
            }
        }
    }
}