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
using System.Windows.Threading;

namespace CS701Hw6
{
    public partial class MainPage : UserControl
    {
        private static Random _random = new Random();
		private DispatcherTimer dispatcherTimer;

        public MainPage()
		{
			// Required to initialize variables
			InitializeComponent();
            CourseDataCollection dc = new CourseDataCollection();
			chart1.DataContext = dc;
			
			 // Create a timer to update the dynamic data regularly
            dispatcherTimer = new DispatcherTimer();
            dispatcherTimer.Interval = TimeSpan.FromSeconds(2);
            dispatcherTimer.Tick += delegate
            {
           		dc.Clear();
                dc.Add(new CourseData { Id = "MET CS341", Name = "Data Structures", Instructor = "Maslanka", Enrollment = getRandomValue() });
                dc.Add(new CourseData { Id = "MET CS701", Name = "Adv Web App Dev", Instructor = "Kalathur", Enrollment = getRandomValue() });
                dc.Add(new CourseData { Id = "CAS CS105", Name = "Databases", Instructor = "Sullivan", Enrollment = getRandomValue() });
                dc.Add(new CourseData { Id = "CAS CS455", Name = "Computer Networks", Instructor = "Crovella", Enrollment = getRandomValue() });
			};
		}
		
		private int getRandomValue() 
		{
			return _random.Next(0, 50);	
		}
		
		private void OnClick_Start(object sender, RoutedEventArgs e)
		{
			dispatcherTimer.Start();
			stopButton.IsEnabled = true;
			startButton.IsEnabled = false;
		}

		private void OnClick_Stop(object sender, RoutedEventArgs e)
		{
			dispatcherTimer.Stop();
			stopButton.IsEnabled = false;
			startButton.IsEnabled = true;
		}
    }
}
