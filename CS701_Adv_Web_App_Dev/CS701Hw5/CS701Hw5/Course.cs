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

namespace CS701Hw5
{
    public class Course
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public string Instructor { get; set; }
        public string Day { get; set; }

        public Course(string id, string name, 
            string instructor, string day)
        {
            Id = id;
            Name = name;
            Instructor = instructor;
            Day = day;
        }
    }
}
