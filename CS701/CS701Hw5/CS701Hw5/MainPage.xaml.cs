using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace CS701Hw5
{
    public partial class MainPage : UserControl
    {
        public MainPage()
        {
            InitializeComponent();
        }

        private void RemoveButton_Click(object sender, RoutedEventArgs e)
        {
            Course selected = SelectedCourseGrid.SelectedItem as Course;
            
            if (selected != null)
            {
                SelectedCourseCollection sc = Resources["selectedCourseList"] as SelectedCourseCollection;
                sc.Remove(selected);
            }
        }

        private void SelectButton_Click(object sender, RoutedEventArgs e)
        {
            Course selected = CourseGrid.SelectedItem as Course;

            SelectedCourseCollection sc = Resources["selectedCourseList"] as SelectedCourseCollection;

            if (selected != null && !sc.Contains(selected))
            {
                sc.Add(selected);
            }

        }

        private void SummaryButton_Click(object sender, RoutedEventArgs e)
        {
            SelectedCourseCollection currentItems = SelectedCourseGrid.ItemsSource as SelectedCourseCollection;
			summaryBlock.Text = "Selected Courses: \n\n";

			if (currentItems != null)
			{
				foreach (var item in currentItems)
				{
					Course c = item as Course;
					summaryBlock.Text += string.Format("Course: {0} {1} {2} {3};\n", c.Id, c.Name, c.Instructor, c.Day);
				}
			}
        }

    }
}
