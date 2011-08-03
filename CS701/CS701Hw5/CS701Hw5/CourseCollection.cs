using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Collections.ObjectModel;

namespace CS701Hw5
{
    public class CourseCollection : ObservableCollection<Course>
    {
        public CourseCollection()
        {
			Add(new Course("MET CS341", "Data Structures", "Maslanka", "Wednesday"));
            Add(new Course("MET CS701", "Adv Web App Dev", "Kalathur", "Wednesday"));
            Add(new Course("CAS CS105", "Databases", "Sullivan", "Monday"));
            Add(new Course("CAS CS455", "Computer Networks", "Crovella", "Thursday"));
        }
    }
}
